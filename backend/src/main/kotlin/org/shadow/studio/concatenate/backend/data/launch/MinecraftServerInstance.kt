package org.shadow.studio.concatenate.backend.data.launch

data class MinecraftServerInstance(
    val process: Process,
    val processBuilder: ProcessBuilder
) : ProgramInstance