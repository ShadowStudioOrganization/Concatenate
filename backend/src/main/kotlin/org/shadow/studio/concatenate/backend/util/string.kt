package org.shadow.studio.concatenate.backend.util

import org.shadow.studio.concatenate.backend.launch.MinecraftVersion

fun String.wrapDoubleQuote() = "\"" + this + "\""

fun String.replaceDollarExpressions(pool: Map<String, String>): String {
    return replace(Regex("\\$\\{([^}]*)}")) {
        val key = it.groupValues[1]
        if (!pool.containsKey(key)) error("$key is required!") // error handling here
        pool[key] ?: ""
    }
}