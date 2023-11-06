package org.shadow.studio.concatenate.backend.util

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.shadow.studio.concatenate.backend.test.getResourceAsBytes
import org.shadow.studio.concatenate.backend.test.getResourceAsStream

class ChecksumKtTest {

    @Test
    fun calculateSHA1() {
        val mid = getResourceAsStream("偏偏喜欢你.mid")
        assertEquals("19c75d4dce8c84322446ca74ed192aa3820fc327", calculateSHA1(mid))
    }
}