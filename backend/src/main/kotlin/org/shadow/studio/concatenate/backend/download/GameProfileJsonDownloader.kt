package org.shadow.studio.concatenate.backend.download

import io.ktor.client.*
import org.shadow.studio.concatenate.backend.data.launchermeta.Version
import org.shadow.studio.concatenate.backend.data.download.RemoteFile
import org.shadow.studio.concatenate.backend.util.DEFAULT_CONCATE_DOWNLOADER_KTOR_BUFFER_SIZE
import org.shadow.studio.concatenate.backend.util.globalClient
import java.nio.file.Path
import kotlin.io.path.exists

class GameProfileJsonDownloader(
    private val storeLocation: Path,
    private val source: Version,
    officialRepositoryUrl: String = OFFICIAL_JSON_PROFILE_REPO_HEAD,
    ktorClient: HttpClient = globalClient,
    ktorBuffetSize: Long = DEFAULT_CONCATE_DOWNLOADER_KTOR_BUFFER_SIZE
): UnknownLengthResourceDownloader(officialRepositoryUrl, ktorClient, ktorBuffetSize) {

    companion object {
        const val OFFICIAL_JSON_PROFILE_REPO_HEAD = "https://piston-data.mojang.com/"
    }

    init {
        repositories.addRepository("bmclapi2", Repository("https://bmclapi2.bangbang93.com/"))
        repositories.addRepository("mcbbs", Repository("https://download.mcbbs.net/"))
    }

    override fun getDownloadTarget(): List<RemoteFile> {
        return if (storeLocation.exists()) emptyList() else listOf(RemoteFile(urlProcess(source.url), 1L, storeLocation, source.sha1))
    }
}