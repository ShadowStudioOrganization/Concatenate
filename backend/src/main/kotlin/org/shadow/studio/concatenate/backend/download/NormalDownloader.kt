package org.shadow.studio.concatenate.backend.download

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.datetime.Clock
import org.shadow.studio.concatenate.backend.data.download.DownloadTask
import org.shadow.studio.concatenate.backend.data.download.RemoteFile
import org.shadow.studio.concatenate.backend.launch.MinecraftVersion
import org.shadow.studio.concatenate.backend.util.*
import org.shadow.studio.concatenate.backend.util.globalLogger
import java.io.File
import java.io.RandomAccessFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.concurrent.Executors
import kotlin.collections.buildList
import kotlin.io.path.exists

//class NormalDownloader(val client: HttpClient = HttpClient(OkHttp) {
//    engine {
//
//
//
////        proxy = ProxyBuilder.socks("127.0.0.1", 7890)
//        proxy = ProxyBuilder.http("http://127.0.0.1:7890")
//        config {
//            followRedirects(true)
//            proxy(ProxyBuilder.http("http://127.0.0.1:7890"))
////            proxy(ProxyBuilder.socks("127.0.0.1", 7890))
//        }
//    }
//
//    defaultRequest {
//
//    }
//
//}) {
//
//    fun deliverTasks(remoteFiles: List<RemoteFile>, taskMaxBufferSize: Int): List<DownloadTask> {
//        return buildList<DownloadTask> {
//            remoteFiles.forEach { file ->
//                if (file.size / taskMaxBufferSize == 0) {
//                    // 单次可运完
//                    this += DownloadTask(0 until file.size, file.localDestination, file.url, file.size.toLong())
//                } else {
//                    // 分多次运
//                    this += splitFileSize(file.size, file.size / taskMaxBufferSize)
//                        .map { DownloadTask(it, file.localDestination, file.url, file.size.toLong()) }
//                }
//            }
//        }
//
//    }
//
//
//    suspend fun f() = withContext(Dispatchers.IO) {
//        val poolSize = 50
//        val coroutineDispatcher = Executors.newFixedThreadPool(poolSize).asCoroutineDispatcher()
//        val coroutineScope = CoroutineScope(coroutineDispatcher)
//
//        val baseDir = "D:/ProjectFiles/idea/Concatenate/backend/build/tmp/repos"
//
//        val remoteFiles = buildList {
//            eachAvailableLibrary(
//                MinecraftVersion("1.17.1", File("D:/Games/aloneg/versions/1.17.1/1.17.1.json")).profile.libraries
//            ) {
//                it.downloads?.artifact?.let { artifact->
//                    add(
//                        RemoteFile(
//                        artifact.url.let { url->
//                            val origin = "https://libraries.minecraft.net/"
//                            val replaced = "https://bmclapi2.bangbang93.com/maven/"
//                            url.replaceFirst(origin, replaced)
//                        },
//                        artifact.size.toInt(),
//                        Paths.get(baseDir, artifact.path),
//                        artifact.sha1
//                    )
//                    )
//                }
//            }
//        }
//
//        remoteFiles.forEach {
//            val parentPath = it.localDestination.parent
//            if (!parentPath.exists()) {
//                Files.createDirectories(parentPath)
//                globalLogger.info("created dir at: $parentPath")
//            }
//        }
//
//        var taskBufferSize = remoteFiles.maxOf { it.size } // 128 * 1024
////        taskBufferSize = remoteFiles.map { it.size }.average().toInt()
////        taskBufferSize = remoteFiles.minOf { it.size }
//
//        globalLogger.info("set task buffer size to $taskBufferSize")
//
//        val tasks = deliverTasks(remoteFiles, taskBufferSize)
//        val queue = ConcatQueue<DownloadTask>()
//        val failedTaskQueue = ConcatQueue<DownloadTask>()
//
//        tasks.forEachIndexed { index, downloadTask ->
//            globalLogger.info("enqueued task $index ==> $downloadTask")
//            queue.enqueue(downloadTask)
//        }
//
//        val coroutinePool = List(poolSize) {
//            coroutineScope.async {
//                while (true) {
//                    val task = queue.dequeue() ?: break
//                    try {
//                        ktorRangedDownloadAndTransferTo(
//                            task.url,
//                            task.range,
//                            task.localDestination,
//                            task.originalFileSize,
//                            256 * 1024L,
//                            client
//                        )
//                        globalLogger.info("coroutine-$it downloaded range: ${task.range} sourceUrl: ${task.url} to local: ${task.localDestination}")
//                    } catch (e: Throwable) {
//                        globalLogger.error("task error occurred, reason: $e")
//                        if (task.ttl > 0) {
//                            queue.enqueue(task.apply { this.ttl -- })
//                            globalLogger.error("task with ttl: ${task.ttl} re-enqueued, fully info: $task")
//                        } else {
//                            failedTaskQueue.enqueue(task)
//                            globalLogger.error("task is failed, loaded to failedTaskQueue, fully info: $task")
//                        }
//                    }
//                }
//            }
//        }
//
//        coroutinePool.forEach {
//            it.await()
//        }
//
//        // Shutdown the coroutine pool
//        coroutineDispatcher.close()
//
//        globalLogger.info("failed tasks:")
//        failedTaskQueue.forEach(::println)
//
//        globalLogger.info("checksum")
//
//        remoteFiles.forEach { file ->
//            file.sha1?.let { sha1 ->
//                if (calculateSHA1(file.localDestination.toFile()) != sha1) {
//                    globalLogger.error("checksum of ${file.localDestination} failed, realSize: ${file.localDestination.toFile().length()}, expected: ${file.size}")
//                }
//            }
//        }
//
//        globalLogger.info("compare size: ")
//        val expectTotalSize = remoteFiles.sumOf { it.size }
//        val realTotalSize = remoteFiles.sumOf { it.localDestination.toFile().length() }
//
//        globalLogger.info("compare size: $expectTotalSize vs $realTotalSize")
//
//    }
//
//
//    suspend fun e() {
//
//        val lbs = listOf(
//            RemoteFile(
//            "https://download.jetbrains.com/idea/ideaIU-2023.2.5.win.zip", 1151577579,
//            Paths.get("D:/ProjectFiles/idea/Concatenate/backend/build/tmp/ddd/idea.zip"), "")
//        )
//
//        val ts = deliverTasks(lbs, 50 * 1024 * 1024)
//        val queue = ConcatQueue<DownloadTask>()
//        val almostFailedTask = ConcatQueue<DownloadTask>()
//
//        ts.forEachIndexed { index, downloadTask ->
//            println("$index ==> $downloadTask")
//            queue.enqueue(downloadTask)
//        }
//
//        val rafPool = mutableMapOf<String, RandomAccessFile>()
//
//
//        while (true) {
//            val task = queue.dequeue() ?: break
//            val data: HttpResponse = downloadRangedFile(task)
//            println("cur data: $data")
//
//            val key = task.localDestination.normalize().toString()
//            val raf = rafPool[key] ?: run {
//                RandomAccessFile(key, "rwd").apply {
//                    rafPool[key] = this
//                }
//            }
//
//            // 将 RandomAccessFile 定位到指定位置
//            raf.seek(task.range.first.toLong())
//
//            println("writing " + task.range.first.toLong())
//
//            val bodyAsChannel = data.bodyAsChannel()
//
//            println(bodyAsChannel)
//
////            data.bodyAsChannel().copyToFile(raf.channel, 1024 * 50)
////            raf.channel.transferFrom((data.bodyAsChannel() as ByteChannel), 0, 0)
//        }
//
//        rafPool.forEach { (_, raf) ->
//            raf.close()
//        }
//
//    }
//
//    suspend fun c() {
//
//        val baseDir = "D:/ProjectFiles/idea/Concatenate/backend/build/tmp/repos"
//
//
//        val lbs = buildList<RemoteFile> {
//            eachAvailableLibrary(
//                MinecraftVersion("1.17.1", File("D:/Games/aloneg/versions/1.17.1/1.17.1.json"))
//                    .profile.libraries
//            ) {
//                it.downloads?.artifact?.let { artifact->
//                    add(RemoteFile(artifact.url, artifact.size.toInt(), Paths.get(baseDir, artifact.path), artifact.sha1))
//                }
//            }
//        }
//
//
//        lbs.forEach {
////            val p = Paths.get(baseDir, it.localDestination.toString()).parent
//            val p = it.localDestination.parent
//            if (!p.exists()) {
//                Files.createDirectories(p)
//                globalLogger.info("created dir at: $p")
//            }
//        }
//
//        val ts = deliverTasks(lbs, 128 * 1024)
//        val queue = ConcatQueue<DownloadTask>()
//        val almostFailedTask = ConcatQueue<DownloadTask>()
//
//        ts.forEachIndexed { index, downloadTask ->
//            println("$index ==> $downloadTask")
//            queue.enqueue(downloadTask)
//        }
//
//        val poolSize = 100
//        val mutex = Mutex() // 创建一个 Mutex 实例
////        val semaphore = Semaphore(poolSize)
//        val coroutineDispatcher = Executors.newFixedThreadPool(poolSize).asCoroutineDispatcher()
//        val coroutineScope = CoroutineScope(coroutineDispatcher)
//
//        val rafPool = mutableMapOf<String, RandomAccessFileWithState>()
//
//        val coroutinePool = List(poolSize) {
//            coroutineScope.async {
//                while (true) {
////                    semaphore.acquire() // 获取信号量，限制并发数
//                    val task = queue.dequeue() ?: break
//                    try {
////                        val data: HttpResponse = downloadRangedFile(task)
//                        println("running in coroutine $it")
//
//                        val key = task.localDestination.normalize().toString()
//
//                        val raf = rafPool[key] ?: run {
//                            RandomAccessFileWithState(key, "rwd", true).apply {
//                                rafPool[key] = this
//                                setLength(task.originalFileSize)
//                            }
//                        }
//
////                        mutex.withLock(key) {
//                            println("$it is copying files ranging: ${task.range}, from: ${task.url} to ${task.localDestination}")
//                            ktorRangedDownloadAndTransferTo(task.url, task.range, task.localDestination, task.originalFileSize)
////                            urlRangedDownloadAndTransferTo(task.url, task.range, raf.channel)
////                        }
//
//
//
//
//
//                        // 将 RandomAccessFile 定位到指定位置
////                        raf.seek(task.range.first.toLong())
//
////                        val raf = RandomAccessFile(File(baseDir, task.localDestination.toString()), "rwd")
////
////                        val channel = data.bodyAsChannel()
////                        val bufferSize = 1024 * 128 // 根据需要调整缓冲区大小
////                        channel.copyToFile(raf.channel, bufferSize)
////                        println("copied piece: ${task.range} in ${task.localDestination}")
////
//
////
////                        // 创建 ByteBuffer 作为缓冲区
////                        val buffer = ByteBuffer.allocate(bufferSize)
//
//                        // 创建一个通道，用于从数据通道读取并写入 RandomAccessFile
////                        val transferChannel = Channel<Int>()
//
//                        // 启动协程，将数据从数据通道传输到 RandomAccessFile
////                        var totalBytesTransferred = 0
////                        while (true) {
////                            buffer.clear()
////                            val bytesRead = channel.readAvailable(buffer)
////                            if (bytesRead == -1) break // 读取完毕
////                            println("wir")
////                            buffer.flip()
////                            val bytesWritten = raf.channel.write(buffer)
////                            totalBytesTransferred += bytesWritten
////                            transferChannel.send(bytesWritten)
////                        }
////                        transferChannel.close()
//                    } catch (e: Exception) {
//                        // Handle the exception (e.g., log it)
//                        println("Exception occurred: ${e.message} $e")
//                        // Re-add the task to the queue for retry
//                        if (task.ttl < 0) {
//                            task.isFailed = true
//                            almostFailedTask.enqueue(task)
//                        } else {
//                            queue.enqueue(task.apply {
//                                ttl -= 1
//                            })
//                        }
//                    } finally {
////                        semaphore.release() // 释放信号量
//                    }
//                }
//
//            }
//        }
//
//        // Wait for all tasks to complete
//        coroutinePool.forEach {
//            it.await()
//        }
//
//        // Shutdown the coroutine pool
//        coroutineDispatcher.close()
//
//        rafPool.forEach { (_, raf) ->
//            raf.close()
//        }
//
//        val rts = lbs.sumOf { it.size }
//        val nrts = lbs.sumOf { it.localDestination.toFile().length() }
//        lbs.forEach {
//            if (it.localDestination.toFile().length().toInt() != it.size || calculateSHA1(it.localDestination.toFile()) != it.sha1) {
//                println("${it.localDestination} is bad, size: ${it.size}, real: ${it.localDestination.toFile().length()}")
//            }
//        }
//
//        println("$rts vs $nrts")
//
//
//    }
//
//
//    suspend fun downloadRangedFile(task: DownloadTask): HttpResponse {
//        return client.get(task.url) {
//            headers {
//                append("Range", "bytes=${task.range.first}-${task.range.last}")
//            }
//        }
//    }
//
//    class RandomAccessFileWithState(file: File, mode: String, var isProcessing: Boolean) : RandomAccessFile(file, mode) {
//        constructor(path: String, mode: String, isProcessing: Boolean) : this(File(path), mode, isProcessing)
//        constructor(path: Path, mode: String, isProcessing: Boolean) : this(path.toFile(), mode, isProcessing)
//    }
//
//    fun splitFileSize(length: Int, count: Int): List<IntRange> {
//        val piece = length / count
//        val left = length % count
//
//        return buildList {
//            repeat(count) {
//                add(piece * it until piece * (it + 1))
//            }
//
//            if (left != 0) {
//                add(piece * count until length)
//            }
//        }
//    }
//
//    suspend fun d() = withContext(Dispatchers.IO) {
//        val size = 1151577579 // 13708936
//        //val file = RandomAccessFile("D:/ProjectFiles/idea/Concatenate/backend/build/tmp/a.jar", "rwd")
//        //val ch = file.channel
//
//        launch {
//            splitFileSize(size, 30).forEachIndexed { index, range ->
//
//                async(Dispatchers.IO) {
//                    var url = "https://libraries.minecraft.net/com/ibm/icu/icu4j/70.1/icu4j-70.1.jar"
//                    val url2 = "https://bmclapi2.bangbang93.com/maven/com/ibm/icu/icu4j/70.1/icu4j-70.1.jar"
//                    url = "https://download.jetbrains.com/idea/ideaIU-2023.2.5.win.zip"
//                    val k = client.get(url) {
//                        headers {
//                            append("Range", "bytes=${range.first}-${range.last}")
//                        }
//                    }
//                    val time = (Clock.System.now().toString().replace(":", "-"))
//                    File("D:/ProjectFiles/idea/Concatenate/backend/build/tmp/a-$time-$index.jar").writeBytes(k.readBytes())
//                    println("$range -> $k")
//                }
//
//            }
//            println("finished")
//        }
//
//        println("sssss")
//
//    }
//
//    suspend fun cl() = withContext(Dispatchers.IO) {
//        async(Dispatchers.IO) {
//            val url = "https://download.jetbrains.com/idea/ideaIU-2023.2.5.win.zip"
//            val url2 = "https://libraries.minecraft.net/com/ibm/icu/icu4j/70.1/icu4j-70.1.jar"
//            val response = client.head(url)
//            val contentLength = response.headers[HttpHeaders.ContentLength]
//            println("a>> $contentLength")
//        }
//    }
//}



