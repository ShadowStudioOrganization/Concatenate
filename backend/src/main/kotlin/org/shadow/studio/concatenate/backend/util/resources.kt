package org.shadow.studio.concatenate.backend.util

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.io.File
import java.io.InputStream
import java.nio.charset.Charset


fun getAssetObjectsFromStream(indexJson: InputStream): JsonNode {
    return jacksonObjectMapper().readTree(indexJson).let { rootNode ->
        if (!rootNode.isObject) error("not an object")
        rootNode.get("objects")
    }
}

fun getAssetObjectsFromString(indexJson: String): JsonNode {
    return jacksonObjectMapper().readTree(indexJson).let { rootNode ->
        if (!rootNode.isObject) error("not an object")
        rootNode.get("objects")
    }
}

fun getAssetObjectsFromFile(indexJsonFile: File): JsonNode = getAssetObjectsFromStream(indexJsonFile.inputStream())

fun getResourceAsStream(path: String): InputStream? {
    return Thread.currentThread().contextClassLoader.getResourceAsStream(path)
}

fun getResourceAsBytes(path: String) = getResourceAsStream(path)?.use { it.readBytes() }

fun getResourceAsString(path: String, charset: Charset = Charsets.UTF_8)
    = getResourceAsStream(path)?.bufferedReader(charset)?.use { it.readText() }