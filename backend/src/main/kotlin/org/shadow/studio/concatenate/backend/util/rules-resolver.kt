package org.shadow.studio.concatenate.backend.util

import org.shadow.studio.concatenate.backend.data.profile.Rule


fun resolveLibraryRules(rules: List<Rule>, featurePool: Map<String, Boolean> = mapOf()): Boolean {

    var allowFlag = 0
    var disallowFlag = 0

    for (rule in rules) {
        when (rule.action) {
            "allow" -> {
                rule.features?.let { feature ->
                    for (featureKey in feature.keys) {
                        if (featurePool[featureKey] != feature[featureKey]) allowFlag ++
                    }
                }
                rule.os?.let { osRule ->
                    if (osRule.name != getSystemName()) allowFlag ++
                    if (osRule.arch != getSystemArch()) allowFlag ++
                    if (osRule.version != getSystemArch()) allowFlag ++
                }
            }

            "disallow" -> {
                var disa = true

                rule.os?.let { os ->
                    disa = os.name != getSystemName() && disa
                    disa = os.arch != getSystemArch() && disa
                    disa = os.version != getSystemArch() && disa

                }
            }
        }
    }

    return allowFlag == 0 /*&& disallowFlag != 0*/
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