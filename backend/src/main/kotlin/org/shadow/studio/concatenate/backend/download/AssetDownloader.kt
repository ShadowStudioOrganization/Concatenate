package org.shadow.studio.concatenate.backend.download

import io.ktor.client.*
import org.shadow.studio.concatenate.backend.data.download.RemoteFile
import org.shadow.studio.concatenate.backend.util.getAssetObjectsFromString
import org.shadow.studio.concatenate.backend.util.globalClient
import java.io.File
import java.io.InputStream
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.absolutePathString

class AssetDownloader(
    private val assetManifestSource: String,
    private val assetDirectory: Path,
    officialRepositoryUrl: String = OFFICIAL_ASSET_REPO_HEAD,
    poolSize: Int = DEFAULT_CONCATE_DOWNLOADER_POOL_SIZE,
    taskTTL: Int = DEFAULT_CONCATE_DOWNLOADER_TASK_TTL,
    ktorClient: HttpClient = globalClient,
    ktorBuffetSize: Long = DEFAULT_CONCATE_DOWNLOADER_KTOR_BUFFER_SIZE
) : MinecraftResourceDownloader(officialRepositoryUrl, poolSize, taskTTL, ktorClient, ktorBuffetSize) {

    constructor(
        assetManifestSource: InputStream,
        assetDirectory: Path,
        officialRepositoryUrl: String = OFFICIAL_ASSET_REPO_HEAD,
        poolSize: Int = DEFAULT_CONCATE_DOWNLOADER_POOL_SIZE,
        taskTTL: Int = DEFAULT_CONCATE_DOWNLOADER_TASK_TTL,
        ktorClient: HttpClient = globalClient,
        ktorBuffetSize: Long = DEFAULT_CONCATE_DOWNLOADER_KTOR_BUFFER_SIZE
    ) : this(assetManifestSource.bufferedReader().readText(), assetDirectory, officialRepositoryUrl, poolSize, taskTTL, ktorClient, ktorBuffetSize)

    constructor(
        assetManifestSource: File,
        assetDirectory: Path,
        officialRepositoryUrl: String = OFFICIAL_ASSET_REPO_HEAD,
        poolSize: Int = DEFAULT_CONCATE_DOWNLOADER_POOL_SIZE,
        taskTTL: Int = DEFAULT_CONCATE_DOWNLOADER_TASK_TTL,
        ktorClient: HttpClient = globalClient,
        ktorBuffetSize: Long = DEFAULT_CONCATE_DOWNLOADER_KTOR_BUFFER_SIZE
    ) : this(assetManifestSource.readText(), assetDirectory, officialRepositoryUrl, poolSize, taskTTL, ktorClient, ktorBuffetSize)

    companion object {
        const val OFFICIAL_ASSET_REPO_HEAD = "https://resources.download.minecraft.net/"
    }

    override fun getDownloadTarget(): List<RemoteFile> {

        val assetObjects = getAssetObjectsFromString(assetManifestSource)

        return buildList {
            for ((_, info) in assetObjects.fields()) {
                val hash = info.get("hash").textValue()
                val size = info.get("size").longValue()
                val subPath = hash.substring(0..1) + "/" + hash

                val local = Paths.get(assetDirectory.absolutePathString(), subPath)
                ifNeedReDownloadThen(local, size, hash) {
                    add(RemoteFile(currentRepository.wrap(subPath), size, local, hash))
                }

            }
        }
    }
}