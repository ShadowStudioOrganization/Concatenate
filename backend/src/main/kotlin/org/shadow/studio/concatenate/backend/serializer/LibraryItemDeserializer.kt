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

class LibraryItemDeserializer : DefaultActionDeserializer<LibraryItem>(LibraryItem::class) {

    override fun deserialize(parser: JsonParser, ctx: DeserializationContext): LibraryItem {
        val libraryItem = super.deserialize(parser, ctx)

        return libraryItem.downloads?.let { libraryItem } ?: run {
            val name = libraryItem.name
            val repoUrl = libraryItem.url ?: error("")

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
                url = url
            )
        }
    }
}