package org.shadow.studio.concatenate.backend.util

import java.io.File

fun gatheringClasspath(libraries: List<Map<String, *>>, librariesRootFile: File, checkFile: Boolean = false): List<String> {
    return buildList classpath@{
        jsonObjectConvGet {
            for (library in libraries) {
                var isForbidden = false
                library.havingKey<List<*>>("rules") { rules ->
                    isForbidden = !rulesJudging(rules)
                }
                if (isForbidden) continue

                val name = library["name"]
                if (library.keys.size == 2 && library.keys.containsAll(listOf("name", "url"))) {
                    // classic fabric
                    // todo fill
                } else {
                    library.havingKey<Map<String, *>>("downloads") { downloads ->
                        downloads.havingKey<Map<String, *>>("artifact") { artifact ->
                            val absFile = File(librariesRootFile, artifact["path"] as String)
                            if (!absFile.exists()) {
                                error("$absFile not exists!")
                                // todo throw an exception
                            }

                            if (checkFile) {
                                val sha1 = artifact["sha1"] as String
                                val size = (artifact["size"] as String).toLong()
                            }

                            +absFile.absolutePath
                        }
                    }
                }
            }
        }
    }
}