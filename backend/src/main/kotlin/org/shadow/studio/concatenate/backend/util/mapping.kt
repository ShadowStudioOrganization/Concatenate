package org.shadow.studio.concatenate.backend.util

import org.shadow.studio.concatenate.backend.data.profile.ComplexMinecraftArgument
import org.shadow.studio.concatenate.backend.data.profile.Logging


/*private fun String.replaceDollarExpressions(pool: Map<String, String>): String {
    return replace(Regex("\\$\\{([^}]*)}")) {
        val key = it.groupValues[1]
        if (!pool.containsKey(key)) error("$key is required!") // error handling here
        pool[key] ?: ""
    }
}*/

/*
fun mappingComplexMinecraftArguments(jsonGameArgs: List<Any>, config: Map<String, String>, ruleFeatures: Map<String, Boolean> = mapOf()): List<String> {

    return buildList {
        for (item in jsonGameArgs) when (item) {
            is String -> +item.replaceDollarExpressions(config)
            is ComplexMinecraftArgument -> {
                item.rules?.let { rules ->
                    if (resolveLibraryRules(rules, ruleFeatures)) when (item.value) {
                        is String -> +item.value.replaceDollarExpressions(config)
                        is List<*> -> item.value.forEach { value -> +(value as String).replaceDollarExpressions(config) }
                    }
                }
            }
        }


       */
/* jsonObjectConvGet {
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
        }*//*

    }
}

*/

//
//fun mappingJvmArguments(jsonJvmArgs: List<Any>, config: Map<String, String>): List<String> {
//
//    return buildList {
//        for (item in jsonJvmArgs) when (item) {
//            is String -> +item.placeHolderReplaceWith(config)
//
//            is ComplexMinecraftArgument -> {
//                item.rules?.let { rules ->
//                    if (resolveLibraryRules(rules)) when (item.value) {
//                        is String -> +item.value.placeHolderReplaceWith(config)
//                        is List<*> -> item.value.forEach { v -> +(v as String).placeHolderReplaceWith(config) }
//                    }
//                }
//            }
//
//            /*if (item is String)
//                +item.placeHolderReplaceWith(config)
//            else {
//                // rules
//                val rules = jacksonMapper.convertValue<List<Rule>>(item["rules"]!!)
//                if (resolveLibraryRules(rules)) {
//                    val values = item["value"]
//                    if (values is String)
//                        +values.placeHolderReplaceWith(config)
//                    else if (values is List<*>)
//                        values.forEach {
//                            it as String
//                            +it.placeHolderReplaceWith(config)
//                        }
//                }
//            }*/
//        }
//    }
//
//    /*return jsonObjectConvGet {
//        buildList argList@{
//            for (item in jsonJvmArgs) {
//                if (item is String)
//                    +item.placeHolderReplaceWith(config)
//                else {
//                    // rules
//                    val rules = jacksonMapper.convertValue<List<Rule>>(item["rules"]!!)
//                    if (resolveLibraryRules(rules)) {
//                        val values = item["value"]
//                        if (values is String)
//                            +values.placeHolderReplaceWith(config)
//                        else if (values is List<*>)
//                            values.forEach {
//                                it as String
//                                +it.placeHolderReplaceWith(config)
//                            }
//                    }
//                }
//            }
//        }
//    }*/
//}

//fun mappingLoggingArguments(jsonLogging: Logging, loggingConfig: Map<String, String>): List<String> = buildList {
//    +jsonLogging.client.argument.replaceDollarExpressions(loggingConfig)
//}

//fun mappingExtraJvmArguments(config: Map<String, String>): List<String> {
//    return buildList {
//        config["initial_java_heap_size"]?.let { +"-Xms$it" }
//        config["maximum_Java_heap_size"]?.let { +"-Xmx$it" }
//        config["the_young_generation_size"]?.let { +"-Xmn$it" }
//        config["use_g1gc"]?.let { if (it != "false") +"-XX:+UseG1GC" }
//        config["use_adaptive_size_policy"]?.let { if (it != "false") +"-XX:-UseAdaptiveSizePolicy" }
//        config["omit_stacktrace_in_fast_throw"]?.let { if (it != "false") +"-XX:-OmitStackTraceInFastThrow" }
//        config["file_encoding"]?.let { +"-Dfile.encoding=$it" }
//        config["sun_stdout_encoding"]?.let { +"-Dsun.stdout.encoding=$it" }
//        config["sun_stderr_encoding"]?.let { +"-Dsun.stderr.encoding=$it" }
//    }
//}
