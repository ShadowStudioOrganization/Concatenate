package org.shadow.studio.concatenate.backend.serializer

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.shadow.studio.concatenate.backend.data.profile.Arguments

class MinecraftArgumentSerializer : JsonSerializer<Arguments>() {

    override fun serialize(value: Arguments?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        gen?.writeStartObject()
        value?.let {
            gen?.writeArrayFieldStart("game")

//            if (it.game.isNotEmpty()) {
//                if (it.game[0] is String) {
//                    it.game as List<String>
//                    it.game.forEach { gen?.writeString(it) }
//                } else if (it.game[0] is ComplexRule) {
//                    it.game as List<ComplexRule>
//                    it.game.forEach { /* Serialize ComplexRule as needed */ }
//                }
//            }

            gen?.writeEndArray()
            gen?.writeArrayFieldStart("jvm")
            it.jvm.forEach { gen?.writeNumber(it as Int) }
            gen?.writeEndArray()
        }
        gen?.writeEndObject()
    }
}