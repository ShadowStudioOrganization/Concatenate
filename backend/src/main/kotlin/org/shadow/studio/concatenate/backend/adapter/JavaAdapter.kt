package org.shadow.studio.concatenate.backend.adapter


import org.shadow.studio.concatenate.backend.launch.MinecraftVersion
import org.shadow.studio.concatenate.backend.util.contains
import org.shadow.studio.concatenate.backend.util.rangeTo


/**
 * this class is use for looking forward java
 * */
open class JavaAdapter(private val finder: JavaFinder = JavaFinder()) {

    open suspend fun getJavaBinary(version: MinecraftVersion, preferVersion: Int? = null): JavaRuntimeLocation? {

        val candidate: List<JavaRuntimeLocation> = finder.find()

        return version.profile.javaVersion?.let { java ->

            candidate.find { it.majorVersion == preferVersion && it.is64bit }
                ?: candidate.find { it.majorVersion == java.majorVersion && it.is64bit }
                ?: candidate.find { it.majorVersion == java.majorVersion }
                ?: with(candidate.filter { it.is64bit }) {
                    when (version) {
                        // 1.12(17w13a) - 1.16.5(1.17-21w18a)
                        in "17w13a".."21w18a" -> find { it.majorVersion == 8 } ?: find { it.majorVersion == 17 } ?: find { it.majorVersion > 8 }
                        // 1.17(21w19a) - 1.17.1
                        in "21w19a".."1.17.1" -> find { it.majorVersion == 16 } ?: find { it.majorVersion == 17 } ?: find { it.majorVersion > 16 }
                        else -> {
                            if (version >= "1.18-pre2")
                                find { it.majorVersion == 17 } ?: find { it.majorVersion > 17 }
                            else
                                find { it.majorVersion == 8 }?: find { it.majorVersion == 17 }
                        }
                    }
                }
        }
    }
}




