package org.shadow.studio.concatenate.backend.checksum

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.shadow.studio.concatenate.backend.resolveBackendBuildPath
import org.shadow.studio.concatenate.backend.showRunTime
import kotlin.time.measureTime

class MinecraftResourceCheckerTest {

    @Test
    fun checkAssetsObjects() {
        showRunTime {
            val checker = MinecraftResourceChecker()
            val index = resolveBackendBuildPath("run/assets/indexes/8.json")
            val objects = resolveBackendBuildPath("run/assets/objects")
            val res = checker.checkAssetsObjects(index, objects)
            println(if (res.isEmpty()) "all files are completed" else "there are some files missing")
        }
    }
}