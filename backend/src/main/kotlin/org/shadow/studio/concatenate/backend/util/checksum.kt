package org.shadow.studio.concatenate.backend.util

import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
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
    println(sha1Digest.digest(stream.readBytes()).let {
        StringBuilder().apply {
            it.forEach { byte -> append(String.format("%02x", byte)) }
        }.toString()
    })

    DigestInputStream(stream, sha1Digest).use { dis ->
        val buffer = ByteArray(8192)

        var bytesRead: Int = dis.read(buffer)
        
//        while  {
//            sha1Digest.update(buffer, 0, bytesRead)
//        }

        println(bytesRead)

//        do {
//            println(left)
//            sha1Digest.update(buffer)
//        } while (left != -1)
    }

    val digestBytes = sha1Digest.digest()

    val hexString = StringBuilder()
    for (byte in digestBytes) {
        hexString.append(String.format("%02x", byte))
    }

    return hexString.toString()
}