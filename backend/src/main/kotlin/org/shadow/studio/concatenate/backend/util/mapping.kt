package org.shadow.studio.concatenate.backend.util

import org.shadow.studio.concatenate.backend.util.JsonUtilScope.Companion.get

private fun String.placeHolderReplaceWith(pool: Map<String, String>): String {
    return replace(Regex("\\$\\{([^}]*)}")) {
        val key = it.groupValues[1]
        if (!pool.containsKey(key)) error("$key is required!")// error handling here
        pool[key] ?: ""
    }
}

fun mappingGameArguments(jsonGameArgs: List<Any?>, config: Map<String, String>, ruleFeatures: Map<String, Boolean> = mapOf()): List<String> {
    return mutableListOf<String>().apply argList@{
        JsonUtilScope.run {
//            val json =  as
            for (item in jsonGameArgs) {
                if (item is String) {
                    this@argList += item.placeHolderReplaceWith(config)
                } else {
                    // rules
                    if (rulesJudging(item["rules"] as List<*>, ruleFeatures)) {
                        val values = item["value"] as List<String>
                        values.forEach { this@argList += it.placeHolderReplaceWith(config) }
                    }
                }
            }
        }
    }
}

fun mappingJvmArguments(jsonJvmArgs: List<Any?>, config: Map<String, String>): List<String> {
    return mutableListOf<String>().apply argList@{
        for (item in jsonJvmArgs) {
            if (item is String)
                this@argList += item.placeHolderReplaceWith(config)
            else {
                // rules
                if (rulesJudging(item["rules"] as List<*>)) {
                    val values = item["value"]
                    if (values is String)
                        this@argList += values.placeHolderReplaceWith(config)
                    else if (values is List<*>)
                        values.forEach {
                            it as String
                            this@argList += it.placeHolderReplaceWith(config)
                        }
                }
            }
        }
    }
}

fun mappingJvmMemoryArguments(config: Map<String, String>): List<String> {
    return mutableListOf<String>().apply {
        config["initial_java_heap_size"]?.let { this += "-Xms$it" }
        config["maximum_Java_heap_size"]?.let { this += "-Xmx$it" }
        config["the_young_generation_size"]?.let { this += "-Xmn$it" }
        config["use_g1gc"]?.let { if (it != "false") this += "-XX:+UseG1GC" }
        config["use_adaptive_size_policy"]?.let { if (it != "false") this += "-XX:-UseAdaptiveSizePolicy" }
        config["omit_stacktrace_in_fast_throw"]?.let { if (it != "false") this += "-XX:-OmitStackTraceInFastThrow" }
    }
}
