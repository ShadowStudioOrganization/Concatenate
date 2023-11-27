package org.shadow.studio.concatenate.backend.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import org.shadow.studio.concatenate.backend.launch.MinecraftClientConfiguration
import org.shadow.studio.concatenate.backend.builder.MinecraftClientConfigurationBuilder
import org.shadow.studio.concatenate.backend.launch.MinecraftClientLauncher
import org.shadow.studio.concatenate.backend.builder.MinecraftClientLauncherBuilder
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

inline fun buildMinecraftClientLauncher(config: MinecraftClientLauncherBuilder.() -> Unit): MinecraftClientLauncher {
    return MinecraftClientLauncherBuilder().apply(config).build()
}

inline fun buildMinecraftClientConfiguration(config: MinecraftClientConfigurationBuilder.() -> Unit): MinecraftClientConfiguration {
    return MinecraftClientConfigurationBuilder().apply(config).build()
}

inline fun <T> buildConcatQueue(block: ConcatQueue<T>.() -> Unit): ConcatQueue<T> {
    return ConcatQueue<T>().apply(block)
}

inline fun <T> buildAsyncConcatQueue(block: AsyncConcatQueue<T>.() -> Unit): AsyncConcatQueue<T> {
    return AsyncConcatQueue<T>().apply(block)
}

const val DEFAULT_CONCATE_DOWNLOADER_POOL_SIZE = 64
const val DEFAULT_CONCATE_DOWNLOADER_TASK_TTL = 7
const val DEFAULT_CONCATE_DOWNLOADER_KTOR_BUFFER_SIZE = 256 * 1024L