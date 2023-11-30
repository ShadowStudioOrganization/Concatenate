package org.shadow.studio.concatenate.backend.download

import io.ktor.client.*
import org.apache.maven.repository.internal.MavenRepositorySystemUtils
import org.eclipse.aether.RepositorySystem
import org.eclipse.aether.artifact.DefaultArtifact
import org.eclipse.aether.collection.CollectRequest
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory
import org.eclipse.aether.graph.Dependency
import org.eclipse.aether.graph.DependencyFilter
import org.eclipse.aether.repository.LocalRepository
import org.eclipse.aether.repository.Proxy
import org.eclipse.aether.repository.RemoteRepository
import org.eclipse.aether.resolution.DependencyRequest
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory
import org.eclipse.aether.spi.connector.transport.TransporterFactory
import org.eclipse.aether.transport.http.HttpTransporterFactory
import org.shadow.studio.concatenate.backend.data.download.RemoteFile
import org.shadow.studio.concatenate.backend.data.profile.LibraryItem
import org.shadow.studio.concatenate.backend.util.*
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.exists

class FabricLibrariesDownloader(
    private val downloader: LibrariesDownloader,
    officialRepositoryUrl: String = OFFICIAL_FABRIC_LIBRARIES_REPO_HEAD,
    poolSize: Int = downloader.poolSize,
    taskTTL: Int = downloader.poolSize,
    ktorClient: HttpClient = globalClient,
    ktorBuffetSize: Long = downloader.ktorBuffetSize
) : UnknownLengthResourceDownloader(
     officialRepositoryUrl = officialRepositoryUrl,
     poolSize = poolSize,
     taskTTL = taskTTL,
     ktorClient = ktorClient,
     ktorBuffetSize = ktorBuffetSize
) {

    companion object {
        const val OFFICIAL_FABRIC_LIBRARIES_REPO_HEAD = "https://maven.fabricmc.net/"
    }

    override fun getDownloadTarget(): List<RemoteFile> {
        return buildList {
            downloader.libraries.forEachAvailableArtifact {
                downloader.getLocalDestination(it.path).let { local ->
                    if (it.isUnknownSH1orSize && !local.exists()) {
                        add(RemoteFile(urlProcess(it.url), it.size, local))
                    }
                }
            }
        }
    }
}