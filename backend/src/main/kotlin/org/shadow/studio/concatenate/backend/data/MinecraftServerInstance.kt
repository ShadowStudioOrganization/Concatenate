package org.shadow.studio.concatenate.backend.data

data class MinecraftServerInstance(
    val process: Process,
    val processBuilder: ProcessBuilder
) : ProgramInstance