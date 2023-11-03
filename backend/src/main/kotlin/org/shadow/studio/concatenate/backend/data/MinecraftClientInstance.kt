package org.shadow.studio.concatenate.backend.data

data class MinecraftClientInstance(
    val process: Process,
    val processBuilder: ProcessBuilder
) : ProgramInstance
