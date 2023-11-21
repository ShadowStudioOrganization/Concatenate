package org.shadow.studio.concatenate.backend

import kotlinx.coroutines.runBlocking
import org.shadow.studio.concatenate.backend.adapter.*
import org.shadow.studio.concatenate.backend.util.globalLogger
import org.shadow.studio.concatenate.backend.launch.MinecraftClientConfiguration
import org.shadow.studio.concatenate.backend.launch.MinecraftClientLauncher
import org.shadow.studio.concatenate.backend.launch.MinecraftVersion
import org.shadow.studio.concatenate.backend.login.OfflineMethod
import java.io.*

fun main() = runBlocking {

    val config = MinecraftClientConfiguration()
    val `1_20` = MinecraftVersion(
        versionName = "1.20",
        gameJar = File("D:/Games/aloneg/versions/1.20/1.20.jar"),
        jsonProfile = File("D:/ProjectFiles/idea/Concatenate/backend/src/test/resources/version-profile/1.20.json"),
    )

    val `1_17_1` = MinecraftVersion(
        gameJar = File("D:/Games/aloneg/versions/1.17.1/1.17.1.jar"),
        jsonProfile = File("D:/Games/aloneg/versions/1.17.1/1.17.1.json"),
        versionName = "1.17.1"
    )

    val `1_7_10` = MinecraftVersion(
        gameJar = File("D:/Games/aloneg/versions/1.7.10/1.7.10.jar"),
        jsonProfile = File("D:/Games/aloneg/versions/1.7.10/1.7.10.json"),
        versionName = "1.7.10"
    )

    val `1_14_1` = MinecraftVersion(
        gameJar = File("D:/Games/aloneg/versions/1.14.1/1.14.1.jar"),
        jsonProfile = File("D:/Games/aloneg/versions/1.14.1/1.14.1.json"),
        versionName = "1.14.1"
    )

    val `1_12_2_forge` = MinecraftVersion(
        gameJar = File("D:/Games/aloneg/versions/1.12.2-Forge_14.23.5.2859/1.12.2-Forge_14.23.5.2859.jar"),
        jsonProfile = File("D:/Games/aloneg/versions/1.12.2-Forge_14.23.5.2859/1.12.2-Forge_14.23.5.2859.json"),
        versionName = "1.12.2-Forge_14.23.5.2859"
    )

    val `1_12_2` = MinecraftVersion(
        gameJar = File("D:/Games/aloneg/versions/1.12.2/1.12.2.jar"),
        jsonProfile = File("D:/Games/aloneg/versions/1.12.2/1.12.2.json"),
        versionName = "1.12.2"
    )

    val stoneblock3 = MinecraftVersion(
        gameJar = File("D:/Games/aloneg/versions/FTB StoneBlock 3 1.6.1/FTB StoneBlock 3 1.6.1.jar"),
        jsonProfile = File("D:/Games/aloneg/versions/FTB StoneBlock 3 1.6.1/FTB StoneBlock 3 1.6.1.json"),
        versionName = "FTB StoneBlock 3 1.6.1"
    )

    val `1_2_5` = MinecraftVersion(
        gameJar = File("D:/Games/aloneg/versions/1.2.5/1.2.5.jar"),
        jsonProfile = File("D:/Games/aloneg/versions/1.2.5/1.2.5.json"),
        versionName = "1.2.5"
    )

    val rd132211 = MinecraftVersion(
        gameJar = File("D:/Games/aloneg/versions/rd-132211/rd-132211.jar"),
        jsonProfile = File("D:/Games/aloneg/versions/rd-132211/rd-132211.json"),
        versionName = "rd-132211"
    )

    val inf20100618 = MinecraftVersion(
        gameJar = File("D:/Games/aloneg/versions/inf-20100618/inf-20100618.jar"),
        jsonProfile = File("D:/Games/aloneg/versions/inf-20100618/inf-20100618.json"),
        versionName = "inf-20100618"
    )

    val launcher = MinecraftClientLauncher(
        adapter = JavaAdapter(),
        clientCfg = config,
        workingDirectory = File("D:/Games/aloneg"),
        version = `1_12_2`,
        loginMethod = OfflineMethod("whiterasbk")
    )

    val logger = globalLogger

    launcher.disableLoggingConfiguration()

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