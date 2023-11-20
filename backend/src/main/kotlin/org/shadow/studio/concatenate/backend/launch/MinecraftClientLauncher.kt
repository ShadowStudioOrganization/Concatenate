package org.shadow.studio.concatenate.backend.launch

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.shadow.studio.concatenate.backend.Concatenate
import org.shadow.studio.concatenate.backend.adapter.JavaAdapter
import org.shadow.studio.concatenate.backend.checksum.MinecraftResourceChecker
import org.shadow.studio.concatenate.backend.data.launch.MinecraftClientInstance
import org.shadow.studio.concatenate.backend.fileresolve.MinecraftResourceResolver
import org.shadow.studio.concatenate.backend.fileresolve.NormalDirectoryLayer
import org.shadow.studio.concatenate.backend.login.LoginMethod
import org.shadow.studio.concatenate.backend.login.OfflineMethod
import org.shadow.studio.concatenate.backend.util.*
import org.slf4j.LoggerFactory
import java.io.File

class MinecraftClientLauncher(
    adapter: JavaAdapter,
    private val clientCfg: MinecraftClientConfiguration,
    override val workingDirectory: File,
    override val version: MinecraftVersion,
    override val environments: Map<String, String> = mapOf(),
    private val loginMethod: LoginMethod = OfflineMethod("Steve"),
    private val isCheckFileIntegrity: Boolean = true,
    private val resolver: MinecraftResourceResolver = MinecraftResourceResolver(NormalDirectoryLayer(workingDirectory, version)),
    private val checker: MinecraftResourceChecker = MinecraftResourceChecker()
) : MinecraftLauncher() {

    // Get the path of Java
    override val program = adapter.getJavaBin(version.versionId)
    private val logger = LoggerFactory.getLogger(MinecraftClientLauncher::class.java)
//    private val librariesDirectoryName = "libraries"
//    private val assetsDirectoryName = "assets"
//    private val profileLibrariesKey = "libraries"
//    private val profileAssetsKey = "assets"
//    private val profileMainClassKey = "mainClass"
    private var isConfiguratorMinecraftLogging = true

    override suspend fun launch(): MinecraftClientInstance = withContext(Dispatchers.IO) {
        val profile = version.profile // parseJson("version.getJsonProfile()")

        // val hooker = BuildingCommandHooker()

        // FIXME: AAAAAAAA
        val versionJar = resolver.resolveGameJar() //version.getJarFile()
//        val versionJarSha1 = profile.downloads.client.sha1 // profile["downloads"]["client"]["sha1"] as String
//        val versionJarSize = profile.downloads.client.size //profile["downloads"]["client"]["size"].toString().toLong()

        if (isCheckFileIntegrity) {
            checker.checkVersionJar(profile, versionJar)
            checker.checkAssetsObjects(resolver.resolveAssetIndexJsonFile(), resolver.resolveAssetObjectsRoot())
            checker.checkClasspath(profile.libraries, resolver.resolveLibrariesRoot())
        }

//        if (isCheckFileIntegrity) {
//            versionJarSha1?.let { sha1 ->
//                if (!checkSum(versionJar, versionJarSize, sha1))
//                    error("version.jar file is damaged")
//            }
//
////            val assetsIndex = profile["assetIndex"]["id"] as String
//            val indexJsonFile = resolver.resolveAssetIndexJsonFile() // findAssetIndexJson(profile.assetIndex.id)
//
//            if (!checkAssetsSum(parseJson(indexJsonFile.readText())))
//                error("assets files not complete")
//        }

//        val librariesRoot = resolver.resolveLibrariesRoot() //File(workingDirectory, librariesDirectoryName)
        val gameDirectory = resolver.resolveGameDirectory() // if (version.isolated) findVersionIsolateDirectory() else workingDirectory
//        val assetsRoot = resolver.resolveAssetRoot()//File(workingDirectory, assetsDirectoryName)
//        val nativesDirectory = findNativeDirectory()
        val javaBin = File(program)

//        checkExists(librariesRoot)
//        checkExists(gameDirectory)
//        checkExists(assetsRoot)
//        checkExists(nativesDirectory) // 1.20 seems to automatically create native directory
        checkExists(javaBin)

//        val libraries = profile.libraries //profile[profileLibrariesKey] as List<Map<String, *>>
//        val classpath = resolver.resolveClasspath(true).joinToString(File.pathSeparator) { it.absolutePath }
        /*buildList<String> {
        +gatheringClasspath(libraries, librariesRoot, isCheckFileIntegrity)
        +versionJar.absolutePath
    }.joinToString(File.pathSeparator)*/

        val nativesDirectory = resolver.resolveNatives()

//        if (!nativesDirectory.exists()) {
//            nativesDirectory.mkdir()
//            // up to 1.20 seems unnecessary
//        }
//        releaseNativeLibraries(libraries, librariesRoot, nativesDirectory)


/*        val gameArgumentConfiguration = mapOf(
            "auth_player_name" to clientCfg.authPlayerName,
            "version_name" to versionJar.nameWithoutExtension,
            "game_directory" to gameDirectory.absolutePath.wrapDoubleQuote(),
            "assets_root" to assetsRoot.absolutePath.wrapDoubleQuote(),
            "assets_index_name" to  profile.assetIndex.id, //profile[profileAssetsKey].toString(),
            "auth_uuid" to clientCfg.authUUID,
            "auth_access_token" to clientCfg.authAccessToken,
            "clientid" to clientCfg.clientId,
            "auth_xuid" to clientCfg.authXXUID,
            "user_type" to clientCfg.userType,
            "version_type" to clientCfg.versionType
        ) + clientCfg.featureGameArguments*/



//        val gameRuleFeatures = clientCfg.clientRuleFeatures

//        val loggingConfiguration = mapOf(
//            "path" to resolver.resolveLoggingConfigurationFile().absolutePath.wrapDoubleQuote() //findLoggingConfigFile().absolutePath.wrapDoubleQuote()
//        )

/*        val jvmArgumentConfiguration = mapOf(
            "classpath" to classpath.wrapDoubleQuote(),
            "launcher_name" to Concatenate.launcherName,
            "natives_directory" to nativesDirectory.absolutePath.wrapDoubleQuote(),
            "launcher_version" to Concatenate.launcherVersion
        )*/

//        val profileJvmArgumentsObject = profile.arguments?.jvm //profile["arguments"]["jvm"] as List<Any?>
//        val profileGameArgumentsObject = profile.arguments?.game //profile["arguments"]["game"] as List<Any?>
//        val profileLoggingArgumentsObject = profile.logging //profile["logging"] as Map<String, *>

        val loginInfo = loginMethod.login()

        val command = buildList<String> {

            add(javaBin.absolutePath.wrapDoubleQuote())

//            hooker.stage = BuildingCommandStage.SetJavaBin

            add(resolver.resolveComplexJvmArguments(buildMap {
                put("classpath", resolver
                    .resolveClasspath(true)
                    .joinToString(File.pathSeparator) { it.absolutePath }
                    .wrapDoubleQuote()
                )
                put("launcher_name", Concatenate.launcherName)
                put("natives_directory", nativesDirectory.absolutePath.wrapDoubleQuote())
                put("launcher_version", Concatenate.launcherVersion)
            }))
//            +mappingComplexMinecraftArguments(profileJvmArgumentsObject, jvmArgumentConfiguration)
//            +mappingExtraJvmArguments(clientConfig.extraJvmArguments)
            add(resolver.resolveExtraJvmArguments(clientCfg.minecraftExtraJvmArguments))

            if (isConfiguratorMinecraftLogging) {
                add(resolver.resolveLoggingArguments(mapOf(
                    "path" to resolver.resolveLoggingConfigurationFile().absolutePath.wrapDoubleQuote()
                )))
//                +mappingLoggingArguments(profileLoggingArgumentsObject, loggingConfiguration)
            }
            add("-Dminecraft.client.jar=" + versionJar.absolutePath.wrapDoubleQuote())
            add(clientCfg.customJvmArguments)
            add(loginMethod.insertJvmArguments())

//            +profile[profileMainClassKey].toString()
            add(profile.mainClass)

            add(resolver.resolveComplexMinecraftArguments(
                buildMap<String, String> {
                    put("auth_player_name", loginInfo.authPlayerName)
                    put("auth_uuid", loginInfo.authUUID)
                    put("auth_xuid", loginInfo.authXUID)
                    put("auth_access_token", loginInfo.authAccessToken)
                    put("version_name", versionJar.nameWithoutExtension)
                    put("assets_root", resolver.resolveAssetRoot().absolutePath.wrapDoubleQuote())
                    put("assets_index_name", profile.assetIndex.id)
                    put("clientid", clientCfg.clientId)
                    put("user_type", clientCfg.userType)
                    put("version_type", clientCfg.versionType)
                    putAll(clientCfg.featureGameArguments)
                },
                clientCfg.clientRuleFeatures
            ))
//            +mappingComplexMinecraftArguments(profileGameArgumentsObject, gameArgumentConfiguration, gameRuleFeatures)
            add(clientCfg.customUserArguments)
        }

        debugCommands(command)

        val processBuilder = ProcessBuilder(command).directory(workingDirectory)

        MinecraftClientInstance(
            process = processBuilder.start(),
            processBuilder = processBuilder
        )
    }

    private fun debugCommands(command: List<String>) {
        logger.debug("final arguments")
        var isReachingAccessToken = false
        command.forEach {
            if (isReachingAccessToken) {
                logger.debug("**********")
                isReachingAccessToken = false
            } else logger.debug(it)

            if (it.trim() == "--accessToken") isReachingAccessToken = true
        }
    }

    private fun checkExists(file: File) {
         if (!file.exists()) error("file/dir ${file.absolutePath} is not exists")
    }

//    private fun findNativeDirectory(): File {
//        return File(workingDirectory, listOf("versions", version.versionName, "${version.versionName}-natives").joinToString(File.separator))
//    }

//    private fun findLoggingConfigFile(): File {
//        return jsonObjectConvGet {
//            File(workingDirectory, listOf(
//                "versions",
//                version.versionName,
//                "log4j2.xml" // profile["logging"]["client"]["file"]["id"] as String + ".xml"
//            ).joinToString(File.separator))
//        }
//    }

//    private fun findVersionIsolateDirectory(): File {
//        return File(workingDirectory, "versions" + File.separator + version.versionName)
//    }

//    private fun findAssetsDirectory(): File {
//        return File(workingDirectory, "assets")
//    }

//    private fun findAssetIndexJson(assetsIndex: String): File {
//        return File(findAssetsDirectory(), listOf("indexes", "$assetsIndex.json").joinToString(File.separator))
//    }




    fun disableLoggingConfiguration() {
        isConfiguratorMinecraftLogging = false
    }

}