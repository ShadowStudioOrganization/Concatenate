package org.shadow.studio.concatenate.backend.download

import io.ktor.client.*
import kotlinx.coroutines.*
import org.shadow.studio.concatenate.backend.data.download.DownloadTask
import org.shadow.studio.concatenate.backend.data.download.RemoteFile
import org.shadow.studio.concatenate.backend.util.*
import org.shadow.studio.concatenate.backend.util.globalLogger
import java.util.concurrent.Executors

class ConcatenateDownloader(
    private val poolSize: Int = 64,
    override val taskTTL: Int = 7,
    private val ktorClient: HttpClient = globalClient,
    private val ktorBuffetSize: Long = 256 * 1024L
) : Downloader {

    private var internalRemoteFiles: List<RemoteFile>? = null
    private var internalBufferSizeAllocation: ((List<RemoteFile>) -> Long)? = null

    override val remoteFiles: List<RemoteFile>
        get() = internalRemoteFiles ?: error("remoteFiles has not been set, call setSource to set a source")

    override val taskBufferSizeAllocation: (List<RemoteFile>) -> Long
        get() = internalBufferSizeAllocation ?: super.taskBufferSizeAllocation

    fun setSource(remoteFiles: List<RemoteFile>) {
        internalRemoteFiles = remoteFiles
    }

    fun source(block: MutableList<RemoteFile>.() -> Unit) {
        internalRemoteFiles = buildList(block)
    }

    fun setSource(remoteFiles: RemoteFile) {
        internalRemoteFiles = listOf(remoteFiles)
    }

    fun taskBufferAllocateMode(mode: (List<RemoteFile>) -> Long) {
        this.internalBufferSizeAllocation = mode
    }

    override suspend fun rangedDownload(downloadTask: DownloadTask) {
        ktorRangedDownloadAndTransferTo(
            downloadTask.url,
            downloadTask.range,
            downloadTask.localDestination,
            downloadTask.originalFileSize,
            ktorBuffetSize,
            ktorClient,
            logger
        )
    }

    override suspend fun checksum(): List<RemoteFile> {
        logger.debug("checksum")

        return remoteFiles.filter { file ->
            file.sha1?.let { sha1 ->
                val dest = file.localDestination.toFile()
                dest.length() != file.size || calculateSHA1(dest) != sha1
            } ?: false
        }
    }

    override suspend fun download(): ConcatQueue<DownloadTask> = withContext(Dispatchers.IO) {

        val taskBufferSize = taskBufferSizeAllocation(remoteFiles)
        logger.info("set task buffer size to $taskBufferSize")

        val tasks = deliverTasks(remoteFiles, taskBufferSize)
        val queue = ConcatQueue<DownloadTask>()
        val failedTaskQueue = ConcatQueue<DownloadTask>()

        createParentsFolders()

        tasks.forEachIndexed { index, downloadTask ->
            queue.enqueue(downloadTask)
            logger.info("enqueued task with index: $index ==> $downloadTask")
        }

        coroutineExecutorsAsync(poolSize) { id ->
            while (true) {
                val task = queue.dequeue() ?: break
                try {
                    rangedDownload(task)
                    globalLogger.info("coroutine-$id downloaded range: ${task.range} sourceUrl: ${task.url} to local: ${task.localDestination}")
                } catch (e: Throwable) {
                    globalLogger.error("task error occurred, reason: $e")
                    if (task.ttl > 0) {
                        queue.enqueue(task.apply { ttl -- })
                        globalLogger.error("task with ttl: ${task.ttl} re-enqueued, fully info: $task")
                    } else {
                        failedTaskQueue.enqueue(task.apply { isFailed = true })
                        globalLogger.error("task is failed, loaded to failedTaskQueue, fully info: $task")
                    }
                }
            }
        }

        failedTaskQueue
    }

}