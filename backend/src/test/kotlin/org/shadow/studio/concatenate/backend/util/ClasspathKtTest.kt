package org.shadow.studio.concatenate.backend.util

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.shadow.studio.concatenate.backend.test.getResourceAsString
import java.io.File

class ClasspathKtTest {

    @Test
    fun gatheringClasspathTest() {

        val json = getResourceAsString("flandrebakapack-1.20.1.json")
        val mapper = jacksonObjectMapper()
        val map: Map<String, Any> = mapper.readValue(json)

        jsonObjectConvGet {
            gatheringClasspath(map["libraries"] as List<Map<String, *>>, File("D:/Games/aloneg/libraries")).forEach {
                assertTrue(File(it).exists())
            }
        }

    }
}