package org.shadow.studio.concatenate.backend

import org.shadow.studio.concatenate.backend.adapter.*;
import org.shadow.studio.concatenate.backend.launch.*;
import java.io.*;

fun main(args: Array<String>) {
    val launcher = MinecraftClientLauncher(JavaAdapter(), mapOf("" to ""), File(""), MinecraftVersion())
    launcher.launch()
    // 好了外部就是这样调用,彳亍，push一下？wait
    
}