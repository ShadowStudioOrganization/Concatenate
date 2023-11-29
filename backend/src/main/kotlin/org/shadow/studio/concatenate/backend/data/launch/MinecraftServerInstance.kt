package org.shadow.studio.concatenate.backend.data.launch

data class MinecraftServerInstance(
    override val process: Process,
    val processBuilder: ProcessBuilder
) : ProgramInstance