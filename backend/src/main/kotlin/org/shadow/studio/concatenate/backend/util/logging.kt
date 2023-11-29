package org.shadow.studio.concatenate.backend.util

import org.shadow.studio.concatenate.backend.data.launch.ProgramInstance
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.concurrent.thread

internal val globalLogger = LoggerFactory.getLogger("Global")

fun ProgramInstance.handleProcessOutput() {

    thread(isDaemon = true, name = "MinecraftOutput") {
        val reader = BufferedReader(InputStreamReader(process.inputStream))
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            println(line)
        }
    }

    thread(isDaemon = true, name = "MinecraftError") {
        val errReader = BufferedReader(InputStreamReader(process.errorStream))
        var errLine: String?
        while (errReader.readLine().also { errLine = it } != null) {
            println(errLine)
        }
    }
}