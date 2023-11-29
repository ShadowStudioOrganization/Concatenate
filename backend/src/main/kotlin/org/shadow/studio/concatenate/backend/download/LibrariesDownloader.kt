package org.shadow.studio.concatenate.backend.download

import io.ktor.client.*
import org.shadow.studio.concatenate.backend.data.download.RemoteFile
import org.shadow.studio.concatenate.backend.data.profile.JsonProfile
import org.shadow.studio.concatenate.backend.data.profile.LibraryItem
import org.shadow.studio.concatenate.backend.launch.MinecraftVersion
import org.shadow.studio.concatenate.backend.util.*
import java.nio.file.Path
import java.util.concurrent.atomic.AtomicInteger
import kotlin.collections.buildList
import kotlin.io.path.absolutePathString

class LibrariesDownloader(
    private val libraries: List<LibraryItem>,
    private val librariesRoot: Path,
    officialRepositoryUrl: String = OFFICIAL_LIBRARIES_REPO_HEAD,
    poolSize: Int = DEFAULT_CONCATE_DOWNLOADER_POOL_SIZE,
    taskTTL: Int = DEFAULT_CONCATE_DOWNLOADER_TASK_TTL,
    ktorClient: HttpClient = globalClient,
    ktorBuffetSize: Long = DEFAULT_CONCATE_DOWNLOADER_KTOR_BUFFER_SIZE
) : MinecraftResourceDownloader(officialRepositoryUrl, poolSize, taskTTL, ktorClient, ktorBuffetSize) {

    constructor(
        jsonProfile: JsonProfile,
        assetDirectory: Path,
        officialRepositoryUrl: String = OFFICIAL_LIBRARIES_REPO_HEAD,
        poolSize: Int = DEFAULT_CONCATE_DOWNLOADER_POOL_SIZE,
        taskTTL: Int = DEFAULT_CONCATE_DOWNLOADER_TASK_TTL,
        ktorClient: HttpClient = globalClient,
        ktorBuffetSize: Long = DEFAULT_CONCATE_DOWNLOADER_KTOR_BUFFER_SIZE
    ) : this(jsonProfile.libraries, assetDirectory, officialRepositoryUrl, poolSize, taskTTL, ktorClient, ktorBuffetSize)

    constructor(
        version: MinecraftVersion,
        assetDirectory: Path,
        officialRepositoryUrl: String = OFFICIAL_LIBRARIES_REPO_HEAD,
        poolSize: Int = DEFAULT_CONCATE_DOWNLOADER_POOL_SIZE,
        taskTTL: Int = DEFAULT_CONCATE_DOWNLOADER_TASK_TTL,
        ktorClient: HttpClient = globalClient,
        ktorBuffetSize: Long = DEFAULT_CONCATE_DOWNLOADER_KTOR_BUFFER_SIZE
    ) : this(version.profile, assetDirectory, officialRepositoryUrl, poolSize, taskTTL, ktorClient, ktorBuffetSize)


    companion object {
        const val OFFICIAL_LIBRARIES_REPO_HEAD = "https://libraries.minecraft.net/"
    }

    init {
        repositories.addRepository("bmclapi2", Repository("https://bmclapi2.bangbang93.com/maven/"))
        repositories.addRepository("mcbbs", Repository("https://download.mcbbs.net/maven/"))
    }

    override fun getDownloadTarget(): List<RemoteFile> {

        val totalItem = libraries.availableArtifactAndClassifier().size
        val index = AtomicInteger(-1)

        return multiThreadGenerateTargets(poolName = "GetLibraryTargets") { initial: (() -> RemoteFile?) -> Unit ->
            libraries.forEachAvailableArtifactAndClassifier { artifact ->
                val local = getLocalDestination(artifact.path)
                initial {
                    ifNeedReDownloadThen(local, artifact.size, artifact.sha1, index.incrementAndGet(), totalItem) {
                        RemoteFile(urlProcess(artifact.url), artifact.size, local, artifact.sha1)
                    }
                }

            }
        }

/*        return buildList {

            libraries.forEachAvailableArtifactAndClassifier { artifact ->
                val local = getLocalDestination(artifact.path)
                ifNeedReDownloadThen(local, artifact.size, artifact.sha1) {
                    add(RemoteFile(
                        urlProcess(artifact.url),
                        artifact.size,
                        local,
                        artifact.sha1
                    ))
                }
            }

        }*/
    }

    private fun getLocalDestination(relativePath: Path): Path {
        return Path.of(librariesRoot.absolutePathString(), relativePath.toString())
    }

    private fun getLocalDestination(relativePath: String): Path {
        return getLocalDestination(Path.of(relativePath))
    }
}