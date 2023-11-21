package org.shadow.studio.concatenate.backend.util

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class StringKtTest {

    @Test
    fun wrapDoubleQuote() {
        assertEquals("\"a\"", "a".wrapDoubleQuote())
    }

    @Test
    fun replaceDollarExpressions() {
        val testString = "--versionType \${version_type} --username \${auth_player_name} --version \${version_name}"
        val replaced = testString.replaceDollarExpressions(buildMap {
            put("version_type", "msa")
            put("auth_player_name", "whiterasbk")
            put("version_name", "concatenate")
        })

        assertEquals("--versionType msa --username whiterasbk --version concatenate", replaced)

    }
}