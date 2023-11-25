package org.shadow.studio.concatenate.backend

import kotlinx.coroutines.runBlocking
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@OptIn(ExperimentalTime::class)
inline fun <R> showRunTime(crossinline block: suspend () -> R): R {
    val ret: R

    measureTime {
        ret = runBlocking {
            block()
        }
    }.apply { println("using time: $this") }

    return ret
}