package org.shadow.studio.concatenate.backend.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import java.util.concurrent.Executors

fun IntRange.toLongRange(): LongRange = LongRange(first.toLong(), last.toLong())
fun LongRange.size(): Long = last - first + 1
fun IntRange.size(): Int = last - first + 1

suspend fun coroutineExecutorsAsync(poolSize: Int, job: suspend CoroutineScope.(Int) -> Unit) {
    val coroutineDispatcher = Executors.newFixedThreadPool(poolSize).asCoroutineDispatcher()
    val coroutineScope = CoroutineScope(coroutineDispatcher)

    val coroutinePool = List(poolSize) { coroutineId ->
        coroutineScope.async {
            this.job(coroutineId)
        }
    }

    coroutinePool.forEach { it.await() }

    // Shutdown the coroutine pool
    coroutineDispatcher.close()
}