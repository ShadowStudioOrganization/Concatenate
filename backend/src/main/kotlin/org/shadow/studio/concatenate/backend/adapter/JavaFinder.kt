package org.shadow.studio.concatenate.backend.adapter

import org.shadow.studio.concatenate.backend.util.getSystemName
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

    fun MutableList<String>.addPath(vararg items: String) {


      /*  fun a(vararg names: String): List<List<String>> {
            return buildList {

                for ((index, name) in names.withIndex()) {
                    if (name == "*" && index != names.lastIndex) {

                    } else {
                        buildList<String> {
                            add(name)
                        }
                    }
                }


                buildString {
                    for ((index, name) in names.withIndex()) {
                        if (name == "*" && index != names.lastIndex) {
                            *//*val basePath = a(*names.asList().subList(0, index).toTypedArray())
                            File(basePath).listFiles()?.filter { it.isDirectory }?.forEach { dir ->

                            }*//*
                        } else {
                             append(name)
                             append(File.separator)
                        }
                    }
                }

            }
        }



        val f = mutableListOf<String>()
        val s = StringBuilder()
        for (item in items) {
            if (item == "*") {
                val collect =
                    Files.list(Paths.get(s.toString())).map { it.absolutePathString() }.collect(Collectors.toList())
                f.addAll(collect)
            } else s.append(File.separator).append(item)
        }
*/

        val retList = mutableListOf(mutableListOf(*items))

        val iter = retList.iterator()

        while (iter.hasNext()) {
            val path = iter.next()
            val indexOfStart = path.indexOfFirst { it == "*" }
            if (indexOfStart != -1) {

                val subFileName = File(path.subList(0, indexOfStart).joinToString(File.separator))
                    .listFiles()
                    ?.filter { it.exists() && it.isDirectory }
                    ?.map { it.name }



            }
        }



    }
}