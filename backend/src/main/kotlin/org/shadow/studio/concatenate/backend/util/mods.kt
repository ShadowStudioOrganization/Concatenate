package org.shadow.studio.concatenate.backend.util

import java.io.File

fun getModsList(workingDirectory: File): MutableMap<String, Boolean> {
    val modsFile: File = File(workingDirectory.path + File.separator + "mods")
    val resList: MutableMap<String, Boolean> = mutableMapOf("modsCounter" to true)
    for (mod in modsFile.list().filter { it.endsWith(".jar") }) {
        resList[mod] = true
    }
    return resList
}