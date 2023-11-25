package org.shadow.studio.concatenate.backend.download

import io.ktor.client.*
import org.shadow.studio.concatenate.backend.data.download.RemoteFile
import org.shadow.studio.concatenate.backend.data.profile.JsonProfile
import org.shadow.studio.concatenate.backend.data.profile.LibraryItem
import org.shadow.studio.concatenate.backend.launch.MinecraftVersion
import org.shadow.studio.concatenate.backend.util.forEachAvailable
import org.shadow.studio.concatenate.backend.util.globalClient
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.absolutePathString

class LibrariesDownloader(
    private val libraries: List<LibraryItem>,
    private val librariesRoot: Path,
    poolSize: Int = 64,
    taskTTL: Int = 7,
    ktorClient: HttpClient = globalClient,
    ktorBuffetSize: Long = 256 * 1024
) : ConcatenateDownloader(poolSize, taskTTL, ktorClient, ktorBuffetSize) {

    constructor(jsonProfile: JsonProfile, librariesRoot: Path) : this(jsonProfile.libraries, librariesRoot)
    constructor(version: MinecraftVersion, librariesRoot: Path) : this(version.profile, librariesRoot)

    private var currentRepository: String = repositories.getOrElse("official") { error("official repository is not find") }

    companion object {
        private const val MINECRAFT_OFFICIAL_LIBRARY_REPOSITORY_HEAD = "https://libraries.minecraft.net/"

        private val repositories: MutableMap<String, String> = mutableMapOf<String, String>().apply {
            put("official", MINECRAFT_OFFICIAL_LIBRARY_REPOSITORY_HEAD)
            put("bmclapi", "https://bmclapi2.bangbang93.com/maven/")
        }
    }

    fun useRepository(key: String) {
        currentRepository = repositories.getOrElse(key) { error("$key repository is not defined") }
    }

    fun addRepository(key: String, head: String) {
        repositories[key] = head
    }

    private fun urlProcess(url: String): String {
        return if (currentRepository != MINECRAFT_OFFICIAL_LIBRARY_REPOSITORY_HEAD) {
            url.replaceFirst(MINECRAFT_OFFICIAL_LIBRARY_REPOSITORY_HEAD, "")
                .let { rawUrl ->
                    currentRepository + rawUrl
                }
        } else url
    }

    override val remoteFiles: List<RemoteFile>
        get() {
            return buildList {
                libraries.forEachAvailable {
                    it.downloads?.artifact?.let { artifact ->
                        this += RemoteFile(
                            urlProcess(artifact.url),
                            artifact.size,
                            Path.of(librariesRoot.absolutePathString(), artifact.path),
                            artifact.sha1
                        )
                    }
                }
            }
        }
}