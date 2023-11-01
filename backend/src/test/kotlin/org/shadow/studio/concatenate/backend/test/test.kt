package org.shadow.studio.concatenate.backend.test

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.shadow.studio.concatenate.backend.util.mappingGameArguments

fun main() {

    val json = getResourceAsString("flandrebakapack-1.20.1.json")
    val mapper = jacksonObjectMapper()
    val map: Map<String, Any> = mapper.readValue(json)
    val k = mappingGameArguments(map, mapOf("auth_player_name" to "whiterasbk", "quickPlayPath" to "1"))
    println(k.joinToString(" "))


}

fun getResourceAsString(path: String): String {
    val inputStream = ::getResourceAsString.javaClass.classLoader.getResourceAsStream(path)

    if (inputStream != null) {
        return inputStream.bufferedReader().use { it.readText() }
    } else {
        error("File not found")
    }
}