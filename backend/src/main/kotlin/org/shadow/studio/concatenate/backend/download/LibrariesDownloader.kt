package org.shadow.studio.concatenate.backend.download

import io.ktor.client.*
import org.shadow.studio.concatenate.backend.data.download.RemoteFile
import org.shadow.studio.concatenate.backend.data.profile.JsonProfile
import org.shadow.studio.concatenate.backend.data.profile.LibraryItem
import org.shadow.studio.concatenate.backend.launch.MinecraftVersion
import org.shadow.studio.concatenate.backend.util.*
import java.nio.file.Path
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
        return buildList {

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

            /*libraries.forEachAvailable { lib ->
                lib.downloads?.artifact?.let { artifact ->
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

                lib.downloads?.classifiers?.let { classifiers ->
                    val artifact = when(getSystemName()) {
                        "windows" -> classifiers.nativesWindows ?: classifiers.nativesWindows64 ?: classifiers.nativesWindows32
                        "linux" -> classifiers.nativesLinux
                        "osx" -> classifiers.nativesMacos ?: classifiers.nativesOSX
                        else -> null
                    }

                    artifact?.let {
                        val local = getLocalDestination(it.path)
                        ifNeedReDownloadThen(local, it.size, it.sha1) {
                            add(RemoteFile(urlProcess(it.url), it.size, local, it.sha1))
                        }
                    }
                }
            }*/
        }
    }

    private fun getLocalDestination(relativePath: Path): Path {
        return Path.of(librariesRoot.absolutePathString(), relativePath.toString())
    }

    private fun getLocalDestination(relativePath: String): Path {
        return getLocalDestination(Path.of(relativePath))
    }
}