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

    override fun launch(): Process {
        val versionJar = version.getJarFile()
        val profile = parseJson(version.getJsonProfile())

        // todo change librariesRootFile
        val classpath = gatheringClasspath(profile["libraries"] as List<Map<String, *>>, File(workingDirectory, "libraries")).let {
            val list = it.toMutableList()
            list += versionJar.absolutePath
            list
        }.joinToString(when(getSystemName()) {
            "windows" -> ";"
            else -> ":"
        })

        val gameArgumentConfiguration = mapOf<String, String>(
            "auth_player_name" to "whiterasbk",
            "version_name" to versionJar.nameWithoutExtension,
            "game_directory" to "D:\\Games\\aloneg\\versions\\flandrebakapack-1.20.1".wrapDoubleQuote(),
            "assets_root" to "D:\\Games\\aloneg\\assets".wrapDoubleQuote(),
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
            "natives_directory" to "D:\\Games\\aloneg\\versions\\flandrebakapack-1.20.1\\flandrebakapack-1.20.1-natives".wrapDoubleQuote(),
            "launcher_version" to "1"
        )

        val jvmMemoryConfiguration = mapOf<String, String>(
            "use_g1gc" to "true "
        )

        JsonUtilScope.run {
            val gameArgList = mappingGameArguments(profile["arguments"]["game"] as List<Any?>, gameArgumentConfiguration, gameRuleFeatures)
            val jvmArgList = mappingJvmArguments(profile["arguments"]["jvm"] as List<Any?>, jvmArgumentConfiguration)
            val jvmMemArgs = mappingJvmMemoryArguments(jvmMemoryConfiguration)

            println(gameArgList.joinToString("\n"))
            println(jvmArgList.joinToString("\n"))
            println(jvmMemArgs.joinToString("\n"))
        }

        return null!!
    }

}