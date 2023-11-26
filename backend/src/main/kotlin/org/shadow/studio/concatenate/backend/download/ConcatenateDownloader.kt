package org.shadow.studio.concatenate.backend.download

import io.ktor.client.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import org.shadow.studio.concatenate.backend.data.download.DownloadTask
import org.shadow.studio.concatenate.backend.data.download.DownloadTaskState
import org.shadow.studio.concatenate.backend.data.download.ProgressInfo
import org.shadow.studio.concatenate.backend.data.download.RemoteFile
import org.shadow.studio.concatenate.backend.util.*
import org.slf4j.Logger

const val DEFAULT_CONCATE_DOWNLOADER_POOL_SIZE = 64
const val DEFAULT_CONCATE_DOWNLOADER_TASK_TTL = 7
const val DEFAULT_CONCATE_DOWNLOADER_KTOR_BUFFER_SIZE = 256 * 1024L

open class ConcatenateDownloader(
    private val poolSize: Int = DEFAULT_CONCATE_DOWNLOADER_POOL_SIZE,
    override val taskTTL: Int = DEFAULT_CONCATE_DOWNLOADER_TASK_TTL,
    private val ktorClient: HttpClient = globalClient,
    private val ktorBuffetSize: Long = DEFAULT_CONCATE_DOWNLOADER_KTOR_BUFFER_SIZE
) : Downloader {

    private var internalRemoteFiles: List<RemoteFile>? = null
    private var internalBufferSizeAllocation: ((List<RemoteFile>) -> Long)? = null
    private var internalDownloadCallback: ((ProgressInfo) -> Unit)? = null
    private var internalLogger: Logger? = null

    private val totalBytes: Long by lazy {
        remoteFiles.sumOf { it.size }
    }
    private val processMutex = Mutex()
    private var doneBytes = 0L

    override val remoteFiles: List<RemoteFile>
        get() = internalRemoteFiles ?: error("remoteFiles has not been set, call setSource to set a source")

    override val taskBufferSizeAllocation: (List<RemoteFile>) -> Long
        get() = internalBufferSizeAllocation ?: super.taskBufferSizeAllocation

    override val logger: Logger
        get() = internalLogger ?: super.logger

    fun setLogger(logger: Logger) {
        this.internalLogger = logger
    }

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

    fun callback(action: (ProgressInfo) -> Unit) {
        this.internalDownloadCallback = action
    }

    override suspend fun rangedDownload(downloadTask: DownloadTask) {

        downloadTask.state = DownloadTaskState.Processing

        val callback: suspend (Long, Long, Long) -> Unit = { bytesWrittenThisLoop, bytesSoFar, _ ->
            processMutex.withLock { doneBytes += bytesWrittenThisLoop }

            internalDownloadCallback?.invoke(ProgressInfo(
                target = downloadTask.remoteFile,
                taskRange = downloadTask.range,
                bytesWrittenThisLoop = bytesWrittenThisLoop,
                bytesSoFarCountFromThisTask = bytesSoFar,
                doneBytesSoFar = doneBytes,
                totalBytes = totalBytes
            ))
        }

        ktorRangedDownloadAndTransferTo(
            downloadTask,
            ktorBuffetSize,
            ktorClient,
            logger,
            callback
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

    suspend fun download(tryTimes: Int, restFor: Long = 1000L): ConcatQueue<DownloadTask> {
        val failedTasks = download()
        return if (failedTasks.isNotEmpty()) {
            logger.debug("download failed, try to re-download $tryTimes times more")
            if (restFor > 0) delay(restFor)
            download(tryTimes - 1, restFor)
        } else ConcatQueue()
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
            logger.debug("enqueued task with index: {} ==> {}", index, downloadTask)
        }

        coroutineExecutorsAsync(poolSize) { id ->
            while (true) {
                val task = processMutex.withLock{ queue.dequeue() } ?: break
                try {
                    task.state = DownloadTaskState.Start
                    rangedDownload(task)
                    task.state = DownloadTaskState.Success
                    logger.debug(
                        "coroutine-{} downloaded range: {} sourceUrl: {} to local: {}",
                        id + 1,
                        task.range,
                        task.remoteFile.url,
                        task.remoteFile.localDestination
                    )
                } catch (e: Throwable) { // todo specific the exception type
                    logger.error("task error occurred, reason: ${e.localizedMessage}, exception: ${e.javaClass.name}, task info: $task")
                    if (task.ttl > 0) {
                        processMutex.withLock {
                            queue.enqueue(task.apply { ttl -- })
                            logger.error("task with ttl: ${task.ttl} re-enqueued, fully info: $task")
                            doneBytes -= task.range.size()
                        }
                    } else {
                        failedTaskQueue.enqueue(task.apply { task.state = DownloadTaskState.Failed })
                        logger.error("task is failed, loaded to failedTaskQueue, fully info: $task")
                    }
                }
            }
        }

        failedTaskQueue
    }

}