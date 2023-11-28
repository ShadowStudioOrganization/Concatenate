package org.shadow.studio.concatenate.backend.download

import io.ktor.client.*
import org.shadow.studio.concatenate.backend.data.download.RemoteFile
import org.shadow.studio.concatenate.backend.util.DEFAULT_CONCATE_DOWNLOADER_KTOR_BUFFER_SIZE
import org.shadow.studio.concatenate.backend.util.DEFAULT_CONCATE_DOWNLOADER_POOL_SIZE
import org.shadow.studio.concatenate.backend.util.DEFAULT_CONCATE_DOWNLOADER_TASK_TTL
import org.shadow.studio.concatenate.backend.util.globalClient

abstract class MinecraftResourceDownloader(
    officialRepositoryUrl: String,
    poolSize: Int = DEFAULT_CONCATE_DOWNLOADER_POOL_SIZE,
    taskTTL: Int = DEFAULT_CONCATE_DOWNLOADER_TASK_TTL,
    ktorClient: HttpClient = globalClient,
    ktorBuffetSize: Long = DEFAULT_CONCATE_DOWNLOADER_KTOR_BUFFER_SIZE
): ConcatenateDownloader(poolSize, taskTTL, ktorClient, ktorBuffetSize) {

    protected val repositories: Repositories = Repositories(Repository(officialRepositoryUrl))
    protected var currentRepository: Repository = repositories.official
    var autoSwitchRepository: Boolean = false

    class Repositories(official: Repository): MutableMap<String, Repository> by LinkedHashMap() {

        init {
            this["official"] = official
        }

        val official: Repository get() = this["official"] ?: error("official repository is not define!")

        fun getRepository(name: String): Repository {
            return getOrElse(name) { error("$name repository is not defined") }
        }

        fun addRepository(name: String, repo: Repository): Boolean {
            return if (this[name] != null) false else {
                this[name] = repo
                true
            }
        }

        fun setRepository(name: String, repo: Repository) {
            this[name] = repo
        }
    }

    @JvmInline
    value class Repository(val baseUrl: String) {
        fun wrap(subUrl: String): String {
            return if (baseUrl.endsWith("/")) baseUrl + subUrl else "$baseUrl/$subUrl"
        }
    }

    protected open fun urlProcess(url: String): String {
        return if (currentRepository != repositories.official && url.startsWith(repositories.official.baseUrl)) {
            currentRepository.wrap(getSubUrl(url))
//            url.replaceFirst(repositories.official.baseUrl, "").let {
//                currentRepository.wrap(it)
//            }
        } else url
    }

    private fun getSubUrl(fullUrl: String): String {
        var now: String = fullUrl
        repositories.forEach { (_, repo) ->
            now = now.replaceFirst(repo.baseUrl, "")
        }
        return now
    }

    fun useRepository(name: String) {
        currentRepository = repositories.getRepository(name)
    }

    private fun RemoteFile.addMultiDownloadRepositories() {
        downloadSources = repositories.map { (_, repo) -> getSubUrl(repo.wrap(url)) }.filter { it != url }.reversed()
    }

    override val remoteFiles: List<RemoteFile> by lazy {
        getDownloadTarget().apply {
            if (autoSwitchRepository) {
                forEach { it.addMultiDownloadRepositories() }
            }
            internalRemoteFiles = this
        }
    }



    abstract fun getDownloadTarget(): List<RemoteFile>
}