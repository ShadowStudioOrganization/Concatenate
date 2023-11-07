package org.shadow.studio.concatenate.backend.util

import org.shadow.studio.concatenate.backend.data.profile.Rule


private fun resolveSingleRules(rule: Rule, featurePool: Map<String, Boolean> = mapOf()): Boolean {
    var flag = 0

    rule.features?.let { feature ->
        for (featureKey in feature.keys) {
            if (featurePool[featureKey] != feature[featureKey]) flag ++
        }
    }

    rule.os?.let { os ->
        os.name?.let { if (it != getSystemName()) flag ++ }
        os.arch?.let { if (it != getSystemArch()) flag ++ }
//        os.version?.let{ if (it != getSystemVersion()) flag++ }
        os.version?.let{ if (!it.toRegex().matches(getSystemVersion())) flag++ }
    }

    return if (rule.action == "allow") flag == 0 else flag != 0
}

fun resolveLibraryRules(rules: List<Rule>, featurePool: Map<String, Boolean> = mapOf()): Boolean {

    var result: Boolean = true

    for (rule in rules)
         result = resolveSingleRules(rule, featurePool) && result

    return result
}

@Deprecated("use resolveLibraryRules")
fun rulesJudging(rules: List<*>, featurePool: Map<String, Boolean> = mapOf()): Boolean {
    var judgeFlag = 0

    jsonObjectConvGet {
        rules.forEach { ruleTest ->
            when (ruleTest["action"]) {
                "allow" -> {

                    ruleTest as Map<String, *>

                    ruleTest.havingKey<Map<String, Boolean>>("features") { features ->
                        features.keys.forEach { featureKeys ->
                            if (featurePool[featureKeys] != features[featureKeys]) judgeFlag ++
                        }
                    }

                    ruleTest.havingKey<Map<String, *>>("os") { os ->
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

                }
                "disallow" -> {

                }
            }
        }
    }

    return judgeFlag == 0
}