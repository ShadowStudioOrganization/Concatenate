package org.shadow.studio.concatenate.backend

import org.shadow.studio.concatenate.backend.adapter.*
import org.shadow.studio.concatenate.backend.util.globalLogger
import org.shadow.studio.concatenate.backend.launch.MinecraftClientConfig
import org.shadow.studio.concatenate.backend.launch.MinecraftClientLauncher
import org.shadow.studio.concatenate.backend.launch.MinecraftVersion
import java.io.*

fun main() {

    val config = MinecraftClientConfig("whiterasbk", "a"/*UUIDUtils.getFullUUID("whiterasbk")*/, "a")
    val `1_20` = MinecraftVersion(
        versionId = "1.20",
        gameJar = File("D:/Games/aloneg/versions/1.20/1.20.jar"),
        jsonProfile = File("D:/ProjectFiles/idea/Concatenate/backend/src/test/resources/version-profile/1.20.json"),
        versionName = "1.20"
    )

    val `1_17_1` = MinecraftVersion(
        versionId = "1.17.1",
        gameJar = File("D:/Games/aloneg/versions/1.17.1/1.17.1.jar"),
        jsonProfile = File("D:/Games/aloneg/versions/1.17.1/1.17.1.json"),
        versionName = "1.17.1"
    )

    val launcher = MinecraftClientLauncher(
        adapter = JavaAdapter(),
        clientConfig = config,
        workingDirectory = File("D:/Games/aloneg"),
        version = `1_17_1`
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