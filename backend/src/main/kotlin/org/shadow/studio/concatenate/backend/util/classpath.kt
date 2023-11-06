package org.shadow.studio.concatenate.backend.util

import org.shadow.studio.concatenate.backend.data.profile.*
import java.io.File

inline fun eachAvailableLibrary(libraries: List<Map<String, *>>, action: (LibraryItem) -> Unit) {
    jsonObjectConvGet {
        for (library in libraries) {
            var isForbidden = false
            library.havingKey<List<*>>("rules") { rules ->
                isForbidden = !rulesJudging(rules)
            }

            if (isForbidden) continue

            val name = library["name"] as String
            if (library.keys.size == 2 && library.keys.containsAll(listOf("name", "url"))) {
                // classic fabric
                val url = library["url"] as String
                action(FabricLibraryItem(name, url))
            } else {
                val artifact = library["downloads"]["artifact"]
                val path = artifact["path"] as String
                val sha1 = artifact["sha1"] as String
                val size = artifact["size"].toString().toLong()
                val url = artifact["url"] as String
                action(
                    CommonLibraryItem(
                        name, Downloads(Artifact(path, sha1, size, url))
                    )
                )
            }
        }
    }
}

fun gatheringClasspath(libraries: List<Map<String, *>>, librariesRootFile: File, checkFile: Boolean = false): List<String> {
    return buildList classpath@{
        jsonObjectConvGet {
            eachAvailableLibrary(libraries) { library ->
                when (library) {
                    is CommonLibraryItem -> {
                        val artifact = library.downloads.artifact

                        val file = File(librariesRootFile, artifact.path)
                        if (!file.exists()) {
                            error("$file not exists!") // todo throw an exception
                        }

                        if (checkFile) {
                            globalLogger.debug("checking sha1 of {}", file)
                            val real = calculateSHA1(file)
                            val expect = artifact.sha1
                            if (file.length() != artifact.size || expect != real) {
                                error("check file failed with $file, expect: $expect, but real: $real") // todo throw an exception
                            }
                        }

                        +file.absolutePath
                    }

                    is FabricLibraryItem -> {

                    }
                }
            }
        }
    }
}