package org.shadow.studio.concatenate.backend.util

import org.shadow.studio.concatenate.backend.data.jrelocalpath.JavaRuntimeLocation
import org.shadow.studio.concatenate.backend.data.launchermeta.Version
import org.shadow.studio.concatenate.backend.launch.MinecraftVersion

internal val String.dateTimeInstance get() = parseOffsetIsoTime(this)

internal fun String.asVersion() =
    getInternalLauncherMetaManifest().versions.find {
        it.id == this
    } ?: run {
        // maybe versionId is too new, or it just not exists, anyway throw an exception
        error("$this is too new or just not exists")
    }

operator fun String.compareTo(mv: MinecraftVersion) = -mv.compareTo(this)

operator fun String.compareTo(version: Version) = version.releaseTime.dateTimeInstance.compareTo(this.dateTimeInstance)

operator fun String.rangeTo(versionId: String): List<Version> {
    val left = this.asVersion().releaseTime.dateTimeInstance
    val right = versionId.asVersion().releaseTime.dateTimeInstance

    return getInternalLauncherMetaManifest().versions
        .filter {
            val current = it.releaseTime.dateTimeInstance
            current in left..right
        }
}

operator fun String.rangeTo(version: MinecraftVersion): List<Version> = this..version.versionId

operator fun String.rangeTo(version: Version): List<Version> = this..version.id

operator fun String.rangeUntil(versionId: String): List<Version> {
    val left = this.asVersion().releaseTime.dateTimeInstance
    val right = versionId.asVersion().releaseTime.dateTimeInstance

    return getInternalLauncherMetaManifest().versions
        .filter {
            val current = it.releaseTime.dateTimeInstance
            left <= current && current < right
        }
}

operator fun String.rangeUntil(version: Version): List<Version> = this..<version.id

operator fun String.rangeUntil(version: MinecraftVersion): List<Version> = this..<version.versionId

operator fun List<Version>.contains(versionId: String): Boolean = versionId.asVersion() in this

@JvmName("versionListContainsMinecraftVersion")
operator fun List<Version>.contains(version: MinecraftVersion): Boolean = version.versionId in this

operator fun List<String>.contains(version: Version): Boolean = version.id in this

@JvmName("stringListContainsMinecraftVersion")
operator fun List<String>.contains(version: MinecraftVersion): Boolean = version.versionId in this

operator fun IntRange.contains(jdkVersion: JavaRuntimeLocation): Boolean = jdkVersion.majorVersion in this