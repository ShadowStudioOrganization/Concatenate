package org.shadow.studio.concatenate.backend.data.launch

data class MinecraftClientInstance(
    val process: Process,
    val processBuilder: ProcessBuilder
) : ProgramInstance
