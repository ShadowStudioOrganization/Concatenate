package org.shadow.studio.concatenate.backend.util

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue


fun parseJsonToMap(json: String): Map<String, Any?> {
    val mapper = jacksonObjectMapper()
    return mapper.readValue(json)
}

fun parseJson(json: String): JsonNode {
    return jacksonObjectMapper().readTree(json)
}

fun parseJsonStr(mapper: Map<String, Any?>): String {
    val jsonStr = ObjectMapper()
    return jsonStr.writeValueAsString(mapper)
}




