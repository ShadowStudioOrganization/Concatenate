package org.shadow.studio.concatenate.backend.data.launch

data class MinecraftClientInstance(
    override val process: Process,
    val processBuilder: ProcessBuilder
) : ProgramInstance
