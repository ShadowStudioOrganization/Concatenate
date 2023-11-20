
package org.shadow.studio.concatenate.backend.resolver

import org.shadow.studio.concatenate.backend.data.launch.MinecraftExtraJvmArguments
import org.shadow.studio.concatenate.backend.data.profile.ComplexMinecraftArgument
import org.shadow.studio.concatenate.backend.util.*
import java.io.File
import kotlin.collections.buildList

open class MinecraftResourceResolver(private val layer: DirectoryLayer) {

    private val profile = layer.version.profile

    open fun resolveAssetIndex(): String = layer.version.getAccessIndex()

    open fun resolveGameJar(): File = layer.version.getJarFile()

    open fun resolveAssetIndexJsonFile(): File = File(resolveAssetRoot(), listOf("indexes", resolveAssetIndex() + ".json").joinToString(File.separator))

    open fun resolveNatives(isExtractSha1: Boolean = false): File {
        val nr = layer.getNativeDirectory(true)
        val lr = layer.getLibrariesRoot()

        fun doUnzip(path: String, excludes: List<String>?) {
            unzip(File(lr, path), nr) { entry ->
                var flag = 0
                excludes?.forEach { exclude ->
                    if (entry.name.startsWith(exclude)) flag ++
                }

                if (entry.name.startsWith("META-INF/")) flag ++
                if (entry.name.endsWith(".txt")) flag ++
                if (entry.name.endsWith(".git")) flag ++
                if (!isExtractSha1 && entry.name.endsWith(".sha1")) flag ++

                flag == 0
            }
        }

        eachAvailableLibrary(profile.libraries) { library ->
            library.downloads?.classifiers?.let { classifiers ->

                classifiers.nativesWindows?.path?.let { path ->
                    if (getSystemName() == "windows") {
                        doUnzip(path, library.extract?.exclude)
                    }
                }

                classifiers.nativesLinux?.path?.let { path ->
                    if (getSystemName() == "linux") {
                        doUnzip(path, library.extract?.exclude)
                    }
                }

                classifiers.nativesMacos?.path?.let { path ->
                    if (getSystemName() == "osx") {
                        doUnzip(path, library.extract?.exclude)
                    }
                }
            }
        }

        return nr
    }

    open fun resolveClasspath(addingGameJar: Boolean = true): List<File> = buildList {
        val lr = resolveLibrariesRoot()
        eachAvailableLibrary(profile.libraries) { library ->
            library.downloads?.artifact?.let { artifact ->
                val file = File(lr, artifact.path)
                if (!file.exists()) error("$file not exists!") // todo throw an exception
                add(file)
            }
        }
        if (addingGameJar) add(resolveGameJar())
    }

    open fun resolveAssetRoot(): File = layer.getAccessRoot()

    open fun resolveGameDirectory(): File = layer.gameDirectory

    open fun resolveLibrariesRoot(): File = layer.getLibrariesRoot()

    open fun resolveComplexMinecraftArguments(gameConfig: Map<String, String>, ruleFeatures: Map<String, Boolean>): List<String> {
        return profile.arguments?.game?.let { gameArguments ->
            mappingComplexMinecraftArguments(gameArguments, gameConfig, ruleFeatures)
        } ?: run {
            // maybe low version
            profile.minecraftArguments
            TODO()
        }
    }

    open fun resolveComplexJvmArguments(jvmConfig: Map<String, String>): List<String> {
        return profile.arguments?.jvm?.let { jvmArguments ->
            mappingComplexMinecraftArguments(jvmArguments, jvmConfig, mapOf())
        } ?: run {
            // maybe low version
            profile.minecraftArguments
            TODO()
        }
    }

    open fun resolveLoggingArguments(loggingConfig: Map<String, String>): List<String> = buildList {
        profile.logging?.client?.argument?.replaceDollarExpressions(loggingConfig)?.let { lc ->
            add(lc)
        }
    }

    open fun resolveExtraJvmArguments(config: MinecraftExtraJvmArguments): List<String> = buildList {
        config.initialJavaHeapSize?.let { add("-Xms$it") }
        config.maximumJavaHeapSize?.let { add("-Xmx$it") }
        config.theYoungGenerationSize?.let { add("-Xmn$it") }
        if (config.useG1GC) add("-XX:+UseG1GC")
        if (config.useAdaptiveSizePolicy) add("-XX:-UseAdaptiveSizePolicy")
        if (config.omitStacktraceInFastThrow) add("-XX:-OmitStackTraceInFastThrow")
        config.fileEncoding?.let { add("-Dfile.encoding=$it") }
        config.stdoutEncoding?.let { add("-Dsun.stdout.encoding=$it") }
        config.stderrEncoding?.let { add("-Dsun.stderr.encoding=$it") }
    }

    open fun resolveAssetObjectsRoot(): File = File(resolveAssetRoot(), "objects")

    open fun resolveLoggingConfigurationFile(): File {
        return File(layer.workingDirectory, listOf(
            "versions",
            layer.version.versionName,
            "log4j2.xml"
        ).joinToString(File.separator))
    }

    private fun mappingComplexMinecraftArguments(
        obj: List<Any>,
        config: Map<String, String>,
        ruleFeatures: Map<String, Boolean>
    ): List<String> = buildList {
        for (item in obj) when (item) {
            is String -> add(item.replaceDollarExpressions(config))
            is ComplexMinecraftArgument -> {
                item.rules?.let { rules ->
                    if (resolveLibraryRules(rules, ruleFeatures)) when (item.value) {
                        is String -> add(item.value.replaceDollarExpressions(config))
                        is List<*> -> item.value.forEach { value -> add((value as String).replaceDollarExpressions(config)) }
                    }
                }
            }
        }
    }
}



private fun String.replaceDollarExpressions(pool: Map<String, String>): String {
    return replace(Regex("\\$\\{([^}]*)}")) {
        val key = it.groupValues[1]
        if (!pool.containsKey(key)) error("$key is required!") // error handling here
        pool[key] ?: ""
    }
}