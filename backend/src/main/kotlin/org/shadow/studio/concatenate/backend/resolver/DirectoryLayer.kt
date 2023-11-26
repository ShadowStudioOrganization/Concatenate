package org.shadow.studio.concatenate.backend.resolver

import org.shadow.studio.concatenate.backend.launch.MinecraftVersion
import java.io.File;

interface DirectoryLayer {

    val workingDirectory: File
    val gameDirectory: File
    val version: MinecraftVersion

    fun getGameVersionDirectory(): File

    fun getMinecraftJar(): File

    fun getLibrariesRoot(): File

    fun getNativeDirectory(isAutoCreate: Boolean = false): File

    fun getAssetIndexFile(): File

    fun getAssetRoot(): File
}