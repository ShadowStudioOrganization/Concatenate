package org.shadow.studio.concatenate.backend.data.download

import java.nio.file.Path

data class RemoteFile(
    val url: String,
    val size: Long,
    val localDestination: Path,
    val sha1: String?
)

data class DownloadTask(
    val range: LongRange,
    val localDestination: Path,
    val url: String,
    val originalFileSize: Long,
    var ttl: Int = 5,
    var isFailed: Boolean = false
)