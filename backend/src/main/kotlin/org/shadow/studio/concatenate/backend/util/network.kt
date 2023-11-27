package org.shadow.studio.concatenate.backend.util

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import io.ktor.utils.io.nio.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.shadow.studio.concatenate.backend.data.download.DownloadTask
import org.slf4j.Logger
import java.io.RandomAccessFile
import java.net.HttpURLConnection
import java.net.Proxy
import java.net.URL
import java.nio.ByteBuffer
import java.nio.channels.Channels
import java.nio.channels.FileChannel
import java.nio.channels.ReadableByteChannel
import java.nio.file.Files
import java.nio.file.Path

val globalClient by lazy {
    HttpClient(OkHttp) {
        engine {
            config {
                followRedirects(true)
            }
        }

        BrowserUserAgent()
    }
}

class URLDownloadMethodConfig {
    var proxy: Proxy? = null
    val headers: MutableMap<String, String> = mutableMapOf()
    var connectTimeout: Int? = null
    var readTimeout: Int? = null
    var requestMethod: String? = null // GET POST
    var defaultUseCaches: Boolean? = null
    var useCaches: Boolean? = null
    var instanceFollowRedirects: Boolean? = null
    var doInput: Boolean? = null
    var doOutput: Boolean? = null
    var processURLString: (String) -> String = { it }

    fun headerOf(name: String, value: String) {
        headers[name] = value
    }
}


suspend fun ktorRangedDownloadAndTransferTo(
    task: DownloadTask,
    bufferSize: Long = DEFAULT_BUFFER_SIZE.toLong(),
    client: HttpClient = globalClient,
    logger: Logger = globalLogger,
    callback: (suspend (Long, Long, Long) -> Unit)? = null
) = ktorRangedDownloadAndTransferTo(
    task.getDownloadUrl(),
    task.range,
    task.remoteFile.localDestination,
    task.remoteFile.size,
    bufferSize,
    client,
    logger,
    callback
)

suspend fun ktorRangedDownloadAndTransferTo(
    url: String,
    range: IntRange,
    destination: Path,
    originalFileSize: Long,
    bufferSize: Long = DEFAULT_BUFFER_SIZE.toLong(),
    client: HttpClient = globalClient,
    logger: Logger = globalLogger,
    callback: (suspend (Long, Long, Long) -> Unit)? = null,
) = ktorRangedDownloadAndTransferTo(url, range.toLongRange(), destination, originalFileSize, bufferSize, client, logger, callback)

/**
 * @param callback when each copy finished, it will call this function, parameter1: bytes that written this loop, parameter2: bytes have written so far, parameter3: total bytes that need to be written
 */
suspend fun ktorRangedDownloadAndTransferTo(
    url: String,
    range: LongRange,
    destination: Path,
    originalFileSize: Long,
    bufferSize: Long = DEFAULT_BUFFER_SIZE.toLong(),
    client: HttpClient = globalClient,
    logger: Logger = globalLogger,
    callback: (suspend (Long, Long, Long) -> Unit)? = null,
) {
    client.prepareGet(url) {
        if (range.size() != originalFileSize) {
            // full content file task
            headers {
                append("Range", "bytes=${range.first}-${range.last}")
            }
        }
    }.execute { httpResponse ->
        val channel: ByteReadChannel = httpResponse.bodyAsChannel()

        if (Files.isDirectory(destination)) error("destination must be a file, which '$destination' is not")

        val file = RandomAccessFile(destination.toFile(), "rw")

        destination.apply {
            if (!Files.exists(this)) {
                Files.createFile(this)
                logger.debug("created file at: {}", this)
            }

            if (Files.size(this) != originalFileSize) {
                file.setLength(originalFileSize)
                logger.debug("set length of {} to {}", this, originalFileSize)
            }
        }

        file.seek(range.first)
        var totalBytesWrittenSoFar = 0L
        val rangeLength = range.last - range.first + 1

        while (!channel.isClosedForRead) {
            val packet = channel.readRemaining(bufferSize)
            while (!packet.isEmpty) {
                val bytesWrittenThisLoop = packet.remaining
                totalBytesWrittenSoFar += bytesWrittenThisLoop
                file.channel.writePacket(packet)
                callback?.invoke(bytesWrittenThisLoop, totalBytesWrittenSoFar, rangeLength)
            }
        }

        file.close()

        channel.closedCause.let {
            if (it != null) logger.error("download failed, reason: $it")
        }
    }
}

suspend fun urlRangedDownloadAndTransferTo(
    url: String,
    range: IntRange,
    channel: FileChannel,
    config: URLDownloadMethodConfig.() -> Unit = {}
) = withContext(Dispatchers.IO) {
    val byteChannel = urlRangedDownload(url, range, config)
    channel.transferFrom(byteChannel, range.first.toLong(), range.last.toLong() - range.first.toLong() + 1)
    byteChannel.close()
}

suspend fun urlRangedDownload(url: String, range: IntRange, config: URLDownloadMethodConfig.() -> Unit = {}): ReadableByteChannel {
    return urlDownload(url) {
        config()
        headerOf("Range", "bytes=${range.first}-${range.last}")
    }
}

suspend fun urlDownload(url: String, config: URLDownloadMethodConfig.() -> Unit = {}): ReadableByteChannel = withContext(Dispatchers.IO) {

    val udmc = URLDownloadMethodConfig()
    udmc.config()

    val connection = URL(udmc.processURLString(url)).let { url ->
        val connection = udmc.proxy?.let { url.openConnection(it) } ?: url.openConnection()
        connection as HttpURLConnection
    }.apply {
        udmc.headers.takeIf { it.isNotEmpty() } ?.forEach { (name, value) -> addRequestProperty(name, value) }
        udmc.connectTimeout?.let { connectTimeout = it }
        udmc.readTimeout?.let { readTimeout = it }
        udmc.requestMethod?.let { requestMethod = it }
        udmc.defaultUseCaches?.let { defaultUseCaches = it }
        udmc.useCaches?.let { useCaches = it }
        udmc.instanceFollowRedirects?.let { instanceFollowRedirects = it }
        udmc.doInput?.let { doInput = it }
        udmc.doOutput?.let { doOutput = it }
    }

    Channels.newChannel(connection.inputStream)
}


/**
 * fileChannel need to be closed by its provider
 */
suspend fun ByteReadChannel.copyToFile(fileChannel: FileChannel, bufferSize: Int = 8192) {
    val buffer = ByteBuffer.allocate(bufferSize)

    while (!isClosedForRead) {
        buffer.clear()
        readAvailable(buffer)
        buffer.flip()
        while (buffer.hasRemaining()) {
            fileChannel.write(buffer)
        }
    }
}