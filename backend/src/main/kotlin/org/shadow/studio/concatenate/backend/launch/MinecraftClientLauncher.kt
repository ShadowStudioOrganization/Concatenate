package org.shadow.studio.concatenate.backend.launch

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.shadow.studio.concatenate.backend.adapter.JavaAdapter
import org.shadow.studio.concatenate.backend.data.launchscript.LaunchJson
import org.shadow.studio.concatenate.backend.util.JsonUtilScope
import java.io.File

open class MinecraftClientLauncher(
    private val adapter: JavaAdapter,
    override val environments: Map<String, String>,
    override val workingDirectory: File,
    override val version: MinecraftVersion
) : MinecraftLauncher() {

    // Get the path of Java
    val gameVersion = version.getVersionString()
    override val program = adapter.getJavaPath(gameVersion)

    private fun getLaunchJson(): LaunchJson {
        val json = "{a:1}"
        val mapper = jacksonObjectMapper()
        val map: Map<String, Any> = mapper.readValue(json)


        TODO()
    }

    override fun launch(): Process {
        val process: Process? = null
        val launchJson = mapOf<String, Any?>() // getLaunchJson()
        val quickPlayPath = ""
        val Xmn = 256
        val Xmx = 4096
        val height = 480
        val width = 854

        JsonUtilScope.run {

            // sample
            launchJson["arguments"]["game"][3]

            val cmd = """
            @echo off 
            title 启动 - ${launchJson["arguments"]["game"][3]}
            echo 游戏正在启动,请稍候...
            set APPDATA=" ${launchJson.arguments.game[5]} "
            cd /D ${launchJson.arguments.game[5]} "
            " $program \java.exe" ^
            -XX:+UseG1GC -XX:-UseAdaptiveSizePolicy ^
            -XX:-OmitStackTraceInFastThrow ^
            -Dfml.ignoreInvalidMinecraftCertificates=True ^
            -Dfml.ignorePatchDiscrepancies=True ^
            -Dlog4j2.formatMsgNoLookups=true ^
            -XX:HeapDumpPath=MojangTricksIntelDriversForPerformance_javaw.exe_minecraft.exe.heapdump ^
            ${launchJson.arguments.jvm[3]} ^
            ${launchJson.arguments.jvm[4]} ^
            ${launchJson.arguments.jvm[5]} ^
            ${launchJson.arguments.jvm[6]} ^
            ${launchJson.arguments.jvm[7]} ^
            ${launchJson.arguments.jvm[8]} ^
            ${launchJson.arguments.jvm[9]} ^
            ${launchJson.arguments.jvm[10]} ^
            -Xmn${Xmn}m ^
            -Xmx${Xmx}m ^
            -Dlog4j2.formatMsgNoLookups=true ^
            --add-exports cpw.mods.bootstraplauncher/cpw.mods.bootstraplauncher=ALL-UNNAMED ^
            -Doolloo.jlw.tmpdir="C:\Users\whiter\AppData\Roaming\PCL" ^
            -jar "C:\Users\whiter\AppData\Roaming\PCL\JavaWrapper.jar" net.fabricmc.loader.impl.launch.knot.KnotClient ^
            --username ${launchJson.arguments.game[1]} ^
            --version ${launchJson.arguments.game[3]} ^
            --gameDir " ${launchJson.arguments.game[5]} " ^
            --assetsDir " ${launchJson.arguments.game[7]} " ^
            --assetIndex ${launchJson.arguments.game[9]} ^
            --uuid ${launchJson.arguments.game[11]} ^
            --accessToken ${launchJson.arguments.game[13]} ^
            --clientId ${launchJson.arguments.game[15]}
            --xuid ${launchJson.arguments.game[17]} ^
            --userType ${launchJson.arguments.game[19]} ^zh
            --versionType ${launchJson.arguments.game[21]} ^
            --width $width ^
            --height $height ^
            --quickPlayPath $quickPlayPath
            echo 游戏已退出.
            pause
        """
        }




        return process!!
    }

}