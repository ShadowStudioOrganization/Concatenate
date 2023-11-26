package org.shadow.studio.concatenate.backend.download

import org.shadow.studio.concatenate.backend.data.download.DownloadTask
import org.shadow.studio.concatenate.backend.data.download.RemoteFile
import org.shadow.studio.concatenate.backend.util.calculateSHA1
import org.shadow.studio.concatenate.backend.util.globalLogger
import org.slf4j.Logger
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.exists

interface MultiRoutineDownloader {

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
                    val range = 0L until file.size
                    this += DownloadTask(range, file, taskTTL)
                    file.splitsRanges = listOf(range)
                } else {
                    // 分多次运
                    val count = file.size / taskMaxBufferSize

                    if (count > Int.MAX_VALUE) error("split count to big, max is ${Int.MAX_VALUE}")

                    val ranges = mutableListOf<LongRange>()
                    this += splitFileSize(file.size, count.toInt()).map { DownloadTask(it.also { ranges += it }, file, taskTTL) }
                    file.splitsRanges = ranges
                }
            }
        }
    }

    fun needReDownload(path: Path, size: Long, sha1: String): Boolean {
        val file = path.toFile()
        return !path.exists() || size != file.length() || calculateSHA1(file) != sha1
    }

    fun <R> ifNeedReDownloadThen(path: Path, size: Long, sha1: String, action: () -> R): R? {
        if (needReDownload(path, size, sha1))
            return action()
        else logger.debug("skipped local: {}", path)
        return null
    }

    fun defaultTaskBufferSizeAllocationMode(files: List<RemoteFile>): Long {
        val maxSize = files.maxOfOrNull { it.size } ?: 1024L
        val upto = 50 * 1024 * 1024L
        return if (maxSize <= upto) maxSize else upto
    }
}

