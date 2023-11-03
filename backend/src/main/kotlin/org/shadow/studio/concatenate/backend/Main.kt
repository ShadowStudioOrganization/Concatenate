package org.shadow.studio.concatenate.backend

import org.shadow.studio.concatenate.backend.adapter.*;
import org.shadow.studio.concatenate.backend.launch.*;
import java.io.*

fun main(args: Array<String>) {
    val launcher = MinecraftClientLauncher(
        JavaAdapter(),
        mapOf("" to ""),
        File("D:/Games/aloneg"),
        MinecraftVersion(
            mcVersionID = "1.20",
            gameJar = File("D:/Games/aloneg/versions/1.20/1.20.jar"),
            jsonProfile = File("D:/ProjectFiles/idea/Concatenate/backend/src/test/resources/1.20.json"),
            versionName = "1.20"
        )
    )

    val process = launcher.launch()
    val inputStream = process.inputStream

    val reader = BufferedReader(InputStreamReader(inputStream))
    var line: String?
    while (reader.readLine().also { line = it } != null) {
        println(line)
    }

    val exitCode = process.waitFor()
    println("MC Process exited with code: $exitCode")
    
}