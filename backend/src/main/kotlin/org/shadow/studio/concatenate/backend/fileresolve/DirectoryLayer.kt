package org.shadow.studio.concatenate.backend.fileresolve

import org.shadow.studio.concatenate.backend.launch.MinecraftVersion
import java.io.File;

interface DirectoryLayer {

    val workingDirectory: File
    val gameDirectory: File
    val version: MinecraftVersion

    fun getGameVersionDirectory(): File

    fun getJsonProfileContent(): String

    fun getMinecraftJar(): File

    fun getLibrariesRoot(): File

    fun getNativeDirectory(isAutoCreate: Boolean = false): File

    fun getAccessIndexFile(): File

    fun getAccessRoot(): File
}