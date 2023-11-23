package org.shadow.studio.concatenate.backend.download

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.*
import io.ktor.utils.io.jvm.javaio.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.sync.Semaphore
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toLocalDateTime
import org.shadow.studio.concatenate.backend.launch.MinecraftVersion
import org.shadow.studio.concatenate.backend.util.calculateSHA1
import org.shadow.studio.concatenate.backend.util.eachAvailableLibrary
import org.shadow.studio.concatenate.backend.util.getInternalVersionManifest
import org.shadow.studio.concatenate.backend.util.globalLogger
import java.io.File
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import java.util.ArrayDeque
import java.util.concurrent.Executors
import kotlin.io.path.exists

class NormalDownloader(val client: HttpClient = HttpClient(OkHttp) {
    engine {



//        proxy = ProxyBuilder.socks("127.0.0.1", 7890)
        proxy = ProxyBuilder.http("http://127.0.0.1:7890")
        config {
            followRedirects(true)
            proxy(ProxyBuilder.http("http://127.0.0.1:7890"))
//            proxy(ProxyBuilder.socks("127.0.0.1", 7890))
        }
    }

    defaultRequest {

    }

}) {

    data class DownloadTask(
        val range: IntRange,
        val localDestination: Path,
        val url: String,
        var ttl: Int = 5,
        var isFailed: Boolean = false
    )

    data class RemoteFile(
        val url: String,
        val size: Int,
        val localDestination: Path,
        val sha1: String?
    )

    fun deliverTasks(remoteFiles: List<RemoteFile>, taskMaxBufferSize: Int): List<DownloadTask> {
        return buildList<DownloadTask> {
            remoteFiles.forEach { file ->
                if (file.size / taskMaxBufferSize == 0) {
                    // 单次可运完
                    DownloadTask(0 until file.size, file.localDestination, file.url)
                } else {
                    // 分多次运
                    this += splitFileSize(file.size, file.size / taskMaxBufferSize)
                        .map { DownloadTask(it, file.localDestination, file.url) }
                }
            }
        }

    }

    suspend fun c() {

        val baseDir = "D:/ProjectFiles/idea/Concatenate/backend/build/tmp/repos"


        val lbs = buildList<RemoteFile> {
            eachAvailableLibrary(
                MinecraftVersion("1.17.1", File("D:/Games/aloneg/versions/1.17.1/1.17.1.json"))
                    .profile.libraries
            ) {
                it.downloads?.artifact?.let { a->
                    val p = Paths.get(baseDir, a.path)
                    add(RemoteFile(a.url, a.size.toInt(), p, a.sha1))
                }
            }
        }


        lbs.forEach {
//            val p = Paths.get(baseDir, it.localDestination.toString()).parent
            val p = it.localDestination.parent
            if (!p.exists()) {
                Files.createDirectories(p)
                globalLogger.info("created dir at: $p")
            }
        }

        val ts = deliverTasks(lbs, 50 * 1024)
        val queue = ConcatQueue<DownloadTask>()
        val almostFailedTask = ConcatQueue<DownloadTask>()

        ts.forEachIndexed { index, downloadTask ->
            println("$index ==> $downloadTask")
            queue.enqueue(downloadTask)
        }

        val poolSize = 50
        val semaphore = Semaphore(poolSize)
        val coroutineDispatcher = Executors.newFixedThreadPool(poolSize).asCoroutineDispatcher()
        val coroutineScope = CoroutineScope(coroutineDispatcher)

        val rafPool = mutableMapOf<String, RandomAccessFile>()

        val coroutinePool = List(poolSize) {
            coroutineScope.async {
                while (true) {
                    semaphore.acquire() // 获取信号量，限制并发数
                    val task = queue.dequeue() ?: break
                    try {
                        val data: HttpResponse = downloadRangedFile(task)
                        println("running in coro$it, cur data: $data")

                        val key = task.localDestination.normalize().toString()
                        val raf = rafPool[key] ?: run {
                            RandomAccessFile(key, "rwd").apply {
                                rafPool[key] = this
                            }
                        }

                        // 将 RandomAccessFile 定位到指定位置
                        raf.seek(task.range.first.toLong())

//                        val raf = RandomAccessFile(File(baseDir, task.localDestination.toString()), "rwd")
//
                        val channel = data.bodyAsChannel()
                        val bufferSize = 1024 * 128 // 根据需要调整缓冲区大小
                        channel.copyToFile(raf.channel, bufferSize)
                        println("copied piece: ${task.range} in ${task.localDestination}")
//

//
//                        // 创建 ByteBuffer 作为缓冲区
//                        val buffer = ByteBuffer.allocate(bufferSize)

                        // 创建一个通道，用于从数据通道读取并写入 RandomAccessFile
//                        val transferChannel = Channel<Int>()

                        // 启动协程，将数据从数据通道传输到 RandomAccessFile
//                        var totalBytesTransferred = 0
//                        while (true) {
//                            buffer.clear()
//                            val bytesRead = channel.readAvailable(buffer)
//                            if (bytesRead == -1) break // 读取完毕
//                            println("wir")
//                            buffer.flip()
//                            val bytesWritten = raf.channel.write(buffer)
//                            totalBytesTransferred += bytesWritten
//                            transferChannel.send(bytesWritten)
//                        }
//                        transferChannel.close()
                    } catch (e: Exception) {
                        // Handle the exception (e.g., log it)
                        println("Exception occurred: ${e.message} $e")
                        // Re-add the task to the queue for retry
                        if (task.ttl < 0) {
                            task.isFailed = true
                            almostFailedTask.enqueue(task)
                        } else {
                            queue.enqueue(task.apply {
                                ttl -= 1
                            })
                        }
                    } finally {
                        semaphore.release() // 释放信号量
                    }
                }

            }
        }

        // Wait for all tasks to complete
        coroutinePool.forEach {
            it.await()
        }

        // Shutdown the coroutine pool
        coroutineDispatcher.close()

        rafPool.forEach { (_, raf) ->
            raf.close()
        }

        val rts = lbs.sumOf { it.size }
        val nrts = lbs.sumOf { it.localDestination.toFile().length() }
        lbs.forEach {
            if (it.localDestination.toFile().length().toInt() != it.size || calculateSHA1(it.localDestination.toFile()) != it.sha1) {
                println("${it.localDestination} is bad, size: ${it.size}, real: ${it.localDestination.toFile().length()}")
            }
        }

        println("$rts vs $nrts")


    }


    suspend fun downloadRangedFile(task: DownloadTask): HttpResponse {
        return client.get(task.url) {
            headers {
                append("Range", "bytes=${task.range.first}-${task.range.last}")
            }
        }
    }

    fun splitFileSize(length: Int, count: Int): List<IntRange> {
        val piece = length / count
        val left = length % count

        return buildList {
            repeat(count) {
                add(piece * it until piece * (it + 1))
            }

            if (left != 0) {
                add(piece * count until length)
            }
        }
    }

    suspend fun d() = withContext(Dispatchers.IO) {
        val size = 1151577579 // 13708936
        //val file = RandomAccessFile("D:/ProjectFiles/idea/Concatenate/backend/build/tmp/a.jar", "rwd")
        //val ch = file.channel

        launch {
            splitFileSize(size, 30).forEachIndexed { index, range ->

                async(Dispatchers.IO) {
                    var url = "https://libraries.minecraft.net/com/ibm/icu/icu4j/70.1/icu4j-70.1.jar"
                    val url2 = "https://bmclapi2.bangbang93.com/maven/com/ibm/icu/icu4j/70.1/icu4j-70.1.jar"
                    url = "https://download.jetbrains.com/idea/ideaIU-2023.2.5.win.zip"
                    val k = client.get(url) {
                        headers {
                            append("Range", "bytes=${range.first}-${range.last}")
                        }
                    }
                    val time = (Clock.System.now().toString().replace(":", "-"))
                    File("D:/ProjectFiles/idea/Concatenate/backend/build/tmp/a-$time-$index.jar").writeBytes(k.readBytes())
                    println("$range -> $k")
                }

            }
            println("finished")
        }

        println("sssss")

    }

    suspend fun cl() = withContext(Dispatchers.IO) {
        async(Dispatchers.IO) {
            val url = "https://download.jetbrains.com/idea/ideaIU-2023.2.5.win.zip"
            val url2 = "https://libraries.minecraft.net/com/ibm/icu/icu4j/70.1/icu4j-70.1.jar"
            val response = client.head(url)
            val contentLength = response.headers[HttpHeaders.ContentLength]
            println("a>> $contentLength")
        }
    }
}

suspend fun ByteReadChannel.copyToFile(fileChannel: FileChannel, bufferSize: Int = 8192) {
    val buffer = ByteBuffer.allocate(bufferSize)
//    try {
        while (!isClosedForRead) {
            buffer.clear()
            readAvailable(buffer)
            buffer.flip()
            while (buffer.hasRemaining()) {
                fileChannel.write(buffer)
            }
        }

    // 由 fileChannel 提供者复制关闭 channel
//    } /*finally {
//        fileChannel.close()
//    }*/
}

class ConcatQueue<T>(private val deque: ArrayDeque<T> = ArrayDeque()): Queue<T> by deque {

    fun enqueue(element: T) {
        deque.addLast(element)
    }

    fun dequeue(): T? {
        return if (deque.isNotEmpty()) {
            deque.removeFirst()
        } else null
    }
}