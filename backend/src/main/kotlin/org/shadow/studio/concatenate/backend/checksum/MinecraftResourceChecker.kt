package org.shadow.studio.concatenate.backend.checksum

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.shadow.studio.concatenate.backend.data.profile.JsonProfile
import org.shadow.studio.concatenate.backend.data.profile.LibraryItem
import org.shadow.studio.concatenate.backend.util.calculateSHA1
import org.shadow.studio.concatenate.backend.util.eachAvailableLibrary
import java.io.File

open class MinecraftResourceChecker {

    open fun checkVersionJar(profile: JsonProfile, versionJar: File) {
        val versionJarSha1 = profile.downloads.client.sha1
        val versionJarSize = profile.downloads.client.size

        versionJarSha1?.let { sha1 ->
            if (!checkSum(versionJar, versionJarSize, sha1))
                error("version.jar file is damaged")
        } // if versionJarSha1 is null
    }

    open fun checkAssetsObjects(indexJsonFile: File, indexObjectRoot: File): Boolean {

        val objects = jacksonObjectMapper().readTree(indexJsonFile).let { rootNode ->
            if (!rootNode.isObject) error("not an object")
            rootNode.get("objects")
        }

        var isComplete = true
        for ((key, item) in objects.fields()) {
            val hash = item.get("hash").textValue()
            val size = item.get("size").longValue()

            val asset = File(indexObjectRoot, hash.substring(0..1) + File.separator + hash)
//            logger.debug("checking asset: {} at {}", key, assetFile.absolutePath)
            if (size != asset.length() || calculateSHA1(asset) != hash) {
                isComplete = false
                break
            }
        }

        return isComplete
    }

    open fun checkClasspath(libraries: List<LibraryItem>, librariesRootFile: File) {
        eachAvailableLibrary(libraries) { library ->
            library.downloads?.artifact?.let { artifact ->
                val jar = File(librariesRootFile, artifact.path) // todo fix here
                if (!jar.exists()) error("$jar not exists!") // todo throw an exception

//                globalLogger.debug("checking sha1 of {}", jar)
                val real = calculateSHA1(jar)
                val expect = artifact.sha1
                if (jar.length() != artifact.size || expect != real)
                    error("check file failed with $jar, expect: $expect, but real: $real") // todo throw an exception
            }
        }
    }

    private fun checkSum(file: File, size: Long, sha1: String): Boolean {
        return file.length() == size && sha1 == calculateSHA1(file)
    }
}