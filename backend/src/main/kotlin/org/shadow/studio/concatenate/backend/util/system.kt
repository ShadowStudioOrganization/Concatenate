package org.shadow.studio.concatenate.backend.util

import java.io.File
import java.nio.file.Paths
import kotlin.io.path.absolutePathString
import kotlin.io.path.exists

/**
 * list the representing path combined by items
 * @param items can be folder name, * or %regex%, always start from root
 * @return all matches paths
 */
fun listPaths(vararg items: String): List<String> {

    val poolList = mutableListOf(mutableListOf(*items))
    val retList = mutableListOf<String>()

    fun pathify(list: List<String>) = Paths.get(list.joinToString(File.separator))

    val iter = poolList.listIterator()

    while (iter.hasNext() || iter.hasPrevious()) {
        val path = if (iter.hasPrevious()) iter.previous() else iter.next()

        val indexOfStar = path.indexOfFirst { it == "*" || (it.startsWith("%") && it.endsWith("%")) }

        if (indexOfStar != -1) {
            val expr = path[indexOfStar]

            val subDirName = pathify(path.subList(0, indexOfStar))
                .toFile()
                .listFiles()
                ?.filter {
                    if (expr == "*")
                        it.exists() && it.isDirectory
                    else {
                        it.exists() && it.isDirectory && it.name.matches(Regex(expr.removePrefix("%").removeSuffix("%")))
                    }
                }
                ?.map { it.name }

            val newPaths = kotlin.collections.buildList<MutableList<String>> {
                subDirName?.forEach { dirname ->
                    val new = path.toMutableList()
                    new[indexOfStar] = dirname
                    add(new)
                }
            }.filter { pathify(it.subList(0, indexOfStar + 1)).exists() }

            iter.remove()
            newPaths.forEach(iter::add)

        } else {
            pathify(path).let {
                if (it.exists()) retList += it.normalize().absolutePathString()
            }

            iter.remove()
        }

    }

    return retList
}

fun getSystemName(): String {
    val javaRet = System.getProperty("os.name").lowercase()
    return if (javaRet.startsWith("windows"))
        "windows"
    else if (javaRet.startsWith("mac"))
        "osx"
    else if (javaRet.startsWith("linux"))
        "linux"
    else if (javaRet.startsWith("unix"))
        "unix"
    else javaRet
}

fun getSystemArch(): String {
    return System.getProperty("os.arch")
}

fun getSystemVersion(): String {
    return System.getProperty("os.version")
}