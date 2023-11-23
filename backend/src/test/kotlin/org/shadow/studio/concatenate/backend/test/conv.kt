package org.shadow.studio.concatenate.backend.test

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Semaphore
import org.shadow.studio.concatenate.backend.download.NormalDownloader
import org.shadow.studio.concatenate.backend.util.globalClient
import java.util.concurrent.Executors
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@OptIn(ExperimentalTime::class)
suspend fun main() {
    val time = measureTime {
        NormalDownloader(globalClient).c()

    }
    println(time.absoluteValue)
}

suspend fun exc() {
    val poolSize = 50
    val semaphore = Semaphore(50)
    val coroutineDispatcher = Executors.newFixedThreadPool(poolSize).asCoroutineDispatcher()
    val coroutineScope = CoroutineScope(coroutineDispatcher)

    val coroutinePool = List(poolSize) {
        coroutineScope.async {

            semaphore.acquire()
            try {
                delay(2000L)
                println(">$it<")
            } finally {
                semaphore.release()
            }

        }
    }

    coroutinePool.forEach {
        it.await()
    }

    // Shutdown the coroutine pool
    coroutineDispatcher.close()
}