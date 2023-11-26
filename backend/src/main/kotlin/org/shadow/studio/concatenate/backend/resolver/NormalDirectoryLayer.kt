package org.shadow.studio.concatenate.backend.resolver

import java.io.File

class NormalDirectoryLayer(
    override val workingDirectory: File,
    override val versionIsIsolated: Boolean,
    override val versionName: String
): DirectoryLayer {

    override val gameDirectory: File
        get() = if (versionIsIsolated)
            File(getGameVersionDirectory(), versionName)
        else workingDirectory

    override fun getGameVersionDirectory(): File {
        return File(workingDirectory, "versions")
    }

    override fun getMinecraftJarPosition(): File {
        return File(getGameVersionDirectory(),  versionName + File.separator + "$versionName.jar")
    }

    override fun getMinecraftJsonProfilePosition(): File {
        return File(getGameVersionDirectory(), versionName + File.separator + "$versionName.json")
    }

    override fun getLibrariesRoot(): File {
        return File(workingDirectory, "libraries")
    }

    override fun getNativeDirectoryPosition(isAutoCreate: Boolean): File {
        val file = File(workingDirectory,
            listOf("versions",
                versionName,
                "${versionName}-natives")
                .joinToString(File.separator))
        if (isAutoCreate && !file.exists()) file.mkdir()
        return file
    }

    override fun getAssetRoot(): File = File(workingDirectory, "assets")
}