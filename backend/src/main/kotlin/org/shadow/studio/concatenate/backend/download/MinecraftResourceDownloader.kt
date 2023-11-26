package org.shadow.studio.concatenate.backend.download

import io.ktor.client.*
import org.shadow.studio.concatenate.backend.data.download.RemoteFile
import org.shadow.studio.concatenate.backend.util.globalClient

abstract class MinecraftResourceDownloader(
    officialRepositoryUrl: String,
    poolSize: Int = DEFAULT_CONCATE_DOWNLOADER_POOL_SIZE,
    taskTTL: Int = DEFAULT_CONCATE_DOWNLOADER_TASK_TTL,
    ktorClient: HttpClient = globalClient,
    ktorBuffetSize: Long = DEFAULT_CONCATE_DOWNLOADER_KTOR_BUFFER_SIZE
): ConcatenateDownloader(poolSize, taskTTL, ktorClient, ktorBuffetSize) {

    val repositories: Repositories = Repositories(Repository(officialRepositoryUrl))
    var currentRepository: Repository = repositories.official

    class Repositories(official: Repository): MutableMap<String, Repository> by LinkedHashMap() {

        init {
            this["official"] = official
        }

        val official: Repository get() = this["official"] ?: error("official repository is not define!")

        fun getRepository(name: String): Repository {
            return getOrElse(name) { error("$name repository is not defined") }
        }

        fun addRepository(name: String, value: Repository): Boolean {
            return if (this[name] != null) false else {
                this[name] = value
                true
            }
        }

        fun setRepository(name: String, value: Repository) {
            this[name] = value
        }
    }

    @JvmInline
    value class Repository(val baseUrl: String) {
        fun wrap(subUrl: String): String {
            return if (baseUrl.endsWith("/")) baseUrl + subUrl else "$baseUrl/$subUrl"
        }
    }

    internal fun urlProcess(url: String): String {
        return if (currentRepository != repositories.official) {
            url.replaceFirst(currentRepository.baseUrl, "").let { currentRepository.wrap(it) }
        } else url
    }

    fun useRepository(name: String) {
        currentRepository = repositories.getRepository(name)
    }

    override val remoteFiles: List<RemoteFile>
        get() = getDownloadTarget()

    abstract fun getDownloadTarget(): List<RemoteFile>
}