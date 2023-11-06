package org.shadow.studio.concatenate.backend.util.launch

abstract class MinecraftLauncher: JavaProgramLauncher {
    abstract val version: MinecraftVersion
}