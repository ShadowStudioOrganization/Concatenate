package org.shadow.studio.concatenate.backend.util

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File

class ModsListKtTest {

    @Test
    fun modsList() {
        val workingDirectory = "F:\\Minecraft\\[v1.0][1.12.2]末影工作室MOD服整合\\.minecraft"
//        val modList: Map<String, Boolean> = getModsList(File(workingDirectory))
//        for (mod in modList) {
//            println((if (mod.value)"[启用]" else "[禁用]") + mod.key)
//        }
        Assertions.assertNotNull(getModsList(File(workingDirectory)))
    }
}