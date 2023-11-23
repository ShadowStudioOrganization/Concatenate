package org.shadow.studio.concatenate.backend.launch

import org.shadow.studio.concatenate.backend.data.launch.ProgramInstance
import java.io.File

interface Launcher {

    val environments: Map<String, String>

    val workingDirectory: File

    suspend fun launch(): ProgramInstance
}