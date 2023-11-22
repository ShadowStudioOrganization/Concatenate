package org.shadow.studio.concatenate.backend.util

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class SystemKtTest {

    @Test
    fun listPaths() {
        var c = 0
        listPaths("D:", "Environments", "java", "%\\d\\d?%", "bin").forEach {
            println("${++c} -> $it")
        }

        listPaths("D:", "Environments", "java", "*", "bin").forEach {
            println("${++c} -> $it")
        }

        listPaths("C:", "Environments", "java", "*", "bin").forEach {
            println("${++c} -> $it")
        }


    }
}