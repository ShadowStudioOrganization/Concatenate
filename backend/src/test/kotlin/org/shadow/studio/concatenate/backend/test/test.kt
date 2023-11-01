package org.shadow.studio.concatenate.backend.test

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.shadow.studio.concatenate.backend.util.JsonUtilScope
import org.shadow.studio.concatenate.backend.util.mappingGameArguments
import org.shadow.studio.concatenate.backend.util.mappingJvmArguments

fun main() {

    val json = getResourceAsString("flandrebakapack-1.20.1.json")
    val mapper = jacksonObjectMapper()
    val map: Map<String, Any> = mapper.readValue(json)
//    val k = mappingGameArguments(map, mapOf("auth_player_name" to "whiterasbk", "quickPlayPath" to "1"))
//    println(k.joinToString(" "))
//    val j = mappingJvmArguments(map, mapOf())
//    println(j.joinToString(" "))

    JsonUtilScope.run {
        val anies = map["libraries"] as List<*>
        for (i in  anies) {
            println(i["name"])
        }
    }


}

fun getResourceAsString(path: String): String {
    val inputStream = ::getResourceAsString.javaClass.classLoader.getResourceAsStream(path)

    if (inputStream != null) {
        return inputStream.bufferedReader().use { it.readText() }
    } else {
        error("File not found")
    }
}