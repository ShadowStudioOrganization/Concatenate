package org.shadow.studio.concatenate.backend.download

import io.ktor.client.*
import org.shadow.studio.concatenate.backend.data.download.RemoteFile
import org.shadow.studio.concatenate.backend.util.*
import java.io.File
import java.io.InputStream
import java.nio.file.Path
import java.nio.file.Paths
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

    companion object {
        const val OFFICIAL_ASSET_REPO_HEAD = "https://resources.download.minecraft.net/"
    }

    init {
        repositories.addRepository("bmclapi2", Repository("https://bmclapi2.bangbang93.com/assets/"))
        repositories.addRepository("mcbbs", Repository("https://download.mcbbs.net/assets/"))
    }

    override fun getDownloadTarget(): List<RemoteFile> {

        val assetObjects = getAssetObjectsFromString(assetManifestSource)

        return buildList {
            for ((_, info) in assetObjects.fields()) {
                val hash = info.get("hash").textValue()
                val size = info.get("size").longValue()
                val subPath = hash.substring(0..1) + "/" + hash

                val local = Paths.get(assetObjectsDirectory.absolutePathString(), subPath)
                ifNeedReDownloadThen(local, size, hash) {
                    add(RemoteFile(currentRepository.wrap(subPath), size, local, hash))
                }

            }
        }
    }
}