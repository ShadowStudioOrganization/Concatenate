package org.shadow.studio.concatenate.backend.serializer

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationConfig
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.BeanDeserializerFactory
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.type.TypeFactory
import org.shadow.studio.concatenate.backend.data.profile.Artifact
import org.shadow.studio.concatenate.backend.data.profile.Downloads
import org.shadow.studio.concatenate.backend.data.profile.LibraryItem

class LibraryItemDeserializer : StdDeserializer<LibraryItem>(LibraryItem::class.java) {

    override fun deserialize(parser: JsonParser?, ctx: DeserializationContext?): LibraryItem {
        if (parser == null || ctx == null) error("jackson parser or deserialization context can not be null")
        val node: JsonNode = parser.codec.readTree(parser)

        if (node.has("downloads")) {
            // resolution from https://stackoverflow.com/a/47274493/20029350

            val config: DeserializationConfig = ctx.config
            val type = TypeFactory.defaultInstance().constructType(LibraryItem::class.java)
            val defaultDeserializer =
                BeanDeserializerFactory.instance.buildBeanDeserializer(ctx, type, config.introspect(type))

            if (defaultDeserializer is ResolvableDeserializer) defaultDeserializer.resolve(ctx)

            val treeParser: JsonParser = parser.codec.treeAsTokens(node)
            config.initialize(treeParser)

            if (treeParser.currentToken == null) treeParser.nextToken()

            return defaultDeserializer.deserialize(treeParser, ctx) as LibraryItem
        } else {
            // assume as fabric library

            val name = node["name"].textValue() ?: error("")
            val repoUrl = node["url"].textValue() ?: error("")

            val pathCom = name.split(":").apply { if (size !in 3..4) error("") }

            val group = pathCom[0]
            val artifactName = pathCom[1]
            val version = pathCom[2]

            val path = buildString {
                append(group.replace(".", "/"))
                append("/")
                append(artifactName)
                append("/")
                append(version)
                append("/")
                append(artifactName)
                append("-")
                append(version)
                append(".jar")
            }

            val url = "$repoUrl$path"

            return LibraryItem(
                name,
                Downloads(
                    Artifact(path, "", 1L, url).apply { isUnknownSH1orSize = true }
                ),
                url =  url
            )
        }
    }
}