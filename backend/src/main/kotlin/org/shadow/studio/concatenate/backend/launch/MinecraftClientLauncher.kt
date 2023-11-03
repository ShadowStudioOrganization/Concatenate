package org.shadow.studio.concatenate.backend.launch

import org.shadow.studio.concatenate.backend.adapter.JavaAdapter
import org.shadow.studio.concatenate.backend.util.*
import java.io.File

class MinecraftClientLauncher(
    private val adapter: JavaAdapter,
    override val environments: Map<String, String>,
    override val workingDirectory: File,
    override val version: MinecraftVersion,
) : MinecraftLauncher() {

    // Get the path of Java
    override val program = adapter.getJavaPath(version.mcVersionID)

    // Get the path of Root
    val rootPath = RootPathUtils.getRootPath(workingDirectory)

    val isVersionDivided = true;

    private fun checkExists(file: File) {
        if (!file.exists()) error("file/dir ${file.absolutePath} is not exists")
    }

    override fun launch(): Process {
        val versionJar = version.getJarFile()
        val profile = parseJson(version.getJsonProfile())

        val librariesRoot = File(workingDirectory, "libraries")
        val gameDirectory = File(rootPath)
        val assetsRoot = File(rootPath, "assets")
        val nativesDirectory = File(workingDirectory,
            listOf("versions", version.versionName, "${version.versionName}-natives").joinToString(File.separator)
        )
        val javaBin = File(program)

        checkExists(librariesRoot)
        checkExists(gameDirectory)
        checkExists(assetsRoot)
        checkExists(nativesDirectory)
        checkExists(javaBin)

        val classpath = gatheringClasspath(profile["libraries"] as List<Map<String, *>>, librariesRoot).let {
            val list = it.toMutableList()
            list += versionJar.absolutePath
            list
        }.joinToString(File.pathSeparator)

        val gameArgumentConfiguration = mapOf<String, String>(
            "auth_player_name" to "whiterasbk",
            "version_name" to versionJar.nameWithoutExtension,
            "game_directory" to gameDirectory.absolutePath.wrapDoubleQuote(),
            "assets_root" to assetsRoot.absolutePath.wrapDoubleQuote(),
            "assets_index_name" to profile["assets"].toString(),
            "auth_uuid" to "114514",
            "auth_access_token" to "114514",
            "clientid" to "a",
            "auth_xuid" to "a",
            "user_type" to "am",
            "version_type" to "am",
        )
        val gameRuleFeatures = mapOf<String, Boolean>(

        )

        val jvmArgumentConfiguration = mapOf<String, String>(
            "classpath" to classpath.wrapDoubleQuote(),
            "launcher_name" to "Concatenate",
            "natives_directory" to nativesDirectory.absolutePath.wrapDoubleQuote(),
            "launcher_version" to "1"
        )

        val jvmMemoryConfiguration = mapOf<String, String>(
            "use_g1gc" to "true"
        )


        val command = jsonObjectConvGet {
            buildList<String> {
                +javaBin.absolutePath
                +mappingJvmMemoryArguments(jvmMemoryConfiguration)
                +"-Xmx4412m"
                +"-Dfile.encoding=GB18030"
                +"-Dsun.stdout.encoding=GB18030"
                +"-Dsun.stderr.encoding=GB18030"
                +"-Djava.rmi.server.useCodebaseOnly=true"
                +"-Dcom.sun.jndi.rmi.object.trustURLCodebase=false"
                +"-Dcom.sun.jndi.cosnaming.object.trustURLCodebase=false"
                +mappingJvmArguments(profile["arguments"]["jvm"] as List<Any?>, jvmArgumentConfiguration)
                +profile["mainClass"].toString()
                +mappingGameArguments(profile["arguments"]["game"] as List<Any?>, gameArgumentConfiguration, gameRuleFeatures)
            }
        }

        val processBuilder = ProcessBuilder(command).directory(workingDirectory)
        return processBuilder.start()
    }

}