package org.shadow.studio.concatenate.backend.launch

import org.shadow.studio.concatenate.backend.data.launch.MinecraftServerInstance
import java.io.File

open class MinecraftServerLauncher(
    override val program: String,
    override val environments: Map<String, String>,
    override val workingDirectory: File,
    override val version: MinecraftVersion
) : MinecraftLauncher() {
    override suspend fun launch(): MinecraftServerInstance {
        TODO("Not yet implemented")
    }

}