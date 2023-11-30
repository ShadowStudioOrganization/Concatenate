package org.shadow.studio.concatenate.backend.download

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.utils.io.core.*
import io.ktor.utils.io.nio.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.shadow.studio.concatenate.backend.data.download.DownloadTask
import org.shadow.studio.concatenate.backend.data.download.ProgressInfo
import org.shadow.studio.concatenate.backend.util.DEFAULT_CONCATE_DOWNLOADER_KTOR_BUFFER_SIZE
import org.shadow.studio.concatenate.backend.util.DEFAULT_CONCATE_DOWNLOADER_POOL_SIZE
import org.shadow.studio.concatenate.backend.util.DEFAULT_CONCATE_DOWNLOADER_TASK_TTL
import org.shadow.studio.concatenate.backend.util.globalClient
import java.io.RandomAccessFile
import java.nio.file.Files

abstract class UnknownLengthResourceDownloader(
    officialRepositoryUrl: String,
    taskTTL: Int = DEFAULT_CONCATE_DOWNLOADER_TASK_TTL,
    poolSize: Int = DEFAULT_CONCATE_DOWNLOADER_POOL_SIZE,
    ktorClient: HttpClient = globalClient,
    ktorBuffetSize: Long = DEFAULT_CONCATE_DOWNLOADER_KTOR_BUFFER_SIZE
): MinecraftResourceDownloader(officialRepositoryUrl, poolSize, taskTTL, ktorClient, ktorBuffetSize) {

    override suspend fun rangedDownload(downloadTask: DownloadTask) = withContext(Dispatchers.IO) {
        val channel = ktorClient.get(downloadTask.remoteFile.url).bodyAsChannel()
        val local = downloadTask.remoteFile.localDestination

        if (Files.isDirectory(local)) error("destination must be a file, which '$local' is not")

        val file = RandomAccessFile(local.toFile(), "rw")

        local.apply {
            if (!Files.exists(this)) {
                Files.createFile(this)
                logger.debug("created file at: {}", this)
            }
        }

        file.channel.use {
            var totalBytesWrittenSoFar = 0L
            while (!channel.isClosedForRead) {
                val packet = channel.readRemaining(ktorBuffetSize)
                while (!packet.isEmpty) {
                    val bytesWrittenThisLoop = packet.remaining
                    totalBytesWrittenSoFar += bytesWrittenThisLoop
                    it.writePacket(packet)
                    internalDownloadCallback?.invoke(
                        ProgressInfo(
                            downloadTask.remoteFile,
                            downloadTask.range,
                            bytesWrittenThisLoop,
                            totalBytesWrittenSoFar,
                            totalBytesWrittenSoFar,
                            -1L
                        )
                    )
                }
            }
        }

        file.close()
    }
}

