package org.shadow.studio.concatenate.backend.launch

import java.io.File

class MinecraftVersion(
    val mcVersionID: String,
    private val jsonProfile: File,
    private val gameJar: File,
    val customName: String? = null
) {
    fun getJarFile(): File {
        return gameJar
    }

    fun getJsonProfile(): String {
        return jsonProfile.readText()
    }
}