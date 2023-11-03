package org.shadow.studio.concatenate.backend.launch

import org.shadow.studio.concatenate.backend.Concatenate
import org.shadow.studio.concatenate.backend.adapter.JavaAdapter
import org.shadow.studio.concatenate.backend.data.MinecraftClientInstance
import org.shadow.studio.concatenate.backend.util.*
import org.shadow.studio.concatenate.backend.util.JsonObjectScope.get
import java.io.File

class MinecraftClientLauncher(
    adapter: JavaAdapter,
    private val clientConfig: MinecraftClientConfig,
    override val environments: Map<String, String>,
    override val workingDirectory: File,
    override val version: MinecraftVersion,
) : MinecraftLauncher() {

    // Get the path of Java
    override val program = adapter.getJavaBin(version.mcVersionID)

    private fun checkExists(file: File) {
        if (!file.exists()) error("file/dir ${file.absolutePath} is not exists")
    }

    private val librariesDirectoryName = "libraries"
    private val assetsDirectoryName = "assets"
    private val profileLibrariesKey = "libraries"
    private val profileAssetsKey = "assets"
    private val profileMainClassKey = "mainClass"

    private fun findNativeDirectory(): File {
        return File(workingDirectory, listOf("versions", version.versionName, "${version.versionName}-natives").joinToString(File.separator))
    }

    private fun findVersionIsolateDirectory(): File {
        return File(workingDirectory, "versions" + File.separator + version.versionName)
    }

    override fun launch(): MinecraftClientInstance {
        val versionJar = version.getJarFile()
        val profile = parseJson(version.getJsonProfile())

        val librariesRoot = File(workingDirectory, librariesDirectoryName)
        val gameDirectory = if (version.isolated) findVersionIsolateDirectory() else workingDirectory
        val assetsRoot = File(workingDirectory, assetsDirectoryName)
        val nativesDirectory = findNativeDirectory()
        val javaBin = File(program)

        checkExists(librariesRoot)
        checkExists(gameDirectory)
        checkExists(assetsRoot)
        checkExists(nativesDirectory)
        checkExists(javaBin)

        val classpath = buildList<String> {
            +gatheringClasspath(profile[profileLibrariesKey] as List<Map<String, *>>, librariesRoot)
            +versionJar.absolutePath
        }.joinToString(File.pathSeparator)

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

        val jvmArgumentConfiguration = mapOf(
            "classpath" to classpath.wrapDoubleQuote(),
            "launcher_name" to Concatenate.launcherName,
            "natives_directory" to nativesDirectory.absolutePath.wrapDoubleQuote(),
            "launcher_version" to Concatenate.launcherVersion
        )

        // todo log

        val profileJvmArgumentsObject = profile["arguments"]["jvm"] as List<Any?>
        val profileGameArgumentsObject = profile["arguments"]["game"] as List<Any?>

        val command = jsonObjectConvGet {
            buildList<String> {
                +javaBin.absolutePath
                +mappingExtraJvmArguments(clientConfig.extraJvmArguments)
                +"-Dfile.encoding=GB18030"
                +"-Dsun.stdout.encoding=GB18030"
                +"-Dsun.stderr.encoding=GB18030"
                +"-Djava.rmi.server.useCodebaseOnly=true"
                +"-Dcom.sun.jndi.rmi.object.trustURLCodebase=false"
                +"-Dcom.sun.jndi.cosnaming.object.trustURLCodebase=false"
                +mappingJvmArguments(profileJvmArgumentsObject, jvmArgumentConfiguration)
                +profile[profileMainClassKey].toString()
                +mappingGameArguments(profileGameArgumentsObject, gameArgumentConfiguration, gameRuleFeatures)
            }
        }

        val processBuilder = ProcessBuilder(command).directory(workingDirectory)

        return MinecraftClientInstance(
            process = processBuilder.start(),
            processBuilder = processBuilder
        )
    }

}