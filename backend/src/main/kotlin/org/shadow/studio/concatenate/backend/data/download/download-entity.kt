package org.shadow.studio.concatenate.backend.data.download

import java.nio.file.Path

data class RemoteFile(
    val url: String,
    val size: Long,
    val localDestination: Path,
    val sha1: String? = null
) {
    var splitsRanges: List<LongRange>? = null
}

data class DownloadTask(
    val range: LongRange,
    val remoteFile: RemoteFile,
    var ttl: Int = 5,
    var state: DownloadTaskState = DownloadTaskState.Idle
) {
    fun isFullyAFile() = range.last - range.first + 1 == remoteFile.size
}

enum class DownloadTaskState {
    Idle, Start, Processing, Success, Failed
}

class ProgressInfo(
    val target: RemoteFile,
    val taskRange: LongRange,
    val bytesWrittenThisLoop: Long,
    val bytesSoFarCountFromThisTask: Long,
    val doneBytesSoFar: Long,
    val totalBytes: Long
)

class DownloadProcess(filesize: Long) {

    private val markLists: MutableList<Long> = mutableListOf()

    fun mark(point: Long) {

    }
}