package org.shadow.studio.concatenate.backend.launch

import java.io.File

open class MinecraftServerLauncher(
    override val program: String,
    override val environments: Map<String, String>,
    override val workingDirectory: File,
    override val version: MinecraftVersion
) : MinecraftLauncher() {
    override fun launch(): Process {
        TODO("Not yet implemented")
    }

}