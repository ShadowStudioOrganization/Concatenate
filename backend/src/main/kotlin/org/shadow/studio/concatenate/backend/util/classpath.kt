package org.shadow.studio.concatenate.backend.util

import org.shadow.studio.concatenate.backend.data.profile.LibraryItem
import java.io.File

fun releaseNativeLibraries(libraries: List<LibraryItem>, librariesRootFile: File, into: File, isExtractSha1: Boolean = false) {
    eachAvailableLibrary(libraries) { library ->
        library.downloads?.classifiers?.let { classifiers ->
            classifiers.nativesWindows?.path?.let { path ->
                unzip(File(librariesRootFile, path), into) { entry ->
                    var flag = 0
                    library.extract?.let { extract ->
                        extract.exclude.forEach { exclude ->
                            if (entry.name.startsWith(exclude)) flag ++
                        }
                    }

                    if (entry.name.startsWith("META-INF/")) flag ++
                    if (entry.name.endsWith(".txt")) flag ++
                    if (entry.name.endsWith(".git")) flag ++
                    if (!isExtractSha1 && entry.name.endsWith(".sha1")) flag ++

                    flag == 0
                }
            }
        }
    }
}

inline fun eachAvailableLibrary(libraries: List<LibraryItem>, action: (LibraryItem) -> Unit) {
    for (library in libraries) {
        var isForbidden = false

        library.rules?.let { rules ->
            isForbidden = !resolveLibraryRules(rules)
        }

        if (isForbidden) continue

        action(library)
    }
}
/*

fun gatheringClasspath(libraries: List<LibraryItem>, librariesRootFile: File, checkFile: Boolean = false): List<String> {
    return buildList classpath@{
        jsonObjectConvGet {
            eachAvailableLibrary(libraries) { library ->

                library.downloads?.let { downloads ->

                    val artifact = downloads.artifact
                    val file = File(librariesRootFile, artifact!!.path) // todo fix here
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
            }
        }
    }
}*/
