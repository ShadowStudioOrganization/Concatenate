package org.shadow.studio.concatenate.backend.adapter

import kotlinx.coroutines.*
import kotlinx.coroutines.time.withTimeout
import org.shadow.studio.concatenate.backend.util.getSystemName
import org.shadow.studio.concatenate.backend.util.globalLogger
import org.shadow.studio.concatenate.backend.util.listPaths
import org.shadow.studio.concatenate.backend.util.wrapDoubleQuote
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.Duration
import java.util.stream.Collectors
import kotlin.io.path.*

open class JavaFinder {

    open suspend fun find(): List<JavaRuntimeLocation> {

        val javaExecutableName: String
        val javawExecutableName: String

        if (getSystemName() == "windows") {
            javaExecutableName = "java.exe"
            javawExecutableName = "javaw.exe"
        } else {
            javaExecutableName = "java"
            javawExecutableName = "javaw"
        }

        return buildList<File> files@{
            buildList paths@{

                this@paths.addAll(listPaths("C:", "Program Files", "Java", "*", "bin"))
                this@paths.addAll(listPaths("C:", "Program Files", "AdoptOpenJDK", "*", "bin"))
                this@paths.addAll(listPaths("C:", "Program Files", "OpenJDK", "*", "bin"))
                this@paths.addAll(listPaths("C:", "Program Files", "Zulu", "*", "bin"))
                this@paths.addAll(listPaths("D:", "Environments", "java", "*", "bin"))

                this@paths.addAll(listPaths("usr", "lib", "jvm", "*", "bin"))
                this@paths.addAll(listPaths("usr", "lib", "java", "*", "bin"))
                this@paths.addAll(listPaths("usr", "lib", "java", "%java-\\d\\d?-openjdk-amd64%", "bin"))
                this@paths.addAll(listPaths("Library", "Java", "JavaVirtualMachines", "*", "bin"))

                this@paths.addAll(run {
                    if (getSystemName() == "windows") System.getenv("Path") else System.getenv("PATH")
                }.split(File.pathSeparator))

                System.getenv().filter { (k, _) ->
                    k.matches(Regex("(?i)(JAVA|JDK|JRE)_?(\\d\\d?_?)?HOME")) || k.matches(Regex("(?i)(JAVA|JDK)"))
                }.map { (_, v) ->
                    val dirname = Paths.get(v).normalize()
                    if (dirname.endsWith("bin"))
                        dirname.absolutePathString()
                    else
                        dirname.absolutePathString() + File.separator + "bin"
                }.let { this@paths.addAll(it) }
            }
            .mapNotNull { path ->
                runCatching { Paths.get(path).toRealPath() }
                    .onFailure { globalLogger.error("get potential java binary location: '$path' fails, reason: $it") }
                    .getOrNull()
            }
            .filter { it.exists() && it.isDirectory() }
            .forEach { path ->
                this@files.addAll(Files
                    .list(path)
                    .filter { it.isRegularFile() && it.endsWith(javaExecutableName) }
                    .map { it.toFile() }
                    .collect(Collectors.toList())
                )
            }
        }.distinct().map { determineJavaExecutable(it) }.awaitAll().filterNotNull()
    }

    private val versionRegex = Regex("\"([0-9_]+(\\.[0-9_]+)*)\"")

    open fun resolveJavaOutput(output: String, javaBinaryFile: File): JavaRuntimeLocation {
        val outputLines = output.trim().split("\n")

        return if (outputLines.size == 3 && outputLines.first().contains(versionRegex)) {
            val versionString = versionRegex.find(outputLines.first())?.groups?.get(1)?.value!!
            val bit = outputLines.last().lowercase().contains("64-bit")
            JavaRuntimeLocation(Paths.get(javaBinaryFile.absolutePath), bit, versionString)
        } else error("not stander java program")
    }

    open suspend fun determineJavaExecutable(javaBinaryFile: File, timeout: Duration = Duration.ofSeconds(2)): Deferred<JavaRuntimeLocation?> = withContext(Dispatchers.IO) {
        async {
            var process: Process? = null

            runCatching {
                withTimeout(timeout) {
                    process = ProcessBuilder(listOf(javaBinaryFile.absolutePath.wrapDoubleQuote(), "-version")).start()

                    process?.let { process ->
                        val exitCode = process.waitFor()
                        if (exitCode == 0) {
                            resolveJavaOutput(process.errorStream.bufferedReader().readText(), javaBinaryFile)
                        } else error("program execute fails")
                    }
                }
            }.onFailure {
                process?.let { p -> if (p.isAlive) p.destroy() }
                globalLogger.error("determine java binary: '$javaBinaryFile' fails, reason: $it")
            }.getOrNull()
        }
    }
}

data class JavaRuntimeLocation(
    val path: Path,
    val is64bit: Boolean,
    private val versionString: String
) {
    val majorVersion: Int get() {
        val vss = versionString.split(".")

        return when (vss.first().toIntOrNull()) {
            // 1.6, 1.8
            1 -> vss[1].toInt()
            // unknown jdk version
            null -> error("failed to parse jdk version")
            else -> vss.first().toInt()
        }
    }

    operator fun compareTo(other: JavaRuntimeLocation): Int {
        return majorVersion.compareTo(other.majorVersion)
    }

    operator fun compareTo(jdkVersion: Int): Int {
        return majorVersion.compareTo(jdkVersion)
    }
}