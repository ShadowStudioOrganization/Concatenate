package org.shadow.studio.concatenate.backend.util


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