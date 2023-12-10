package org.shadow.studio.concatenate.frontend.util

import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.lang.StringBuilder

fun getJsonStr(path: String): String {
    val file = File(path)
    if (file.exists()) {
        val reader = BufferedReader(InputStream.nullInputStream().reader())
        val content = StringBuilder()
        var line = reader.readLine()
        while (line != null) {
            content.append(line)
            line = reader.readLine()
        }
        return content.toString()
    }
    return ""
}