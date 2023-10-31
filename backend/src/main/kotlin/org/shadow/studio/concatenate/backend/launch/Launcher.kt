package org.shadow.studio.concatenate.backend.launch

import java.io.File

interface Launcher {

    val program: String

    val environments: Map<String, String>

    val workingDirectory: File

    fun launch(): Process
}