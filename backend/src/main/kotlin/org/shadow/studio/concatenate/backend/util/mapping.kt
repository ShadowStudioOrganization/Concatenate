package org.shadow.studio.concatenate.backend.util

import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.shadow.studio.concatenate.backend.data.profile.Rule

private val jacksonMapper = jacksonObjectMapper()

private fun String.placeHolderReplaceWith(pool: Map<String, String>): String {
    return replace(Regex("\\$\\{([^}]*)}")) {
        val key = it.groupValues[1]
        if (!pool.containsKey(key)) error("$key is required!") // error handling here
        pool[key] ?: ""
    }
}

fun mappingGameArguments(jsonGameArgs: List<Any?>, config: Map<String, String>, ruleFeatures: Map<String, Boolean> = mapOf()): List<String> {

    return buildList argList@{
        jsonObjectConvGet {
            for (item in jsonGameArgs) {
                if (item is String) {
                    +item.placeHolderReplaceWith(config)
                } else {
                    // rules
                    val rules = jacksonMapper.convertValue<List<Rule>>(item["rules"]!!)
                    if (resolveLibraryRules(rules, ruleFeatures)) {
                        val values = item["value"] as List<String>
                        values.forEach { +it.placeHolderReplaceWith(config) }
                    }
                }
            }
        }
    }
}

fun mappingJvmArguments(jsonJvmArgs: List<Any?>, config: Map<String, String>): List<String> {
    return jsonObjectConvGet {
        buildList argList@{
            for (item in jsonJvmArgs) {
                if (item is String)
                    +item.placeHolderReplaceWith(config)
                else {
                    // rules
                    val rules = jacksonMapper.convertValue<List<Rule>>(item["rules"]!!)
                    if (resolveLibraryRules(rules)) {
                        val values = item["value"]
                        if (values is String)
                            +values.placeHolderReplaceWith(config)
                        else if (values is List<*>)
                            values.forEach {
                                it as String
                                +it.placeHolderReplaceWith(config)
                            }
                    }
                }
            }
        }
    }
}

fun mappingLoggingArguments(jsonLogging: Map<String, *>, loggingConfig: Map<String, String>): List<String> = buildList {
    jsonObjectConvGet {
        val arg = jsonLogging["client"]["argument"] as String
        +arg.placeHolderReplaceWith(loggingConfig)
    }
}

fun mappingExtraJvmArguments(config: Map<String, String>): List<String> {
    return buildList {
        config["initial_java_heap_size"]?.let { +"-Xms$it" }
        config["maximum_Java_heap_size"]?.let { +"-Xmx$it" }
        config["the_young_generation_size"]?.let { +"-Xmn$it" }
        config["use_g1gc"]?.let { if (it != "false") +"-XX:+UseG1GC" }
        config["use_adaptive_size_policy"]?.let { if (it != "false") +"-XX:-UseAdaptiveSizePolicy" }
        config["omit_stacktrace_in_fast_throw"]?.let { if (it != "false") +"-XX:-OmitStackTraceInFastThrow" }
        config["file_encoding"]?.let { +"-Dfile.encoding=$it" }
        config["sun_stdout_encoding"]?.let { +"-Dsun.stdout.encoding=$it" }
        config["sun_stderr_encoding"]?.let { +"-Dsun.stderr.encoding=$it" }
    }
}
