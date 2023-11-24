package org.shadow.studio.concatenate.backend.download

import org.shadow.studio.concatenate.backend.data.download.DownloadTask
import org.shadow.studio.concatenate.backend.data.download.RemoteFile
import org.shadow.studio.concatenate.backend.util.globalLogger
import org.slf4j.Logger
import java.nio.file.Files
import kotlin.io.path.exists

interface Downloader {

    val taskBufferSizeAllocation: (List<RemoteFile>) -> Long
        get() = ::defaultTaskBufferSizeAllocationMode

    val logger: Logger
        get() = globalLogger

    val remoteFiles: List<RemoteFile>

    val taskTTL: Int

    /**
     * @return failed download tasks
     */
    suspend fun download(): ConcatQueue<DownloadTask>

    /**
     * @return failed remote files
     */
    suspend fun checksum(): List<RemoteFile>

    suspend fun rangedDownload(downloadTask: DownloadTask)

    fun createParentsFolders() {
        remoteFiles.forEach {
            val parentPath = it.localDestination.parent
            if (!parentPath.exists()) {
                Files.createDirectories(parentPath)
                logger.info("created dir at: $parentPath")
            }
        }
    }

    fun splitFileSize(length: Long, count: Int): List<LongRange> {
        val piece = length / count
        val left = length % count

        return buildList {
            repeat(count) {
                add(piece * it until piece * (it + 1))
            }

            if (left != 0L) {
                add(piece * count until length)
            }
        }
    }

    fun deliverTasks(remoteFiles: List<RemoteFile>, taskMaxBufferSize: Long): List<DownloadTask> {
        return buildList<DownloadTask> {
            remoteFiles.forEach { file ->
                if (file.size / taskMaxBufferSize == 0L) {
                    // 单次可运完
                    this += DownloadTask(0L until file.size, file.localDestination, file.url, file.size, taskTTL)
                } else {
                    // 分多次运
                    val count = file.size / taskMaxBufferSize

                    if (count > Int.MAX_VALUE) error("split count to big, max is ${Int.MAX_VALUE}")

                    this += splitFileSize(file.size, count.toInt())
                        .map { DownloadTask(it, file.localDestination, file.url, file.size, taskTTL) }
                }
            }
        }

    }

    fun defaultTaskBufferSizeAllocationMode(files: List<RemoteFile>): Long {
        return files.maxOf { it.size }
    }
}

