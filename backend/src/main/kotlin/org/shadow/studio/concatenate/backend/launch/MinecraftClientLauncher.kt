package org.shadow.studio.concatenate.backend.launch

import org.shadow.studio.concatenate.backend.Concatenate
import org.shadow.studio.concatenate.backend.adapter.JavaAdapter
import org.shadow.studio.concatenate.backend.data.MinecraftClientInstance
import org.shadow.studio.concatenate.backend.util.*
import org.slf4j.LoggerFactory
import java.io.File

class MinecraftClientLauncher(
    adapter: JavaAdapter,
    private val clientConfig: MinecraftClientConfig,
    override val workingDirectory: File,
    override val version: MinecraftVersion,
    override val environments: Map<String, String> = mapOf(),
    private val isCheckFileIntegrity: Boolean = true
) : MinecraftLauncher() {

    // Get the path of Java
    override val program = adapter.getJavaBin(version.versionId)
    private val logger = LoggerFactory.getLogger(MinecraftClientLauncher::class.java)
    private val librariesDirectoryName = "libraries"
    private val assetsDirectoryName = "assets"
    private val profileLibrariesKey = "libraries"
    private val profileAssetsKey = "assets"
    private val profileMainClassKey = "mainClass"
    private var isEnableMinecraftLogging = true

    override fun launch(): MinecraftClientInstance = jsonObjectConvGet {
        val profile = parseJson(version.getJsonProfile())
        val versionJar = version.getJarFile()
        val versionJarSha1 = profile["downloads"]["client"]["sha1"] as String
        val versionJarSize = profile["downloads"]["client"]["size"].toString().toLong()

        if (isCheckFileIntegrity) {
            if (!checkSum(versionJar, versionJarSize, versionJarSha1))
                error("version.jar file is damaged")

            val assetsIndex = profile["assetIndex"]["id"] as String
            val indexJsonFile = findAssetIndexJson(assetsIndex)

            if (!checkAssetsSum(parseJson(indexJsonFile.readText())))
                error("assets files not complete")
        }

        val librariesRoot = File(workingDirectory, librariesDirectoryName)
        val gameDirectory = if (version.isolated) findVersionIsolateDirectory() else workingDirectory
        val assetsRoot = File(workingDirectory, assetsDirectoryName)
        val nativesDirectory = findNativeDirectory()
        val javaBin = File(program)

        checkExists(librariesRoot)
        checkExists(gameDirectory)
        checkExists(assetsRoot)
//        checkExists(nativesDirectory) // 1.20 seems to automatically create native directory
        checkExists(javaBin)

        val libraries = profile[profileLibrariesKey] as List<Map<String, *>>
        val classpath = buildList<String> {
            +gatheringClasspath(libraries, librariesRoot, isCheckFileIntegrity)
            +versionJar.absolutePath
        }.joinToString(File.pathSeparator)

        if (!nativesDirectory.exists()) {
            nativesDirectory.mkdir()
            // up to 1.20 seems unnecessary
        }
        releaseNativeLibraries(libraries, librariesRoot, nativesDirectory)

        val gameArgumentConfiguration = mapOf(
            "auth_player_name" to clientConfig.authPlayerName,
            "version_name" to versionJar.nameWithoutExtension,
            "game_directory" to gameDirectory.absolutePath.wrapDoubleQuote(),
            "assets_root" to assetsRoot.absolutePath.wrapDoubleQuote(),
            "assets_index_name" to profile[profileAssetsKey].toString(),
            "auth_uuid" to clientConfig.authUUID,
            "auth_access_token" to clientConfig.authAccessToken,
            "clientid" to clientConfig.clientId,
            "auth_xuid" to clientConfig.authXXUID,
            "user_type" to clientConfig.userType,
            "version_type" to clientConfig.versionType
        ) + clientConfig.featureGameArguments

        val gameRuleFeatures = clientConfig.clientRuleFeatures

        val loggingConfiguration = mapOf(
            "path" to findLoggingConfigFile(profile).absolutePath.wrapDoubleQuote()
        )

        val jvmArgumentConfiguration = mapOf(
            "classpath" to classpath.wrapDoubleQuote(),
            "launcher_name" to Concatenate.launcherName,
            "natives_directory" to nativesDirectory.absolutePath.wrapDoubleQuote(),
            "launcher_version" to Concatenate.launcherVersion
        )

        val profileJvmArgumentsObject = profile["arguments"]["jvm"] as List<Any?>
        val profileGameArgumentsObject = profile["arguments"]["game"] as List<Any?>
        val profileLoggingArgumentsObject = profile["logging"] as Map<String, *>

        val command = buildList<String> {
            +javaBin.absolutePath
            +mappingJvmArguments(profileJvmArgumentsObject, jvmArgumentConfiguration)
            +mappingExtraJvmArguments(clientConfig.extraJvmArguments)
            if (isEnableMinecraftLogging) {
                +mappingLoggingArguments(profileLoggingArgumentsObject, loggingConfiguration)
            }
            +"-Dminecraft.client.jar=${versionJar.absolutePath.wrapDoubleQuote()}"
            +clientConfig.customJvmArguments
            +profile[profileMainClassKey].toString()
            +mappingGameArguments(profileGameArgumentsObject, gameArgumentConfiguration, gameRuleFeatures)
            +clientConfig.customUserArguments
        }

        logger.debug("final arguments")
        var isReachingAccessToken = false
        command.forEach {

            if (isReachingAccessToken) {
                logger.debug("**********")
                isReachingAccessToken = false
            } else logger.debug(it)

            if (it.trim() == "--accessToken") isReachingAccessToken = true
        }

        val processBuilder = ProcessBuilder(command).directory(workingDirectory)

        return MinecraftClientInstance(
            process = processBuilder.start(),
            processBuilder = processBuilder
        )
    }

    private fun checkExists(file: File) {
         if (!file.exists()) error("file/dir ${file.absolutePath} is not exists")
    }

    private fun findNativeDirectory(): File {
        return File(workingDirectory, listOf("versions", version.versionName, "${version.versionName}-natives").joinToString(File.separator))
    }

    private fun findLoggingConfigFile(profile: Map<String, *>): File {
        return jsonObjectConvGet {
            File(workingDirectory, listOf(
                "versions",
                version.versionName,
                "log4j2.xml" // profile["logging"]["client"]["file"]["id"] as String + ".xml"
            ).joinToString(File.separator))
        }
    }

    private fun findVersionIsolateDirectory(): File {
        return File(workingDirectory, "versions" + File.separator + version.versionName)
    }

    private fun findAssetsDirectory(): File {
        return File(workingDirectory, "assets")
    }

    private fun findAssetIndexJson(assetsIndex: String): File {
        return File(findAssetsDirectory(), listOf("indexes", "$assetsIndex.json").joinToString(File.separator))
    }

    private fun checkSum(file: File, size: Long, sha1: String): Boolean {
        return file.length() == size && sha1 == calculateSHA1(file)
    }

    private fun checkAssetsSum(indexJson: Map<String, *>): Boolean = jsonObjectConvGet {
        val objects = indexJson["objects"] as Map<String, Map<String, *>>

        var isComplete = true
        for (key in objects.keys) {
            val item = objects[key]
            val hash = item["hash"] as String
            val size = item["size"].toString().toLong()

            val assetFile = File(findAssetsDirectory(), listOf("objects", hash.substring(0..1), hash).joinToString(File.separator))
            logger.debug("checking asset: {} at {}", key, assetFile.absolutePath)
            if (size != assetFile.length() || calculateSHA1(assetFile) != hash) {
                isComplete = false
                break
            }
        }

        isComplete
    }

    fun disableLoggingConfiguration() {
        isEnableMinecraftLogging = false
    }

}