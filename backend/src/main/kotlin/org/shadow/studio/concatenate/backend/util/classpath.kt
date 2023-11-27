package org.shadow.studio.concatenate.backend.util

import org.shadow.studio.concatenate.backend.data.profile.Artifact
import org.shadow.studio.concatenate.backend.data.profile.LibraryItem
import java.io.File

inline fun LibraryItem.pickArtifact(action: LibraryItem.(Artifact) -> Unit) {
    downloads?.artifact?.let { artifact -> action(artifact) }
}


inline fun LibraryItem.pickClassifierArtifact(action: LibraryItem.(Artifact) -> Unit) {
    downloads?.classifiers?.let { classifiers ->
        val artifact = when(getSystemName()) {
            "windows" -> classifiers.nativesWindows ?: classifiers.nativesWindows64 ?: classifiers.nativesWindows32
            "linux" -> classifiers.nativesLinux
            "osx" -> classifiers.nativesMacos ?: classifiers.nativesOSX
            else -> null
        }
        artifact?.let { action(it) }
    }
}

suspend fun releaseNativeLibraries(libraries: List<LibraryItem>, librariesRootFile: File, into: File, isExtractSha1: Boolean = false) {
    libraries.forEachAvailableClassifier { artifact ->
        coroutineExecutorsAsync(9) {
            uncompressZipFile(File(librariesRootFile, artifact.path), into) { entry ->
                var flag = 0
                this@forEachAvailableClassifier.extract?.let { extract ->
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


inline fun List<LibraryItem>.forEachAvailableArtifact(action: LibraryItem.(Artifact) -> Unit) {
    forEachAvailable { lib ->
        lib.pickArtifact(action)
    }
}

inline fun List<LibraryItem>.forEachAvailableClassifier(action: LibraryItem.(Artifact) -> Unit) {
    forEachAvailable { lib ->
        lib.pickClassifierArtifact(action)
    }
}

inline fun List<LibraryItem>.forEachAvailableArtifactAndClassifier(action: LibraryItem.(Artifact) -> Unit) {
    forEachAvailable { lib ->
        lib.pickArtifact(action)
        lib.pickClassifierArtifact(action)
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