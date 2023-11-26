package org.shadow.studio.concatenate.backend.launch

import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.shadow.studio.concatenate.backend.data.Version
import org.shadow.studio.concatenate.backend.data.profile.Arguments
import org.shadow.studio.concatenate.backend.data.profile.JsonProfile
import org.shadow.studio.concatenate.backend.serializer.MinecraftArgumentsDeserializer
import org.shadow.studio.concatenate.backend.util.getInternalLauncherMetaManifest
import org.shadow.studio.concatenate.backend.util.*
import java.io.File

class MinecraftVersion(
    val versionName: String,
    private val jsonProfile: String,
    private val gameJar: File,
    val isolated: Boolean = false
) {

    constructor(
        versionName: String,
        jsonProfile: File,
        gameJar: File = File(jsonProfile.parentFile, "$versionName.jar"),
        isolated: Boolean = false
    ) : this(versionName, jsonProfile.readText(), gameJar, isolated)

    val profile: JsonProfile by lazy { getGameJsonProfile() }
    val versionId: String by lazy { profile.id }

    fun getAssetIndex(): String = profile.assetIndex.id

    fun getJarFile(): File = gameJar

    private fun getGameJsonProfile(): JsonProfile {
        val objectMapper = jacksonObjectMapper()
        val module = SimpleModule()
        module.addDeserializer(Arguments::class.java, MinecraftArgumentsDeserializer())
        objectMapper.registerModule(module)

        return objectMapper.readValue<JsonProfile>(jsonProfile)
    }

    operator fun compareTo(versionId: String): Int {
        return profile.releaseTime.dateTimeInstance.compareTo(versionId.asVersion().releaseTime.dateTimeInstance)
    }

    operator fun compareTo(other: MinecraftVersion): Int {
        return profile.releaseTime.dateTimeInstance.compareTo(other.profile.releaseTime.dateTimeInstance)
    }

    operator fun compareTo(other: Version): Int {
        return profile.releaseTime.dateTimeInstance.compareTo(other.releaseTime.dateTimeInstance)
    }

    operator fun rangeTo(versionId: String): List<Version> {

        val left = this.profile.releaseTime.dateTimeInstance
        val right = versionId.asVersion().releaseTime.dateTimeInstance

        return getInternalLauncherMetaManifest().versions
            .filter {
                val current = it.releaseTime.dateTimeInstance
                current in left..right
            }
    }

    operator fun rangeTo(other: MinecraftVersion): List<Version> = this..other.versionId

    operator fun rangeTo(other: Version): List<Version> = this..other.id

    operator fun rangeUntil(versionId: String): List<Version> {
        val left = this.profile.releaseTime.dateTimeInstance
        val right = versionId.asVersion().releaseTime.dateTimeInstance

        return getInternalLauncherMetaManifest().versions
            .filter {
                val current = it.releaseTime.dateTimeInstance
                left <= current && current < right
            }
    }

    operator fun rangeUntil(other: MinecraftVersion): List<Version> = this..<other.versionId

    operator fun rangeUntil(other: Version): List<Version> = this..<other.id

    override fun equals(other: Any?): Boolean {
        return if (other is MinecraftVersion) {
            other.versionId == versionId && other.versionName == versionName && other.isolated == isolated
        } else false
    }

    override fun hashCode(): Int {
        var result = versionName.hashCode()
        result = 31 * result + jsonProfile.hashCode()
        result = 31 * result + gameJar.absolutePath.hashCode()
        result = 31 * result + isolated.hashCode()
        return result
    }

}

