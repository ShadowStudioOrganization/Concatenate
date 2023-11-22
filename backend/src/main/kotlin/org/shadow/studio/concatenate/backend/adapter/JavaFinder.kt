package org.shadow.studio.concatenate.backend.adapter

import org.shadow.studio.concatenate.backend.util.getSystemName
import org.shadow.studio.concatenate.backend.util.listPaths
import java.io.File
import java.lang.StringBuilder
import java.nio.file.Files
import java.nio.file.Paths
import java.util.Collections
import java.util.stream.Collectors
import kotlin.io.path.*

class JavaFinder {

    fun findJava() {

        val javaExecutableName: String
        val javawExecutableName: String

        if (getSystemName() == "windows") {
            javaExecutableName = "java.exe"
            javawExecutableName = "javaw.exe"
        } else {
            javaExecutableName = "java"
            javawExecutableName = "javaw"
        }

        val javas = buildList<File> files@{
            buildList paths@{

                this@paths.addAll(listPaths("C:", "Program Files", "Java", "*", "bin"))
                this@paths.addAll(listPaths("C:", "Program Files", "AdoptOpenJDK", "*", "bin"))
                this@paths.addAll(listPaths("C:", "Program Files", "OpenJDK", "*", "bin"))
                this@paths.addAll(listPaths("C:", "Program Files", "Zulu", "*", "bin"))

                this@paths.addAll(listPaths("usr", "lib", "jvm", "*", "bin"))
                this@paths.addAll(listPaths("usr", "lib", "java", "*", "bin"))
                this@paths.addAll(listPaths("usr", "lib", "java", "%java-\\d\\d?-openjdk-amd64%", "bin"))
                this@paths.addAll(listPaths("Library", "Java", "JavaVirtualMachines", "*", "bin"))

                this@paths.addAll(run {
                    if (getSystemName() == "windows")
                        System.getenv("Path")
                    else System.getenv("PATH")
                }.split(File.pathSeparator))

                System.getenv().filter { (k , _) ->
                    k.matches(Regex("(?i)(JAVA|JDK|JRE)_?(\\d\\d?_?)?HOME")) || k.matches(Regex("(?i)(JAVA|JDK)"))
                }
                    .map { (_, v) ->
                        val dirname = Paths.get(v).normalize()
                        if (dirname.endsWith("bin"))
                            dirname.absolutePathString()
                        else
                            dirname.absolutePathString() + File.separator + "bin"
                    }
                    .let { this@paths.addAll(it) }
            }
                .map { Paths.get(it) }
                .filter { it.exists() && it.isDirectory() }
                .forEach { path ->
                    this@files.addAll(Files
                        .list(path)
                        .filter { it.isRegularFile() && it.endsWith(javaExecutableName) }
                        .map { it.toFile() }
                        .collect(Collectors.toList())
                    )
                }
        }.distinct()

        javas.forEach(::println)



    }
}