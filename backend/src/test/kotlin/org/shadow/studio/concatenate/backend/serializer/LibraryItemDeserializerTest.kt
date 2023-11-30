package org.shadow.studio.concatenate.backend.serializer

import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.shadow.studio.concatenate.backend.data.profile.Arguments
import org.shadow.studio.concatenate.backend.data.profile.JsonProfile
import org.shadow.studio.concatenate.backend.data.profile.LibraryItem
import org.shadow.studio.concatenate.backend.resolveBackendBuildPath

class LibraryItemDeserializerTest {

    @Test
    fun deserialize() {

        val al  = jacksonObjectMapper().apply {
            val module = SimpleModule()
            module.addDeserializer(Arguments::class.java, MinecraftArgumentsDeserializer())
            registerModule(module)
        }.readValue<JsonProfile>(resolveBackendBuildPath("run2/versions/1.18.2/1.18.2.json"))

        val bl = jacksonObjectMapper().apply {
            val module = SimpleModule()
            module.addDeserializer(Arguments::class.java, MinecraftArgumentsDeserializer())
            module.addDeserializer(LibraryItem::class.java, LibraryItemDeserializer())
            registerModule(module)
        }.readValue<JsonProfile>(resolveBackendBuildPath("run2/versions/1.18.2/1.18.2.json"))

        assertEquals(al, bl)
    }
}