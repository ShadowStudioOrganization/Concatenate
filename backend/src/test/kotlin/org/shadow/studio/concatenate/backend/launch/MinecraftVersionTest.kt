package org.shadow.studio.concatenate.backend.launch

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import java.io.File
import org.shadow.studio.concatenate.backend.util.*
import kotlin.test.assertTrue

class MinecraftVersionTest {


    @Test
    fun testSigns() {
        val v1_17_1 = MinecraftVersion(
            versionName = "1.17.1",
            jsonProfile = File("D:/Games/aloneg/versions/1.17.1/1.17.1.json")
        )

        val v1_18_1 = MinecraftVersion(
            versionName = "1.18.1",
            jsonProfile = File("D:/Games/aloneg/versions/1.18.1/1.18.1.json")
        )

        val v1_20 = MinecraftVersion(
            versionName = "1.20",
            jsonProfile = File("D:/Games/aloneg/versions/1.20/1.20.json")
        )

        assertTrue(v1_18_1 > v1_17_1)
        assertTrue(v1_20 > v1_18_1)
        assertTrue(v1_20 >= v1_17_1)
        assertTrue(v1_17_1 >= v1_17_1)
        assertTrue(v1_17_1 <= v1_17_1)
        assertTrue(v1_17_1 < v1_20)
        assertTrue("1.16.5" < v1_20)
        assertTrue("1.20" <= v1_20)
        assertTrue(v1_18_1 >= "1.7.10")
        assertFalse(v1_17_1 < v1_17_1)
        assertTrue(v1_17_1 in "1.13.2".."1.19")
        assertTrue(v1_17_1 !in "1.2.5".."1.12.2")
        assertTrue("1.15" in "1.11".."1.16")
        assertTrue("1.15" !in "1.11".."1.12.2")

        assertFalse(v1_17_1 in "1.13.2"..<"1.17.1")
        assertTrue(v1_18_1 !in "1.2.5"..<"1.18.1")
        assertFalse("1.15" in "1.11"..<"1.15")
        assertTrue("1.13.2" !in "1.12"..<"1.13.2")

        assertTrue(v1_17_1 in v1_17_1..v1_20)
        assertTrue(v1_20 !in v1_17_1..<v1_20)
        assertTrue("1.18" in v1_17_1..v1_18_1)
        assertTrue(v1_20 !in "1.14.1"..<v1_20)
        assertTrue(v1_20 in v1_17_1.."1.20.1")
    }

    @Test
    fun getProfile() {
    }

    @Test
    fun getAssetIndex() {
    }

    @Test
    fun getJarFile() {
    }

    @Test
    fun getVersionId() {
    }

    @Test
    fun getVersionName() {
    }
}