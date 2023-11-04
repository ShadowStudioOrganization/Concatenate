package org.shadow.studio.concatenate.backend.launch

import java.io.File

class MinecraftVersion(
    val versionId: String,
    private val jsonProfile: File,
    private val gameJar: File,
    val versionName: String,
    val isolated: Boolean = false
) {
    fun getJarFile(): File {
        return gameJar
    }

    fun getJsonProfile(): String {
        return jsonProfile.readText()
    }
}