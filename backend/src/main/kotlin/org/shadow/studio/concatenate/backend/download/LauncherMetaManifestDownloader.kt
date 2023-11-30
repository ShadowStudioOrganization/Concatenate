package org.shadow.studio.concatenate.backend.download

import io.ktor.client.*
import org.shadow.studio.concatenate.backend.data.download.RemoteFile
import org.shadow.studio.concatenate.backend.util.DEFAULT_CONCATE_DOWNLOADER_KTOR_BUFFER_SIZE
import org.shadow.studio.concatenate.backend.util.globalClient
import java.nio.file.Path

class LauncherMetaManifestDownloader(
    private val storeLocation: Path,
    officialRepositoryUrl: String = OFFICIAL_LAUNCHER_META_REPO_HEAD,
    ktorClient: HttpClient = globalClient,
    ktorBuffetSize: Long = DEFAULT_CONCATE_DOWNLOADER_KTOR_BUFFER_SIZE
) : UnknownLengthResourceDownloader(
    officialRepositoryUrl = officialRepositoryUrl,
    poolSize = 1,
    taskTTL = 3,
    ktorClient = ktorClient,
    ktorBuffetSize = ktorBuffetSize
) {

    private val url = "http://launchermeta.mojang.com/mc/game/version_manifest_v2.json"

    companion object {
        const val OFFICIAL_LAUNCHER_META_REPO_HEAD = "http://launchermeta.mojang.com/"
    }

    init {
        repositories.addRepository("bmclapi2", Repository("https://bmclapi2.bangbang93.com/"))
        repositories.addRepository("mcbbs", Repository("https://download.mcbbs.net/"))
    }

    override fun getDownloadTarget(): List<RemoteFile> {
        return listOf(RemoteFile(
            urlProcess(url), 1L, storeLocation
        ))
    }
}