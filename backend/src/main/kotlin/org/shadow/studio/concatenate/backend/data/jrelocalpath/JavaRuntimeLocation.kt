package org.shadow.studio.concatenate.backend.data.jrelocalpath

import java.nio.file.Path

data class JavaRuntimeLocation(
    val path: Path,
    val is64bit: Boolean,
    private val versionString: String
) {
    val majorVersion: Int get() {
        val vss = versionString.split(".")

        return when (vss.first().toIntOrNull()) {
            // 1.6, 1.8
            1 -> vss[1].toInt()
            // unknown jdk version
            null -> error("failed to parse jdk version")
            else -> vss.first().toInt()
        }
    }

    operator fun compareTo(other: JavaRuntimeLocation): Int {
        return majorVersion.compareTo(other.majorVersion)
    }

    operator fun compareTo(jdkVersion: Int): Int {
        return majorVersion.compareTo(jdkVersion)
    }
}