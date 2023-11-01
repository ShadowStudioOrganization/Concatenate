package org.shadow.studio.concatenate.backend.util

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.forEach
import kotlin.collections.getOrNull
import kotlin.collections.getValue
import kotlin.collections.mapOf
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.plusAssign
import kotlin.collections.set

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

fun parseJson(json: String): Map<String, Any> {
    val mapper = jacksonObjectMapper()
    return mapper.readValue(json)
}

fun parseGameArguments(versionJson: Map<String, Any>): Map<String, String> {
    return JsonUtilScope.run {
        val gameArgument = mutableMapOf<String, String>()
        var cacheValue = ""
        for (item in versionJson["arguments"]["game"] as List<Any?>) {
            if (item is String) {
                if (!item.startsWith("\${")) cacheValue = item else gameArgument[item] = cacheValue
            } else {
                // rules
            }
        }
        gameArgument
    }
}

fun mappingGameArguments(versionJson: Map<String, Any>, config: Map<String, String>, rulesOverrides: Map<String, Boolean> = mapOf()): List<String> {
    return JsonUtilScope.run {
        val gameArguments = mutableListOf<String>()
        val json = versionJson["arguments"]["game"] as List<Any?>
        for (item in json) {
            val newItem: String = if (item is String) {
                if (item.startsWith("\${") && item.endsWith("}")) {
                    val key = item.removePrefix("\${").removeSuffix("}")
                    config[key] ?: ""
                    // error handling here
                } else item
            } else {
                // rules
                val rules = item["rules"] as List<*>
                var judgeFlag = 0
                rules.forEach { ruleTest ->
                    when (ruleTest["action"]) {
                        "allow" -> {
                            val features = ruleTest["features"]
                            if (rulesOverrides[features]) {

                            } else if (features[""])
                        }
                    }
                }

                val values = item["values"]
                println(item?.javaClass)
                ""
            }

            gameArguments += newItem
        }
        gameArguments
    }
}