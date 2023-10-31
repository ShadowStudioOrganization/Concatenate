package org.shadow.studio.concatenate.backend.launch

import org.shadow.studio.concatenate.backend.adapter.JavaAdapter
import java.io.File

open class MinecraftClientLauncher(
    private val adapter: JavaAdapter,
    override val environments: Map<String, String>,
    override val workingDirectory: File
) : MinecraftLauncher() {

    override val program = adapter.getJavaPath("")

    override fun launch(): Process {



        TODO("Not yet implemented")
    }

}