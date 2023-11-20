package org.shadow.studio.concatenate.backend.launch

import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.shadow.studio.concatenate.backend.data.profile.Arguments
import org.shadow.studio.concatenate.backend.data.profile.JsonProfile
import org.shadow.studio.concatenate.backend.serializer.MinecraftArgumentsDeserializer
import java.io.File

class MinecraftVersion(
    val versionId: String,
    private val jsonProfile: String,
    private val gameJar: File,
    val versionName: String,
    val isolated: Boolean = false
) {

    constructor(
        versionId: String,
        jsonProfile: File,
        gameJar: File,
        versionName: String,
        isolated: Boolean = false
    ) : this(versionId, jsonProfile.readText(), gameJar, versionName, isolated)

    val profile: JsonProfile by lazy {
        getGameJsonProfile()
    }

    fun getAccessIndex(): String = profile.assetIndex.id

    fun getJarFile(): File = gameJar

    private fun getGameJsonProfile(): JsonProfile {
        val objectMapper = jacksonObjectMapper()
        val module = SimpleModule()
        module.addDeserializer(Arguments::class.java, MinecraftArgumentsDeserializer())
        objectMapper.registerModule(module)

        return objectMapper.readValue<JsonProfile>(jsonProfile)
    }
}