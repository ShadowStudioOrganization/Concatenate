package org.shadow.studio.concatenate.backend.test

import ch.qos.logback.classic.Level
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.utils.io.jvm.nio.*
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withLock
import org.shadow.studio.concatenate.backend.adapter.JavaAdapter
import org.shadow.studio.concatenate.backend.data.download.RemoteFile
import org.shadow.studio.concatenate.backend.data.launch.MinecraftExtraJvmArguments
import org.shadow.studio.concatenate.backend.download.AssetDownloader
import org.shadow.studio.concatenate.backend.download.ConcatQueue
import org.shadow.studio.concatenate.backend.download.ConcatenateDownloader
import org.shadow.studio.concatenate.backend.download.LibrariesDownloader
import org.shadow.studio.concatenate.backend.launch.MinecraftClientConfiguration
import org.shadow.studio.concatenate.backend.launch.MinecraftClientLauncher
import org.shadow.studio.concatenate.backend.launch.MinecraftVersion
import org.shadow.studio.concatenate.backend.login.OfflineMethod
import org.shadow.studio.concatenate.backend.resolver.DirectoryLayer
import org.shadow.studio.concatenate.backend.resolver.MinecraftResourceResolver
import org.shadow.studio.concatenate.backend.resolver.NormalDirectoryLayer
import org.shadow.studio.concatenate.backend.util.*
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.io.RandomAccessFile
import java.net.URL
import java.nio.file.Path
import java.util.concurrent.Executors
import kotlin.collections.buildList
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@OptIn(ExperimentalTime::class)
suspend fun main(): Unit = withContext(Dispatchers.IO) {
    val time = measureTime {
        mc()
//        libDownload()
//        assetDownload()
    }

    println("total spent: $time")
}

suspend fun mc(): Unit = withContext(Dispatchers.IO) {
    val wd = "D:/ProjectFiles/idea/Concatenate/backend/build/run"

    val manifest = getInternalVersionManifest()
    val versionManifest = manifest.versions.find { it.id == "1.20.2" } ?: error("????")

    val versionProfile = File(wd, "versions/${versionManifest.id}/${versionManifest.id}.json").apply {
        if (!parentFile.exists()) parentFile.mkdirs()
    }

    if (!versionProfile.exists()) {
        globalClient.get(versionManifest.url).bodyAsChannel().copyToFile(versionProfile.outputStream().channel)
        globalLogger.info("fetched game profile")
    }

    val version = MinecraftVersion("1.20.2", versionProfile)
    val resolver = MinecraftResourceResolver(NormalDirectoryLayer(File(wd), version))

    val gameJar = resolver.resolveGameJar()

    if (!gameJar.exists()) {
        globalClient.get(version.profile.downloads.client.url).bodyAsChannel().copyToFile(gameJar.outputStream().channel)
        globalLogger.info("fetched game jar")
    }

    resolver.resolveAssetRoot()

    val indexesDir = resolver.resolveAssetIndexesDirectory()
    val assetIndexJson = File(indexesDir, version.profile.assetIndex.id + ".json")
    globalClient.get(version.profile.assetIndex.url).bodyAsChannel().copyToFile(assetIndexJson.outputStream().channel)

    globalLogger.info("fetched asset index")

    val task1 = async {
        AssetDownloader(
            assetManifestSource = assetIndexJson,
            assetDirectory = resolver.resolveAssetObjectsRoot().toPath()
        ).download(3)

        globalLogger.info("fetched assets")
    }

    val task2 = async {
        val librariesRoot = resolver.resolveLibrariesRoot()
        LibrariesDownloader(
            version,
            librariesRoot.toPath()
        ).download(3)

        globalLogger.info("fetched libs")
    }

    val config = MinecraftClientConfiguration(
        minecraftExtraJvmArguments = MinecraftExtraJvmArguments(
            fileEncoding = "GBK"
        )
    )

    listOf(task1, task2).awaitAll()

    val launcher = MinecraftClientLauncher(
        adapter = JavaAdapter(),
        clientCfg = config,
        workingDirectory = File(wd),
        version = version,
        loginMethod = OfflineMethod("whiterasbk")
    )

    globalLogger.info("Minecraft instance is starting")
    val instance = launcher.launch()
    val inputStream = instance.process.inputStream

    val reader = BufferedReader(InputStreamReader(inputStream))
    var line: String?
    while (reader.readLine().also { line = it } != null) {
        println(line)
    }

    val errorStream = instance.process.errorStream

    val errReader = BufferedReader(InputStreamReader(errorStream))
    var errLine: String?
    while (errReader.readLine().also { errLine = it } != null) {
        println(errLine)
    }

    val exitCode = instance.process.waitFor()
    globalLogger.info("Minecraft process exited with code: $exitCode")

}

suspend fun libDownload() {

    val baseDir = "D:/ProjectFiles/idea/Concatenate/backend/build/tmp/repos"
    val mv = MinecraftVersion("1.17.1", File("D:/Games/aloneg/versions/1.17.1/1.17.1.json"))

    val downloader = LibrariesDownloader(mv.profile.libraries, Path.of(baseDir))

    downloader.apply {
        val mLogger = LoggerFactory.getLogger("download")

        mLogger as ch.qos.logback.classic.Logger
        mLogger.level = Level.ERROR


//        setLogger(mLogger)

        callback {
            val k = it.doneBytesSoFar.toFloat() / it.totalBytes
            globalLogger.info("progress: $k")
        }
    }

    downloader.download()
}

suspend fun assetDownload() = withContext(Dispatchers.IO){
//    val assetUrl = "https://piston-meta.mojang.com/v1/packages/edc6213221c780c6f44ec687084046200189c605/5.json"
//    val input = URL(assetUrl).openConnection().getInputStream()

    val path = "D:/ProjectFiles/idea/Concatenate/backend/build/tmp/asset"
    val man = File("D:/ProjectFiles/idea/Concatenate/backend/build/tmp/5.json")
    val downloader = AssetDownloader(man, Path.of(path))

    downloader.download(5)
//    input.close()
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