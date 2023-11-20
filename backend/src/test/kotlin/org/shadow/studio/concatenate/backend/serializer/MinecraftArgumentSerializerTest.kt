package org.shadow.studio.concatenate.backend.serializer

import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.shadow.studio.concatenate.backend.data.profile.Arguments

class MinecraftArgumentSerializerTest {

    @Test
    fun serialize() {
        val objectMapper = jacksonObjectMapper()

        val module = SimpleModule()
        module.addSerializer(Arguments::class.java, MinecraftArgumentSerializer())
        objectMapper.registerModule(module)

        val arguments = Arguments(
            game = listOf("ExampleString"), // 或者 listOf(ComplexRule())
            jvm = listOf(1, 2, 3)
        )

        // 将 Arguments 对象转换为 JSON 字符串
        val json = objectMapper.writeValueAsString(arguments)
        println(json)
    }
}