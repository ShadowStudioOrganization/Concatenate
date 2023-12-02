package org.shadow.studio.concatenate.backend.serializer

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import org.shadow.studio.concatenate.backend.data.profile.JsonProfile

class JsonProfileDeserializer : DefaultActionDeserializer<JsonProfile>(JsonProfile::class) {

    override fun deserialize(parser: JsonParser, ctx: DeserializationContext): JsonProfile {

        val treeNode = copyCurrentLocationParserAsTreeNode(parser)
        val rootTypes = preReadJsonRootKeys(treeNode)

        return if ("inheritsFrom" in rootTypes.keys) {
            val inheritsFrom = rootTypes["inheritsFrom"] as? String ?: error("")



            // reparse the json
            TODO("aa")
        } else super.deserialize(treeNode.traverse(parser.codec), ctx)
    }




}