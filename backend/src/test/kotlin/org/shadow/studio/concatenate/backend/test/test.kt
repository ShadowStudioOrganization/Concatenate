package org.shadow.studio.concatenate.backend.test

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.shadow.studio.concatenate.backend.test.JsonUtilScope.Companion.get
import java.util.ArrayList

fun main() {

    val json = getResourceAsString("flandrebakapack-1.20.1.json")
    val mapper = jacksonObjectMapper()
    val map: Map<String, Any> = mapper.readValue(json)
    println(map["arguments"]["game"][25])


//    val gameArg = mapOf<String, String>()
//    var cacheKey = ""
//    for (item in json["arguments"]["game"]) {
//        if (item is String) {
//            if (!item.startsWith("\${")) {
//                cacheKey = item
//            } else gameArg[cacheKey] = item
//        } else if (item is Any) {
//            /// 根据rule生成 key value pair
//        }
//    }

    JsonUtilScope.run {
        map["arguments"]["game"]
    }

}

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



private operator fun Any?.get(key: String): Any? {
    return if (this is Map<*, *>) {
        val map: Map<String, Any?> = this as Map<String, Any?>
        map.getValue(key)
    } else error("not a Map<String, Any?>")
}

fun getResourceAsString(path: String): String {
    val inputStream = ::getResourceAsString.javaClass.classLoader.getResourceAsStream(path)

    if (inputStream != null) {
        return inputStream.bufferedReader().use { it.readText() }
    } else {
        error("File not found")
    }
}