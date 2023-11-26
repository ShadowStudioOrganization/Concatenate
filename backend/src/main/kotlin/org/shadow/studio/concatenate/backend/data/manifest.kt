package org.shadow.studio.concatenate.backend.data

data class LauncherMetaManifest(
    val latest: Latest,
    val versions: List<Version>
)

data class Latest(
    val release: String,
    val snapshot: String
)

data class Version(
    val id: String,
    val type: String,
    val url: String,
    val time: String,
    val releaseTime: String,
    val sha1: String,
    val complianceLevel: Int? = null
) {
    companion object {
        fun forDownload(id: String, url: String, sha1: String) = Version(id, "", url, "", "", sha1)
    }
}
