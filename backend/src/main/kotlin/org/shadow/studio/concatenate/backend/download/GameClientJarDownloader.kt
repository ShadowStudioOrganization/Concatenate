package org.shadow.studio.concatenate.backend.download

import io.ktor.client.*
import org.shadow.studio.concatenate.backend.data.download.DownloadTask
import org.shadow.studio.concatenate.backend.data.download.RemoteFile
import org.shadow.studio.concatenate.backend.data.profile.GameDownloadItem
import org.shadow.studio.concatenate.backend.data.profile.GameDownloads
import org.shadow.studio.concatenate.backend.util.globalClient
import java.nio.file.Path
import kotlin.io.path.exists

class GameClientJarDownloader(
    private val storeLocation: Path,
    private val source: GameDownloadItem,
    private val piece: Int = 4,
    officialRepositoryUrl: String = OFFICIAL_GAME_CLIENT_JAR_REPO_HEAD,
    poolSize: Int = piece,
    taskTTL: Int = DEFAULT_CONCATE_DOWNLOADER_TASK_TTL,
    ktorClient: HttpClient = globalClient,
    ktorBuffetSize: Long = DEFAULT_CONCATE_DOWNLOADER_KTOR_BUFFER_SIZE
): MinecraftResourceDownloader(officialRepositoryUrl, poolSize, taskTTL, ktorClient, ktorBuffetSize) {

    companion object {
        const val OFFICIAL_GAME_CLIENT_JAR_REPO_HEAD = "https://piston-data.mojang.com/"
    }

    init {
        repositories.addRepository("bmclapi2", Repository("https://bmclapi2.bangbang93.com/"))
        repositories.addRepository("mcbbs", Repository("https://download.mcbbs.net/"))
    }

    override fun getDownloadTarget(): List<RemoteFile> {

        val isSkipped = source.sha1?.let { sha1 ->
            !needReDownload(storeLocation, source.size, sha1)
        } ?: storeLocation.exists()

        return if (isSkipped) emptyList() else listOf(RemoteFile(urlProcess(source.url), source.size, storeLocation, source.sha1))
    }

    override fun deliverTasks(remoteFiles: List<RemoteFile>, taskMaxBufferSize: Long): List<DownloadTask> {
        if (remoteFiles.isEmpty()) return emptyList()

        val jar = remoteFiles.first()
        val ranges = mutableListOf<LongRange>()
        return splitFileSize(jar.size, piece).map {
            DownloadTask(it.also { ranges += it }, jar, taskTTL)
        }.also {
            jar.splitsRanges = ranges
        }
    }
}