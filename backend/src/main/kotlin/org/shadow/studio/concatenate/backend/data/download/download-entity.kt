package org.shadow.studio.concatenate.backend.data.download

import org.shadow.studio.concatenate.backend.util.size
import java.nio.file.Path

data class RemoteFile(
    val url: String,
    val size: Long,
    val localDestination: Path,
    val sha1: String? = null
) {
    var downloadSources: List<String>? = null
    var splitsRanges: List<LongRange>? = null
}

data class DownloadTask(
    val range: LongRange,
    val remoteFile: RemoteFile,
    var ttl: Int = 5,
    var state: DownloadTaskState = DownloadTaskState.Idle,
    private var downloadSourceIndex: Int = 0
) {

    var isFailedOnFirstTime: Boolean = false

    fun isFullyAFile() = range.size() == remoteFile.size

    fun tweakDownloadIndex() {
        remoteFile.downloadSources?.takeIf { it.isNotEmpty() && it.lastIndex >= downloadSourceIndex }?.let {
            if (it.size - 1 > downloadSourceIndex)
                downloadSourceIndex ++
            else
                downloadSourceIndex = 0
        }
    }

    fun getDownloadUrl(): String {
        return if (!isFailedOnFirstTime)
            remoteFile.url
        else {
            remoteFile.downloadSources?.takeIf { it.isNotEmpty() && it.lastIndex >= downloadSourceIndex }?.let {
                it[downloadSourceIndex]
            } ?: remoteFile.url
        }
    }
}

enum class DownloadTaskState {
    Idle, Start, Processing, Success, Failed, ReEnqueue
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