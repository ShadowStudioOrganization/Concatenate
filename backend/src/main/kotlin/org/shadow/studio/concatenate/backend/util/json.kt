package org.shadow.studio.concatenate.backend.util

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

class JsonUtilScope {
    companion object {
        operator fun Any?.get(index: Int): Any? {
            return if (this is List<*>) this.getOrNull(index) else error("not a List<Any>")
        }

        operator fun Any?.get(key: String): Any? {
            return if (this is Map<*, *>) {
                val map: Map<String, Any?> = this as Map<String, Any?>
                map.getValue(key)
            } else error("not a Map<String, Any?>")
        }
    }
}

fun <T> Map<*,*>.havingKey(key: String, action: (T) -> Unit) {
    if (containsKey(key)) {
        (this[key] as? T)?.let {
            action(it)
        }
    }
}

fun parseJson(json: String): Map<String, Any?> {
    val mapper = jacksonObjectMapper()
    return mapper.readValue(json)
}



