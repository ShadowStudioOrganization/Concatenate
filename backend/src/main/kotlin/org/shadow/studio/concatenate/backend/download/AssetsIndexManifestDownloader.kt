package org.shadow.studio.concatenate.backend.download

import io.ktor.client.*
import org.shadow.studio.concatenate.backend.data.download.RemoteFile
import org.shadow.studio.concatenate.backend.data.profile.AssetIndex
import org.shadow.studio.concatenate.backend.resolver.DirectoryLayer
import org.shadow.studio.concatenate.backend.util.globalClient
import java.nio.file.Path

class AssetsIndexManifestDownloader(
    private val source: AssetIndex,
    private val storeDestination: Path,
    officialRepositoryUrl: String = OFFICIAL_ASSET_INDEX_REPO_HEAD,
    poolSize: Int = 1,
    taskTTL: Int = 3,
    ktorClient: HttpClient = globalClient,
    ktorBuffetSize: Long = DEFAULT_CONCATE_DOWNLOADER_KTOR_BUFFER_SIZE
) : MinecraftResourceDownloader(officialRepositoryUrl, poolSize, taskTTL, ktorClient, ktorBuffetSize) {

    companion object {
        const val OFFICIAL_ASSET_INDEX_REPO_HEAD = "https://piston-meta.mojang.com/"
    }

    init {
        repositories.addRepository("bmclapi2", Repository("https://bmclapi2.bangbang93.com/"))
        repositories.addRepository("mcbbs", Repository("https://download.mcbbs.net/"))
    }

    override fun getDownloadTarget(): List<RemoteFile> {
        return ifNeedReDownloadThen(storeDestination, source.size, source.sha1) {
            listOf(RemoteFile(urlProcess(source.url), source.size, storeDestination, source.sha1))
        } ?: emptyList()
    }
}