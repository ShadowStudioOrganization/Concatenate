package org.shadow.studio.concatenate.backend.download


import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import org.shadow.studio.concatenate.backend.builder.MinecraftClientLauncherBuilder
import org.shadow.studio.concatenate.backend.data.launchermeta.LauncherMetaManifest
import org.shadow.studio.concatenate.backend.data.launchermeta.Version
import org.shadow.studio.concatenate.backend.launch.MinecraftClientLauncher
import org.shadow.studio.concatenate.backend.launch.MinecraftVersion
import org.shadow.studio.concatenate.backend.resolver.DirectoryLayer
import org.shadow.studio.concatenate.backend.resolver.MinecraftResourceResolver
import org.shadow.studio.concatenate.backend.resolver.NormalDirectoryLayer
import org.shadow.studio.concatenate.backend.util.buildMinecraftClientLauncher
import org.shadow.studio.concatenate.backend.util.globalClient
import org.shadow.studio.concatenate.backend.util.globalLogger
import org.slf4j.Logger
import java.io.File

class MinecraftClientDownloadManager(
    private val versionId: String,
    versionName: String,
    workingDirectory: File,
    meta: LauncherMetaManifest,
    versionIsolated: Boolean = false
) {

    private val versionManifest: Version = meta.versions.find { it.id == versionId }
        ?: error("can't find the given minecraft version: $versionId in launcher mata manifest.")

    private val layer: DirectoryLayer
    private lateinit var resolver: MinecraftResourceResolver
    private lateinit var version: MinecraftVersion
    private val downloaders: MutableList<MinecraftResourceDownloader> = mutableListOf()
    private var ktorClient: HttpClient
    private var logger: Logger

    init {
        layer = NormalDirectoryLayer(workingDirectory, versionIsolated, versionName)
        ktorClient = globalClient
        logger = globalLogger
    }

    /**
     * this function should be called ahead of [initResourcesDownloader]
     */
    suspend fun downloadManifest(tryTimes: Int = 3, restFor: Long = 1000) {
        layer.getMinecraftJsonProfilePosition().let { jsonProfileLocal ->
            if (!jsonProfileLocal.exists()) {
                GameProfileJsonDownloader(
                    jsonProfileLocal.toPath(),
                    versionManifest
                ).download(tryTimes, restFor).apply {
                    if (isNotEmpty()) error("download json profile of $versionManifest failed.")
                }
            }
        }

        version = layer.newMinecraftVersion()
        resolver = MinecraftResourceResolver(layer, version)

        resolver.resolveAssetIndexJsonFile().let { indexJson ->
            if (!indexJson.exists()) {
                AssetsIndexManifestDownloader(
                    version.profile.assetIndex,
                    indexJson.toPath()
                ).download(3).apply {
                    if (isNotEmpty()) error("download assets index json of ${version.profile.assetIndex} failed.")
                }
            }
        }
    }

    fun initResourcesDownloader(): List<MinecraftResourceDownloader> {
        return downloaders.apply {
            layer.getMinecraftJarPosition().takeIf { !it.exists() }?.let { jar ->
                this += GameClientJarDownloader(
                    jar.toPath(),
                    version.profile.downloads.client,
                    ktorClient = ktorClient
                )
            }

            resolver.resolveAssetIndexJsonFile().apply { if (!exists()) error("assets index of $this not exist.") }
                .let { indexJson ->
                    this += AssetDownloader(
                        assetManifestSource = indexJson,
                        assetObjectsDirectory = resolver.resolveAssetObjectsRoot().toPath(),
                        poolSize = 128,
                        ktorClient = ktorClient
                    )
                }

            resolver.resolveLibrariesRoot().let { libraryRoot ->
                this += LibrariesDownloader(version, libraryRoot.toPath(), poolSize = 18, ktorClient = ktorClient)
            }

        }.onEach {
            it.setLogger(this@MinecraftClientDownloadManager.logger)
        }
    }

    fun useNewOkHttpClient(block: HttpClientConfig<OkHttpConfig>.() -> Unit) {
        ktorClient = HttpClient(OkHttp, block)
    }

    suspend fun download(
        tryTimes: Int = 3,
        restFor: Long = 1000L,
        config: (MinecraftResourceDownloader.() -> Unit)? = null
    ) = withContext(Dispatchers.IO) {
        downloaders.map {
            async {
                config?.let { cfg -> it.cfg() }
                it.download(tryTimes, restFor).apply {
                    if (isNotEmpty()) error("download minecraft with $it for $tryTimes times, but all failed.")
                }
            }
        }.awaitAll()
    }

    fun buildLauncher(block: MinecraftClientLauncherBuilder.() -> Unit): MinecraftClientLauncher {
        return buildMinecraftClientLauncher {
            workingDirectory = layer.workingDirectory
            versionName = layer.versionName
            versionIsolate = layer.versionIsIsolated
            directoryLayer = layer
            block()
        }
    }
}