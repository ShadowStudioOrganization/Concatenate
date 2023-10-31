package org.shadow.studio.concatenate.backend.launch

abstract class MinecraftLauncher: JavaProgramLauncher {
    abstract val version: MinecraftVersion
}