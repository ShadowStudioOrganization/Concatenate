package org.shadow.studio.concatenate.backend.util

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.shadow.studio.concatenate.backend.data.profile.Rule
import org.shadow.studio.concatenate.backend.data.profile.RuleOS

class RulesResolverKtTest {

    @Test
    fun allowTest() {
        runsOn("windows", "amd64", "10.0") {
            assertTrue(judge(makeSingleOsRulesWithAction()))
            assertTrue(judge(makeSingleOsRulesWithAction(name = "windows")))
            assertFalse(judge(makeSingleOsRulesWithAction(version = "^10."))) // not my fault, go for bugjang
            assertTrue(judge(makeSingleOsRulesWithAction(version = "^10.*")))
            assertTrue(judge(makeSingleOsRulesWithAction(arch = "amd64")))
            assertTrue(judge(makeSingleOsRulesWithAction(name = "windows", arch = "amd64")))

            assertFalse(judge(makeSingleOsRulesWithAction(name = "osx")))
            assertFalse(judge(makeSingleOsRulesWithAction(name = "osx", arch = "amd64")))
            assertFalse(judge(makeSingleOsRulesWithAction(name = "osx", arch = "x86")))
            assertFalse(judge(makeSingleOsRulesWithAction(name = "windows", arch = "x86")))
        }
    }

    @Test
    fun disallowTest() {
        runsOn("windows", "amd64", "10.0") {
            assertFalse(judge(makeSingleOsRulesWithAction(action = "disallow")))
            assertFalse(judge(makeSingleOsRulesWithAction(name = "windows", action = "disallow")))
            assertFalse(judge(makeSingleOsRulesWithAction(arch = "amd64", action = "disallow")))
            assertFalse(judge(makeSingleOsRulesWithAction(name = "windows", arch = "amd64", action = "disallow")))

            assertTrue(judge(makeSingleOsRulesWithAction(name = "linux", action = "disallow")))
            assertTrue(judge(makeSingleOsRulesWithAction(arch = "x86", action = "disallow")))
            assertTrue(judge(makeSingleOsRulesWithAction(name = "osx", arch = "x86", action = "disallow")))
            assertTrue(judge(makeSingleOsRulesWithAction(name = "windows", arch = "x86", action = "disallow")))
            assertTrue(judge(makeSingleOsRulesWithAction(name = "linux", arch = "amd64", action = "disallow")))
        }
    }

    @Test
    fun mixTest() {
        runsOn("windows", "amd64", "10.0") {
            assertTrue(judge(makeMultipleOsRulesWithActions(
                "allow" to null,
                "disallow" to RuleOS(name = "osx")
            )))
        }
    }

    @Test
    fun testFeature() {
        assertTrue(judge(makeFeatures("allow", mapOf("is_demo_user" to true)), mapOf("is_demo_user" to true)))
        assertFalse(judge(makeFeatures("allow", mapOf("a" to true)), mapOf("a" to false)))
        assertFalse(judge(makeFeatures("disallow", mapOf("b" to true)), mapOf("b" to true)))
    }

    private fun makeFeatures(action: String, features: Map<String, Boolean>): String {
        val fm = features.map { (k, v) -> "\"$k\": \"$v\"" }.joinToString(",")
        val ff = if (fm.isNotBlank()) ", \"features\": {$fm}" else ""
        return "[{\"action\": \"$action\"$ff}]"
    }

    private fun makeMultipleOsRulesWithActions(vararg pairs: Pair<String, RuleOS?>): String = StringBuilder().apply {
        append("[")
        val ruleList = pairs.toMutableList()
        val (lastAction, lastOs) = ruleList.removeLast()
        for ((action, os) in ruleList) {
            append("{")
            append("\"action\": \"$action\"")
            if (os != null) {
                append(",")
                append(makeSingleOsRules(os))
            }
            append("},")
        }

        append("{")
        append("\"action\": \"$lastAction\"")
        if (lastOs != null) {
            append(",")
            append(makeSingleOsRules(lastOs))
        }
        append("}")
        append("]")
    }.toString()

    private fun makeSingleOsRulesWithAction(name: String? = null, arch: String? = null, version: String? = null, action: String = "allow"): String {
        val os = if (name == null && arch == null && version == null) {
            ""
        } else {
            StringBuilder().apply {
                append(",")
                append(makeSingleOsRules(RuleOS(name, arch, version)))
            }.toString()
        }

        return """[{"action": "$action"$os}]"""
    }

    private fun makeSingleOsRules(os: RuleOS): String = StringBuilder().apply {
        append("\"os\":{")
        val params = listOf("name" to os.name, "arch" to os.arch, "version" to os.version)
            .filter { it.second != null }
            .map { it.first to it.second!! }
            .toMutableList()
        val (lastKey, lastValue) = params.removeLast()
        params.forEach { (k, v) ->
            append("\"$k\": \"$v\",")
        }
        append("\"$lastKey\": \"$lastValue\"")
        append("}")
    }.toString()

    private fun runsOn(system: String = getSystemName(), arch: String = getSystemArch(), version: String = getSystemVersion(), block: () -> Unit) {
        val condition = getSystemName() == system && getSystemArch() == arch && version == getSystemVersion()
        if (condition) block()
    }

    private fun judge(json: String, features: Map<String, Boolean> = mapOf()): Boolean
        = resolveLibraryRules(jacksonObjectMapper().readValue<List<Rule>>(json), features)

}