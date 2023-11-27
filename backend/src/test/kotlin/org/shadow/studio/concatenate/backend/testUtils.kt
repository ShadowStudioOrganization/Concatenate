package org.shadow.studio.concatenate.backend

import kotlinx.coroutines.runBlocking
import java.io.File
import java.nio.file.Paths
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime




fun resolveBackendBuildPath(relative: String, isDirAndAutoCreate: Boolean = false): File {
    val userDir = System.getProperty("user.dir")
    val target = if (userDir.endsWith("backend") || userDir.endsWith("backend/"))
        Paths.get(userDir, "build", relative)
    else
        Paths.get(userDir, "backend/build", relative)
    return target.toFile().apply { if (isDirAndAutoCreate && !exists()) mkdirs() }
}

@OptIn(ExperimentalTime::class)
inline fun <R> showRunTime(crossinline block: suspend () -> R): R {
    val ret: R

    measureTime {
        ret = runBlocking {
            block()
        }
    }.apply { println("using time: $this") }

    return ret
}