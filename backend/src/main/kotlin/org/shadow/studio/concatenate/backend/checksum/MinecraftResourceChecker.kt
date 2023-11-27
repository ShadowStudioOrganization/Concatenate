package org.shadow.studio.concatenate.backend.checksum

import org.shadow.studio.concatenate.backend.data.profile.JsonProfile
import org.shadow.studio.concatenate.backend.data.profile.LibraryItem
import org.shadow.studio.concatenate.backend.download.AsyncConcatQueue
import org.shadow.studio.concatenate.backend.download.buildAsyncConcatQueue
import org.shadow.studio.concatenate.backend.util.*
import org.shadow.studio.concatenate.backend.util.globalLogger
import org.slf4j.Logger
import java.io.File

open class MinecraftResourceChecker(private val logger: Logger = globalLogger) {

    open fun checkVersionJar(profile: JsonProfile, versionJar: File): Boolean {
        val versionJarSha1 = profile.downloads.client.sha1
        val versionJarSize = profile.downloads.client.size

        return versionJarSha1?.let { sha1 ->
            checkSum(versionJar, versionJarSize, sha1)
        } ?: versionJar.exists() && versionJar.length() != 0L
    }

    /**
     * @return failed files
     */
    open suspend fun checkAssetsObjects(indexJsonFile: File, indexObjectRoot: File, poolSize: Int = 64): AsyncConcatQueue<File> {

        val objects = getAssetObjectsFromFile(indexJsonFile)

        val failedPaths = AsyncConcatQueue<File>()

        val items = buildAsyncConcatQueue<Triple<File, Long, Pair<String, String>>> {
            for ((key, item) in objects.fields()) {
                val hash = item.get("hash").textValue()
                val size = item.get("size").longValue()
                val asset = File(indexObjectRoot, hash.substring(0..1) + File.separator + hash)
                enqueueAsync(Triple(asset, size, hash to key))
            }
        }

        coroutineExecutorsAsync(poolSize) {
            while (true) {
                val (asset, size, hashAndKey) = items.dequeueAsync() ?: break
                val (hash, key) = hashAndKey
                if (!asset.exists() || size != asset.length() || calculateSHA1(asset) != hash) {
                    failedPaths.enqueueAsync(asset)
                }
                logger.debug("checked asset: {}, named: {} at {}", hash, key, asset.parent)
            }
        }

        return failedPaths
    }

    /**
     * @return failed files
     */
    open fun checkClasspath(libraries: List<LibraryItem>, librariesRootFile: File): List<File> {
        val failedPaths = mutableListOf<File>()
        libraries.forEachAvailableArtifactAndClassifier { artifact ->
            val jar = File(librariesRootFile, artifact.path) // todo fix here
            if (!checkSum(jar, artifact.size, artifact.sha1)) {
                failedPaths += jar
            }
            logger.debug("checked library: {}", jar)
        }

        return failedPaths
    }

    private fun checkSum(file: File, size: Long, sha1: String): Boolean {
        return file.exists() && file.length() == size && sha1 == calculateSHA1(file)
    }
}