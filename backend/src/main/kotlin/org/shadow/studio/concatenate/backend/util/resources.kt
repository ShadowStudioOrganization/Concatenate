package org.shadow.studio.concatenate.backend.util

import java.io.InputStream
import java.nio.charset.Charset

fun getResourceAsStream(path: String): InputStream? {
    return Thread.currentThread().contextClassLoader.getResourceAsStream(path)
}

fun getResourceAsBytes(path: String) = getResourceAsStream(path)?.use { it.readBytes() }

fun getResourceAsString(path: String, charset: Charset = Charsets.UTF_8)
    = getResourceAsStream(path)?.bufferedReader(charset)?.use { it.readText() }