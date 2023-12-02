package org.shadow.studio.concatenate.backend.serializer

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.core.TreeNode
import com.fasterxml.jackson.databind.DeserializationConfig
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.BeanDeserializerFactory
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.type.TypeFactory
import kotlinx.coroutines.currentCoroutineContext
import org.shadow.studio.concatenate.backend.data.profile.LibraryItem
import kotlin.reflect.KClass

open class DefaultActionDeserializer <T : Any> (clz: KClass<T>): StdDeserializer<T>(clz.java) {

    override fun deserialize(parser: JsonParser, ctx: DeserializationContext): T {
        // resolution from https://stackoverflow.com/a/47274493/20029350

        val tree = parser.readValueAsTree<TreeNode>()
        val traverseParser = tree.traverse(ctx.parser.codec)

        val config: DeserializationConfig = ctx.config
        val type = TypeFactory.defaultInstance().constructType(_valueClass)
        val defaultDeserializer =
            BeanDeserializerFactory.instance.buildBeanDeserializer(ctx, type, config.introspect(type))

        if (defaultDeserializer is ResolvableDeserializer) defaultDeserializer.resolve(ctx)

        val treeParser: JsonParser = traverseParser.codec.treeAsTokens(tree)
        config.initialize(treeParser)

        if (treeParser.currentToken == null) treeParser.nextToken()

        return defaultDeserializer.deserialize(treeParser, ctx) as T
    }

    protected open fun copyCurrentLocationParserAsTreeNode(parser: JsonParser): TreeNode {
        return parser.readValueAsTree()
    }

    protected fun preReadJsonRootKeys(treeNode: TreeNode): Map<String, Any?> {
        val preCheckParser = treeNode.traverse()
        return buildMap {
            if (preCheckParser.currentToken == null) preCheckParser.nextToken()

            var key: String? = null
            var value: Any? = null

            val insert = {
                key?.let {
                    put(it, value)
                    value = null
                    key = null
                }
            }

            while (preCheckParser.hasCurrentToken()) {
                preCheckParser.nextToken()
                when (preCheckParser.currentToken) {
                    JsonToken.START_OBJECT, JsonToken.START_ARRAY -> {
                        value = preCheckParser.currentToken
                        preCheckParser.skipChildren()
                        insert()
                    }

                    JsonToken.FIELD_NAME -> key = preCheckParser.currentName
                    JsonToken.VALUE_STRING -> {
                        value = preCheckParser.text
                        insert()
                    }

                    JsonToken.VALUE_NUMBER_FLOAT, JsonToken.VALUE_NUMBER_INT -> {
                        value = when (preCheckParser.numberType) {
                            JsonParser.NumberType.DOUBLE -> preCheckParser.numberValue.toDouble()
                            JsonParser.NumberType.FLOAT -> preCheckParser.numberValue.toFloat()
                            JsonParser.NumberType.INT -> preCheckParser.numberValue.toInt()
                            JsonParser.NumberType.LONG -> preCheckParser.numberValue.toLong()
                            JsonParser.NumberType.BIG_DECIMAL -> preCheckParser.numberValue
                            JsonParser.NumberType.BIG_INTEGER -> preCheckParser.numberValue
                        }
                        insert()
                    }

                    JsonToken.VALUE_TRUE, JsonToken.VALUE_FALSE -> {
                        value = preCheckParser.valueAsBoolean
                        insert()
                    }

                    JsonToken.VALUE_NULL -> {
                        value = null
                        insert()
                    }

                    else -> {}
                }
            }
        }
    }
}