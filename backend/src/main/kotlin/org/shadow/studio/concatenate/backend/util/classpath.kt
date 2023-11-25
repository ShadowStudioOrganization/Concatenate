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


inline fun List<LibraryItem>.forEachAvailable(action: (LibraryItem) -> Unit) {
    for (library in this) {
        var isForbidden = false

        library.rules?.let { rules ->
            isForbidden = !resolveLibraryRules(rules)
        }

        if (isForbidden) continue

        action(library)
    }
}


@Deprecated("use List<LibraryItem>.forEachAvailable instead")
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