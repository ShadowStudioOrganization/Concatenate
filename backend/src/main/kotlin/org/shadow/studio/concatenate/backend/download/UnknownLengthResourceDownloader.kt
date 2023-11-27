package org.shadow.studio.concatenate.backend.download

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.utils.io.core.*
import io.ktor.utils.io.nio.*
import org.shadow.studio.concatenate.backend.data.download.DownloadTask
import org.shadow.studio.concatenate.backend.data.download.ProgressInfo
import org.shadow.studio.concatenate.backend.util.globalClient

abstract class UnknownLengthResourceDownloader(
    officialRepositoryUrl: String,
    ktorClient :HttpClient = globalClient,
    ktorBuffetSize: Long = DEFAULT_CONCATE_DOWNLOADER_KTOR_BUFFER_SIZE
): MinecraftResourceDownloader(officialRepositoryUrl, 1, 3, ktorClient, ktorBuffetSize)  {
    override suspend fun rangedDownload(downloadTask: DownloadTask) {
        val channel = ktorClient.get(downloadTask.remoteFile.url).bodyAsChannel()
        val local = downloadTask.remoteFile.localDestination.toFile()
        local.outputStream().use {
            var totalBytesWrittenSoFar = 0L
            while (!channel.isClosedForRead) {
                val packet = channel.readRemaining(ktorBuffetSize)
                while (!packet.isEmpty) {
                    val bytesWrittenThisLoop = packet.remaining
                    totalBytesWrittenSoFar += bytesWrittenThisLoop
                    it.channel.writePacket(packet)
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
    }
}