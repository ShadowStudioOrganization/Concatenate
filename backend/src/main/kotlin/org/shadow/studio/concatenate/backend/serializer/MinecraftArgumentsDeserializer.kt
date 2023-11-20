package org.shadow.studio.concatenate.backend.serializer

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import org.shadow.studio.concatenate.backend.data.profile.Arguments
import org.shadow.studio.concatenate.backend.data.profile.ComplexMinecraftArgument
import org.shadow.studio.concatenate.backend.data.profile.Rule


class MinecraftArgumentsDeserializer : StdDeserializer<Arguments>(Arguments::class.java) {
    override fun deserialize(parser: JsonParser?, ctxt: DeserializationContext?): Arguments {
        val node: JsonNode? = parser?.codec?.readTree(parser)

        val gameList = node?.get("game")!!.parse(parser)
        val jvmList = node.get("jvm")!!.parse(parser)

        return Arguments(gameList, jvmList)
    }

    private fun JsonNode.parse(parser: JsonParser): List<Any> {
        val retList = mutableListOf<Any>()
        if (this.isArray) {
            for (element in this) {
                if (element.isTextual) {
                    retList.add(element.textValue())
                } else if (element.isObject) {

                    val rules = if (element["rules"] != null && element["rules"].isArray) {
                        buildList<Rule> {
                            for (rule in element["rules"])
                                parser.codec?.treeToValue(rule, Rule::class.java)?.let { r -> add(r) }
                        }
                    } else error("rules field not found or cast to array failed.")

                    val value: Any = if (element["value"] != null) {
                        if (element["value"].isTextual)
                            element["value"].textValue()
                        else if (element["value"].isArray)
                            element["value"].map { v -> v.textValue() }
                        else error("value type only support string/array<string>.")
                    } else error("value field is not found.")

                    retList.add(ComplexMinecraftArgument(rules, value))
                }
            }
        }
        return retList
    }
}
