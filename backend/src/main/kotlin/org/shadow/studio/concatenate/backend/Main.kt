package org.shadow.studio.concatenate.backend

import org.shadow.studio.concatenate.backend.adapter.*;
import org.shadow.studio.concatenate.backend.launch.*;
import java.io.*;

fun main(args: Array<String>) {
    val launcher = MinecraftClientLauncher(
        JavaAdapter(),
        mapOf("" to ""),
        File("D:/Games/aloneg"),
        MinecraftVersion(
            mcVersionID = "1",
            gameJar = File("D:/Games/aloneg/versions/flandrebakapack-1.20.1/flandrebakapack-1.20.1.jar"),
            jsonProfile = File("backend/src/test/resources/flandrebakapack-1.20.1.json")
        )
    )
    launcher.launch()
    
}