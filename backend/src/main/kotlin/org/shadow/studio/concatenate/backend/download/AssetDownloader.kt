package org.shadow.studio.concatenate.backend.download

import io.ktor.client.*
import org.shadow.studio.concatenate.backend.data.download.RemoteFile
import org.shadow.studio.concatenate.backend.util.*
import java.io.File
import java.io.InputStream
import java.nio.file.Path
import java.nio.file.Paths
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import kotlin.collections.buildList
import kotlin.io.path.absolutePathString

class AssetDownloader(
    private val assetManifestSource: String,
    private val assetObjectsDirectory: Path,
    officialRepositoryUrl: String = OFFICIAL_ASSET_REPO_HEAD,
    poolSize: Int = DEFAULT_CONCATE_DOWNLOADER_POOL_SIZE,
    taskTTL: Int = DEFAULT_CONCATE_DOWNLOADER_TASK_TTL,
    ktorClient: HttpClient = globalClient,
    ktorBuffetSize: Long = DEFAULT_CONCATE_DOWNLOADER_KTOR_BUFFER_SIZE
) : MinecraftResourceDownloader(officialRepositoryUrl, poolSize, taskTTL, ktorClient, ktorBuffetSize) {

    constructor(
        assetManifestSource: InputStream,
        assetObjectsDirectory: Path,
        officialRepositoryUrl: String = OFFICIAL_ASSET_REPO_HEAD,
        poolSize: Int = DEFAULT_CONCATE_DOWNLOADER_POOL_SIZE,
        taskTTL: Int = DEFAULT_CONCATE_DOWNLOADER_TASK_TTL,
        ktorClient: HttpClient = globalClient,
        ktorBuffetSize: Long = DEFAULT_CONCATE_DOWNLOADER_KTOR_BUFFER_SIZE
    ) : this(assetManifestSource.bufferedReader().readText(), assetObjectsDirectory, officialRepositoryUrl, poolSize, taskTTL, ktorClient, ktorBuffetSize)

    constructor(
        assetManifestSource: File,
        assetObjectsDirectory: Path,
        officialRepositoryUrl: String = OFFICIAL_ASSET_REPO_HEAD,
        poolSize: Int = DEFAULT_CONCATE_DOWNLOADER_POOL_SIZE,
        taskTTL: Int = DEFAULT_CONCATE_DOWNLOADER_TASK_TTL,
        ktorClient: HttpClient = globalClient,
        ktorBuffetSize: Long = DEFAULT_CONCATE_DOWNLOADER_KTOR_BUFFER_SIZE
    ) : this(assetManifestSource.readText(), assetObjectsDirectory, officialRepositoryUrl, poolSize, taskTTL, ktorClient, ktorBuffetSize)

    val checkerPoolSize: Int = 64

    companion object {
        const val OFFICIAL_ASSET_REPO_HEAD = "https://resources.download.minecraft.net/"
    }

    init {
        repositories.addRepository("bmclapi2", Repository("https://bmclapi2.bangbang93.com/assets/"))
        repositories.addRepository("mcbbs", Repository("https://download.mcbbs.net/assets/"))
    }



    override fun getDownloadTarget(): List<RemoteFile> {

        val assetObjects = getAssetObjectsFromString(assetManifestSource)
        val totalItem = assetObjects.size()
        val index = AtomicInteger(-1)

        return multiThreadGenerateTargets(poolName = "GetAssetTargets") { initial: (() -> RemoteFile?) -> Unit ->
            for ((_, info) in assetObjects.fields()) {
                val hash = info.get("hash").textValue()
                val size = info.get("size").longValue()
                val subPath = hash.substring(0..1) + "/" + hash
                val local = Paths.get(assetObjectsDirectory.absolutePathString(), subPath)

                initial {
                    ifNeedReDownloadThen(local, size, hash, index.incrementAndGet(), totalItem) {
                        RemoteFile(currentRepository.wrap(subPath), size, local, hash)
                    }
                }
            }
        }


    /*    val totalItem = assetObjects.size()
        val index = AtomicInteger(-1)
        val pool = Executors.newFixedThreadPool(24)

        val futures = buildList<Future<RemoteFile?>> {
            for ((_, info) in assetObjects.fields()) {
                val hash = info.get("hash").textValue()
                val size = info.get("size").longValue()
                val subPath = hash.substring(0..1) + "/" + hash
                val local = Paths.get(assetObjectsDirectory.absolutePathString(), subPath)

                this += pool.submit<RemoteFile?> {
                    ifNeedReDownloadThen(local, size, hash, index.incrementAndGet(), totalItem) {
                        RemoteFile(currentRepository.wrap(subPath), size, local, hash)
                    }
                }
            }
        }

        pool.shutdown()
        pool.awaitTermination(10, TimeUnit.MINUTES)

        return futures.mapNotNull { it.get() }*/

    }
}