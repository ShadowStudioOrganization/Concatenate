package org.shadow.studio.concatenate.backend.adapter

import org.shadow.studio.concatenate.backend.launch.MinecraftVersion
import org.shadow.studio.concatenate.backend.util.*


/**
 * this class is use for looking forward java
 * */
class JavaAdapter {
    fun getJavaBin(version: MinecraftVersion): String {
        return version.profile.javaVersion?.majorVersion?.let { javaVersion ->
            when (javaVersion) {
                8 -> ""
            }
            version.profile.javaVersion?.component
            ""
        } ?: when (version) {
            in "rd-132211".."" -> { "D:/Environments/java/8/bin/java.exe" }
            // "1.2.5", "rd-132211", "inf-20100618", "1.12.2" -> "D:/Environments/java/8/bin/java.exe"
            else -> "" //"D:/Environments/java/17/bin/java.exe"
        }
    }
}