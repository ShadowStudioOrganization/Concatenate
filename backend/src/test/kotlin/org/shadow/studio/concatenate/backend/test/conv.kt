package org.shadow.studio.concatenate.backend.test

import ch.qos.logback.classic.Level
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.*
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withLock
import org.shadow.studio.concatenate.backend.adapter.JavaAdapter
import org.shadow.studio.concatenate.backend.data.launch.MinecraftExtraJvmArguments
import org.shadow.studio.concatenate.backend.download.*
import org.shadow.studio.concatenate.backend.launch.MinecraftClientConfiguration
import org.shadow.studio.concatenate.backend.launch.MinecraftClientLauncher
import org.shadow.studio.concatenate.backend.launch.MinecraftVersion
import org.shadow.studio.concatenate.backend.login.OfflineMethod
import org.shadow.studio.concatenate.backend.resolveBackendBuildPath
import org.shadow.studio.concatenate.backend.resolver.MinecraftResourceResolver
import org.shadow.studio.concatenate.backend.resolver.NormalDirectoryLayer
import org.shadow.studio.concatenate.backend.util.*
import org.shadow.studio.concatenate.backend.util.globalLogger
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.io.RandomAccessFile
import java.nio.file.Path
import java.util.concurrent.Executors
import kotlin.collections.buildList
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@OptIn(ExperimentalTime::class)
suspend fun main(): Unit = withContext(Dispatchers.IO) {
    val time = measureTime {
//        launcherMetaDownload()
        mc()
    }

    println("total spent: $time")
}

suspend fun launcherMetaDownload() {
    val location = resolveBackendBuildPath("tmp/version_manifest.json")
    val downloader = LauncherMetaManifestDownloader(location.toPath())
    downloader.download()
}

@OptIn(ExperimentalTime::class)
suspend fun mc(): Unit = withContext(Dispatchers.IO) {

    val ktorClient = HttpClient(OkHttp) {
        engine {
            config {
                followRedirects(true)
            }
        }

        install(HttpTimeout) {
//            requestTimeoutMillis = 1000
            socketTimeoutMillis = 1000
            connectTimeoutMillis = 700
        }

        BrowserUserAgent()
    }


    val versionName = "happy"
    val versionId = "1.20.2"
    val workingDir = resolveBackendBuildPath("run2")
    val launcherMeta = getInternalLauncherMetaManifest()
    val versionManifest = launcherMeta.versions.find { it.id == versionId } ?: error("????")
    val layer = NormalDirectoryLayer(workingDir, false, versionName)

    val profileDownloader = GameProfileJsonDownloader(layer.getMinecraftJsonProfilePosition().toPath(), versionManifest)
    profileDownloader.download()

    val mcVersion = layer.newMinecraftVersion()

    val resolver = MinecraftResourceResolver(layer, mcVersion)

    val assetIndexDownloader =
        AssetsIndexManifestDownloader(mcVersion.profile.assetIndex, resolver.resolveAssetIndexJsonFile().toPath())
    assetIndexDownloader.download()

    val clientInfo = mcVersion.profile.downloads.client
    val jarDownloader = GameClientJarDownloader(layer.getMinecraftJarPosition().toPath(), clientInfo)

    val assetDownloader = AssetDownloader(
        assetManifestSource = resolver.resolveAssetIndexJsonFile(),
        assetObjectsDirectory = resolver.resolveAssetObjectsRoot().toPath(),
        poolSize = 128,
        ktorClient = ktorClient
    )

    val libDownloader = LibrariesDownloader(mcVersion, resolver.resolveLibrariesRoot().toPath(), poolSize = 18, ktorClient = ktorClient)

    val downloadUsingTime = measureTime {
        listOf(
            jarDownloader,
            assetDownloader,
            libDownloader
        ).map {
            async {
                it.autoSwitchRepository = true
                it.useRepository("mcbbs")
                it.download(3)
            }
        }.awaitAll()
    }


    val config = MinecraftClientConfiguration(
        minecraftExtraJvmArguments = MinecraftExtraJvmArguments(
            fileEncoding = "GBK"
        )
    )

    val launcher = MinecraftClientLauncher(
        adapter = JavaAdapter(),
        clientCfg = config,
        workingDirectory = workingDir,
        version = mcVersion,
        loginMethod = OfflineMethod("whiterasbk")
    )

    globalLogger.info("download using time: $downloadUsingTime")
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

suspend fun assetDownload() = withContext(Dispatchers.IO) {
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