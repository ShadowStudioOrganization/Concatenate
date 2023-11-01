package org.shadow.studio.concatenate.backend.util

private fun String.placeHolderReplaceWith(pool: Map<String, String>): String {
    return replace(Regex("\\$\\{([^}]*)}")) {
        val key = it.groupValues[1]
        pool[key] ?: ""// error handling here
    }
}

fun mappingGameArguments(versionJson: Map<String, Any>, config: Map<String, String>, ruleFeatures: Map<String, Boolean> = mapOf()): List<String> {
    return JsonUtilScope.run {
        val arguments = mutableListOf<String>()
        val json = versionJson["arguments"]["game"] as List<Any?>
        for (item in json) {
            if (item is String) {
                arguments += item.placeHolderReplaceWith(config)
            } else {
                // rules
                val rules = item["rules"] as List<*>
                var judgeFlag = 0

                rules.forEach { ruleTest ->
                    when (ruleTest["action"]) {
                        "allow" -> {
                            val features = ruleTest["features"] as Map<String, Boolean>
                            features.keys.forEach { featureKeys ->
                                if (ruleFeatures[featureKeys] != features[featureKeys]) judgeFlag ++
                            }
                        }
                    }
                }

                if (judgeFlag == 0) {
                    val values = item["value"] as List<String>
                    for (v in values) {
                        arguments += v.placeHolderReplaceWith(config)
                    }
                }
            }
        }
        arguments
    }
}

fun mappingJvmArguments(versionJson: Map<String, Any>, config: Map<String, String>): List<String> {
    return JsonUtilScope.run {
        val arguments = mutableListOf<String>()
        val json = versionJson["arguments"]["jvm"] as List<Any?>

        for (item in json) {
            if (item is String)
                arguments += item.placeHolderReplaceWith(config)
            else {
                // rules
                val rules = item["rules"] as List<*>
                var judgeFlag = 0

                rules.forEach { ruleTest ->
                    val os = ruleTest["os"] as Map<String, *>
                    when (ruleTest["action"]) {
                        "allow" -> {
                            if (os.containsKey("name")) {
                                if (os["name"] != getSystemName()) {
                                    judgeFlag ++
                                }
                            }
                            if (os.containsKey("arch")) {
                                if (os["arch"] != getSystemArch()) {
                                    judgeFlag ++
                                }
                            }
                            if (os.containsKey("version")) {
                                if (os["version"] != getSystemVersion()) {
                                    judgeFlag ++
                                }
                            }
                        }
                        "disallow" -> {

                        }
                    }
                }
                if (judgeFlag == 0) {
                    val values = item["value"]
                    if (values is String)
                        arguments += values.placeHolderReplaceWith(config)
                    else if (values is List<*>)
                        values.forEach {
                            it as String
                            arguments += it.placeHolderReplaceWith(config)
                        }
                }
            }
        }

        arguments
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
