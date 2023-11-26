
package org.shadow.studio.concatenate.backend.resolver

import org.shadow.studio.concatenate.backend.data.launch.MinecraftExtraJvmArguments
import org.shadow.studio.concatenate.backend.data.profile.ComplexMinecraftArgument
import org.shadow.studio.concatenate.backend.launch.MinecraftVersion
import org.shadow.studio.concatenate.backend.util.*
import java.io.File
import kotlin.collections.buildList

open class MinecraftResourceResolver(private val layer: DirectoryLayer, private val version: MinecraftVersion) {

//    private val profile: JsonProfile = layer.version.profile
    private val logger = globalLogger

    open fun resolveAssetIndex(): String = version.getAssetIndex()


    /**
     * .minecraft/versions/version-name/version-name.jar
     */
    open fun resolveGameJar(): File = version.getJarFile()

    /**
     * .minecraft/assets/index/8.json
     * File(resolveAssetRoot(), listOf("indexes", "${version.getAssetIndex()}.json").joinToString(File.separator))
     */
    open fun resolveAssetIndexJsonFile(): File = File(resolveAssetRoot(), listOf("indexes", resolveAssetIndex() + ".json").joinToString(File.separator))

    /**
     * .minecraft/versions/version-name/version-name-natives
     */
    open fun resolveNatives(isExtractSha1: Boolean = false): File {
        val nr = layer.getNativeDirectoryPosition(true)
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

        version.profile.libraries.forEachAvailable { library ->
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

    open fun resolveClasspath(addingGameJar: Boolean = true, checkFileExists: Boolean = true): List<File> = buildList {
        val lr = resolveLibrariesRoot()
        version.profile.libraries.forEachAvailable { library ->
            library.downloads?.artifact?.let { artifact ->
                val file = File(lr, artifact.path)
                if (checkFileExists && !file.exists()) error("$file not exists!") // todo throw an exception
                add(file)
            }
        }
        if (addingGameJar) add(resolveGameJar())
    }

    /**
     * .minecraft/assets/
     */
    open fun resolveAssetRoot(autoCreate: Boolean = true): File = layer.getAssetRoot().apply {
        if (autoCreate && !exists()) mkdirs()
    }

    /**
     * .minecraft/assets/indexes/
     */
    open fun resolveAssetIndexesDirectory(autoCreate: Boolean = true) = File(layer.getAssetRoot(), "indexes").apply {
        if (autoCreate && !exists()) mkdirs()
    }

    /**
     * .minecraft/assets/skins/
     */
    open fun resolveAssetSkinDirectory(autoCreate: Boolean = true) = File(layer.getAssetRoot(), "skins").apply {
        if (autoCreate && !exists()) mkdirs()
    }

    /**
     * .minecraft or .minecraft/versions/version-name/
     */
    open fun resolveGameDirectory(autoCreate: Boolean = true): File = layer.gameDirectory.apply { if (autoCreate && !exists()) mkdirs() }

    /**
     * .minecraft/libraries/
     */
    open fun resolveLibrariesRoot(autoCreate: Boolean = true): File = layer.getLibrariesRoot().apply { if (autoCreate && !exists()) mkdirs() }

    open fun resolveComplexMinecraftArguments(gameConfig: Map<String, String>, ruleFeatures: Map<String, Boolean>): List<String> {
        return version.profile.arguments?.game?.let { gameArguments ->
            mappingComplexMinecraftArguments(gameArguments, gameConfig, ruleFeatures)
        } ?: version.profile.minecraftArguments?.let { ma ->
            logger.debug("lower version: ${version.versionId} detected, minecraftArguments is not null.")
            resolveLowVersionMinecraftArguments(ma, gameConfig)
        } ?: run {
            // maybe low version
            TODO()
        }
    }

    open fun resolveLowVersionMinecraftArguments(commandLine: String, gameConfig: Map<String, String>): List<String> {
        return buildList<String> {
            commandLine.split(" ").forEach { item ->
                add(item.replaceDollarExpressions(gameConfig))
            }
        }
    }

    open fun resolveComplexJvmArguments(jvmConfig: Map<String, String>): List<String> {
        return version.profile.arguments?.jvm?.let { jvmArguments ->
            mappingComplexMinecraftArguments(jvmArguments, jvmConfig, mapOf())
        } ?: version.profile.minecraftArguments?.let { _ ->
            resolveLowVersionJvmArguments(jvmConfig)
        } ?: run {
            // maybe low version
            version.profile.minecraftArguments
            TODO()
        }
    }

    open fun resolveLowVersionJvmArguments(jvmConfig: Map<String, String>): List<String> {
        return listOf(
            "-cp",
            resolveClasspath(true).joinToString(File.pathSeparator) { it.absolutePath },
            "-Djava.library.path=" + resolveNatives() //layer.getNativeDirectoryPosition(true)
        )
    }

    open fun resolveLoggingArguments(loggingConfig: Map<String, String>): List<String> = buildList {
        version.profile.logging?.client?.argument?.replaceDollarExpressions(loggingConfig)?.let { lc ->
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

    /**
     * .minecraft/assets/objects
     */
    open fun resolveAssetObjectsRoot(autoCreate: Boolean = true): File = File(resolveAssetRoot(), "objects")
        .apply { if (autoCreate && !exists()) mkdirs() }

    open fun resolveLoggingConfigurationFile(): File {
        return File(layer.workingDirectory, listOf(
            "versions",
            version.versionName,
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