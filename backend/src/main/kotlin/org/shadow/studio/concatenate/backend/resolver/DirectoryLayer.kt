package org.shadow.studio.concatenate.backend.resolver

import org.shadow.studio.concatenate.backend.launch.MinecraftVersion
import java.io.File;


interface DirectoryLayer {

    val versionIsIsolated: Boolean
    val versionName: String

    /**
     * .minecraft/
     */
    val workingDirectory: File

    /**
     * .minecraft/ or .minecraft/versions/version-name/
     */
    val gameDirectory: File

    /**
     * .minecraft/versions/
     */
    fun getGameVersionDirectory(): File

    /**
     * .minecraft/versions/version-name/version-name.jar
     */
    fun getMinecraftJarPosition(): File

    /**
     * .minecraft/versions/version-name/version-name.json
     */
    fun getMinecraftJsonProfilePosition(): File

    /**
     * .minecraft/libraries
     */
    fun getLibrariesRoot(): File

    /**
     * .minecraft/versions/version-name/version-name-natives
     */
    fun getNativeDirectoryPosition(isAutoCreate: Boolean = false): File

    /**
     * .minecraft/assets
     */
    fun getAssetRoot(): File

    fun newMinecraftVersion(): MinecraftVersion {
        val profile = getMinecraftJsonProfilePosition()
        if (!profile.exists())
            error("'$profile' which is minecraft json profile is not exist!")
        return MinecraftVersion(versionName, profile, getMinecraftJarPosition(), versionIsIsolated)
    }
}