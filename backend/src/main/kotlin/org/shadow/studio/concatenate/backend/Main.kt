package org.shadow.studio.concatenate.backend

import kotlinx.coroutines.runBlocking
import org.shadow.studio.concatenate.backend.adapter.*
import org.shadow.studio.concatenate.backend.data.launch.MinecraftExtraJvmArguments
import org.shadow.studio.concatenate.backend.util.globalLogger
import org.shadow.studio.concatenate.backend.launch.MinecraftClientConfiguration
import org.shadow.studio.concatenate.backend.launch.MinecraftClientLauncher
import org.shadow.studio.concatenate.backend.launch.MinecraftVersion
import org.shadow.studio.concatenate.backend.login.OfflineMethod
import org.shadow.studio.concatenate.backend.resolver.NormalDirectoryLayer
import java.io.*

fun main() = runBlocking {

    val config = MinecraftClientConfiguration(
        minecraftExtraJvmArguments = MinecraftExtraJvmArguments(
            fileEncoding = "UTF-8",
            initialJavaHeapSize = "1G",
            maximumJavaHeapSize = "4G"
        )
    ).apply {
        customJvmArguments {
            add("-XX:+UseConcMarkSweepGC")
            add("-XX:+CMSIncrementalMode")
        }

        preferJavaVersion = 8
    }

    val `1_12_2_forge` = "1.12.2-Forge_14.23.5.2859"
    val stoneblock3 = "FTB StoneBlock 3 1.6.1"
    val rd132211 = "rd-132211"
    val inf20100618 = "inf-20100618"

    val wd1 = File("D:/Games/aloneg")
    val wd2 = File("D:/ProjectFiles/idea/Concatenate/backend/build/run")

    val layer = NormalDirectoryLayer(wd1, false, "1.0")


    val launcher = MinecraftClientLauncher(
        adapter = JavaAdapter(),
        clientCfg = config,
        workingDirectory = wd1,
        version = layer.newMinecraftVersion(),
        loginMethod = OfflineMethod("whiterasbk"),
        isCheckFileIntegrity = true
    )

    val logger = globalLogger

    logger.info("Minecraft instance is starting")
    val instance = launcher.launch()
    val inputStream = instance.process.inputStream

    val reader = BufferedReader(InputStreamReader(inputStream))
    var line: String?
    while (reader.readLine().also { line = it } != null) {
        println(line)
    }

    val errorStream = instance.process.errorStream

    val errReader = BufferedReader(InputStreamReader(errorStream))
    var errLine: String?
    while (errReader.readLine().also { errLine = it } != null) {
        println(errLine)
    }

    val exitCode = instance.process.waitFor()
    logger.info("Minecraft process exited with code: $exitCode")
    
}