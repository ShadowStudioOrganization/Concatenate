package org.shadow.studio.concatenate.backend.test

import io.ktor.utils.io.jvm.nio.*
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withLock
import org.shadow.studio.concatenate.backend.data.download.RemoteFile
import org.shadow.studio.concatenate.backend.download.ConcatQueue
import org.shadow.studio.concatenate.backend.download.ConcatenateDownloader
import org.shadow.studio.concatenate.backend.launch.MinecraftVersion
import org.shadow.studio.concatenate.backend.util.*
import java.io.File
import java.io.RandomAccessFile
import java.nio.file.Path
import java.util.concurrent.Executors
import kotlin.collections.buildList
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@OptIn(ExperimentalTime::class)
suspend fun main(): Unit = withContext(Dispatchers.IO) {
    val time = measureTime {
//        exc()
//         NormalDownloader(globalClient).f()

        val baseDir = "D:/ProjectFiles/idea/Concatenate/backend/build/tmp/repos"
        val mv = MinecraftVersion("1.17.1", File("D:/Games/aloneg/versions/1.17.1/1.17.1.json"))

        val downloader = ConcatenateDownloader().apply {
            source {
                eachAvailableLibrary(mv.profile.libraries) {
                    it.downloads?.artifact?.let { artifact ->
                        this += RemoteFile(
                            artifact.url.let { url ->
                                val origin = "https://libraries.minecraft.net/"
                                val replaced = "https://bmclapi2.bangbang93.com/maven/"
                                url.replaceFirst(origin, replaced)
                            },
                            artifact.size,
                            Path.of(baseDir, artifact.path),
                            artifact.sha1
                        )
                    }
                }
            }
        }

        downloader.download()


    }



    println("total spent: $time")
}




@OptIn(ExperimentalTime::class)
suspend fun testKtorURLSpeed(): Unit = withContext(Dispatchers.IO) {

    val fileUrl = "https://libraries.minecraft.net/com/ibm/icu/icu4j/70.1/icu4j-70.1.jar"
    val filePath = "D:/ProjectFiles/idea/Concatenate/backend/build/tmp/ddd/uru.jar"

    // NormalDownloader(globalClient).e()
    val raf = RandomAccessFile(filePath, "rwd")
    val raf2 = RandomAccessFile(filePath + "ktor", "rwd")
    raf.setLength(13708936)
    raf2.setLength(13708936)
    val dest = raf.channel
    val dest2 = raf2.channel

    val k1 = measureTime {
        urlRangedDownloadAndTransferTo(fileUrl, 128..13708936, dest)
    }

    val k2 = measureTime {
        ktorRangedDownloadAndTransferTo(fileUrl, 128..13708936, Path.of(filePath + "ktor"), 13708936L)
    }

    dest.close()
    dest2.close()

    println("k1 using time: " + k1.absoluteValue)
    println("k2 using time: " + k2.absoluteValue)
}

suspend fun exc() {
    val poolSize = 5
    val semaphore = Semaphore(5)
    val mutex = Mutex()
    val coroutineDispatcher = Executors.newFixedThreadPool(poolSize).asCoroutineDispatcher()
    val coroutineScope = CoroutineScope(coroutineDispatcher)

    val keys = buildList {
        add("whiterasbk")
        add("falasscasc")
        add("yascadawyb")
    }

    class MF(val id: String, private val data: ByteArray) {
        var position: Int = 0
        val limit: Int = data.size

        fun write(bytes: ByteArray) = bytes.forEach(::write)

        fun write(byte: Byte) {
            data[position++] = byte
        }

        override fun toString() = data.toList().toString()
    }

    val coroutinePool = List(poolSize) {
        coroutineScope.async {

            val key = keys[it % 3]

            semaphore.acquire()

            try {

                if (!mutex.isLocked) {
                    delay(1000L)
                    println(">$it ==> $key<")
                } else mutex.withLock(key) {
                    delay(1000L)
                    println(">$it ==> $key<")
                }

            } finally {
                semaphore.release()
            }

        }
    }

    coroutinePool.forEach {
        it.await()
    }

    // Shutdown the coroutine pool
    coroutineDispatcher.close()
}