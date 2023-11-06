package org.shadow.studio.concatenate.backend.util

import io.ktor.utils.io.jvm.javaio.*
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.channels.Channels
import java.security.DigestInputStream
import java.security.MessageDigest

fun checksumUsingSha1(filesWithSha1: Map<String, File>): Map<String, File> {
    return filesWithSha1.filter { (sha1, file) ->
        calculateSHA1(file) == sha1
    }
}

fun calculateSHA1(file: File): String = calculateSHA1(file.inputStream())

fun calculateSHA1(stream: InputStream): String {

    val sha1Digest = MessageDigest.getInstance("SHA-1")
    val buffer = ByteArray(1024 * 10 * 5)

    var length: Int
    while (stream.read(buffer).also { length = it } > 0) {
        sha1Digest.update(buffer, 0, length)
    }

    val digestBytes = sha1Digest.digest()

    val hexString = StringBuilder()
    for (byte in digestBytes) {
        hexString.append(String.format("%02x", byte))
    }

    return hexString.toString()
}