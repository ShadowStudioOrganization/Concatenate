package org.shadow.studio.concatenate.backend.util

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.shadow.studio.concatenate.backend.data.LauncherMetaManifest

@get:JvmName("_internalVersionManifest")
private val internalLauncherMetaManifest: LauncherMetaManifest by lazy {
    jacksonObjectMapper().readValue<LauncherMetaManifest>(
        getResourceAsString("minecraft-versions-manifest/version_manifest_v2_23.11.21.json")
            ?: error("minecraft-versions-manifest/version_manifest_v2_23.11.21.json is not found in resource")
    )
}

fun getInternalLauncherMetaManifest(): LauncherMetaManifest = internalLauncherMetaManifest

fun parseJsonToMap(json: String): Map<String, Any?> {
    val mapper = jacksonObjectMapper()
    return mapper.readValue(json)
}

fun parseJson(json: String): JsonNode {
    return jacksonObjectMapper().readTree(json)
}

fun parseJsonStr(mapper: Map<String, Any?>): String {
    val jsonStr = ObjectMapper()
    return jsonStr.writeValueAsString(mapper)
}




