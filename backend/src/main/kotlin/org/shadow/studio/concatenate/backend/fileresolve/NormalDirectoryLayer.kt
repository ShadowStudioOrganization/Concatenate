package org.shadow.studio.concatenate.backend.fileresolve

import java.io.File
import org.shadow.studio.concatenate.backend.launch.MinecraftVersion
class NormalDirectoryLayer(
    override val workingDirectory: File,
    override val version: MinecraftVersion
): DirectoryLayer {
    override val gameDirectory: File
        get() = if (version.isolated)
            File(getGameVersionDirectory(), version.versionName)
        else workingDirectory

    override fun getJsonProfileContent(): String {
        return ""// version.getJsonProfile()
    }

    override fun getGameVersionDirectory(): File {
        return File(workingDirectory, "versions")
    }

    override fun getMinecraftJar(): File {
        return File(getGameVersionDirectory(), version.versionName + ".jar")
    }

    override fun getLibrariesRoot(): File {
        return File(workingDirectory, "libraries")
    }

    override fun getNativeDirectory(isAutoCreate: Boolean): File {
        val file = File(workingDirectory,
            listOf("versions",
                version.versionName,
                "${version.versionName}-natives")
                .joinToString(File.separator))
        if (isAutoCreate && !file.exists()) file.mkdir()
        return file
    }

    override fun getAccessIndexFile(): File {
        return File(getAccessRoot(), listOf("indexes", "${version.getAccessIndex()}.json").joinToString(File.separator))
    }

    override fun getAccessRoot(): File = File(workingDirectory, "assets")
}