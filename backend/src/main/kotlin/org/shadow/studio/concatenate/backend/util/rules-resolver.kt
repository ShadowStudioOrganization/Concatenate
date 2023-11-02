package org.shadow.studio.concatenate.backend.util

import org.shadow.studio.concatenate.backend.util.JsonUtilScope.Companion.get

fun rulesJudging(rules: List<*>, featurePool: Map<String, Boolean> = mapOf()): Boolean {
    var judgeFlag = 0

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

    return judgeFlag == 0
}