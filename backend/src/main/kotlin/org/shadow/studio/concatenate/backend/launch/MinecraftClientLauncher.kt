package org.shadow.studio.concatenate.backend.launch

import java.io.File

open class MinecraftClientLauncher(
    override val program: String,
    override val environments: Map<String, String>,
    override val workingDirectory: File
) : MinecraftLauncher() {
    override fun launch() {
        TODO("Not yet implemented")
    }

}