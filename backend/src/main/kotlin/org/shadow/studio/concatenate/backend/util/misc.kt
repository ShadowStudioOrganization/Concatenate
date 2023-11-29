package org.shadow.studio.concatenate.backend.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import org.shadow.studio.concatenate.backend.builder.MinecraftClientConfigurationBuilder
import org.shadow.studio.concatenate.backend.builder.MinecraftClientLauncherBuilder
import org.shadow.studio.concatenate.backend.launch.MinecraftClientConfiguration
import org.shadow.studio.concatenate.backend.launch.MinecraftClientLauncher
import java.util.concurrent.*
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy
import java.util.concurrent.atomic.AtomicInteger

fun IntRange.toLongRange(): LongRange = LongRange(first.toLong(), last.toLong())
fun LongRange.size(): Long = last - first + 1
fun IntRange.size(): Int = last - first + 1

suspend fun coroutineExecutorsAsync(
    poolSize: Int,
    capacity: Int = 1_000_000,
    poolName: String = "CoroutineExecutor",
    isDaemonThread: Boolean = false,
    job: suspend CoroutineScope.(Int) -> Unit
) {

    val coroutineDispatcher =
        newNamedThreadPool(poolSize, poolSize, capacity, 0L, poolName, isDaemonThread).asCoroutineDispatcher()

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

inline fun <T> multiThreadGenerateTargets(
    poolSize: Int = 24,
    timeoutSecond: Long = 15,
    isDaemonThread: Boolean = false,
    capacity: Int = 1_000_000,
    poolName: String = "MultiThreadGenerateTargets",
    looper: ((() -> T?) -> Unit) -> Unit
): List<T> {

    val pool = newNamedThreadPool(poolSize, poolSize, capacity, 0L, poolName, isDaemonThread)
    val futures = kotlin.collections.buildList<Future<T?>> {
        looper { initializer: () -> T? ->
            this += pool.submit<T?> { initializer() }
        }
    }

    pool.shutdown()
    pool.awaitTermination(timeoutSecond, TimeUnit.MINUTES)

    return futures.mapNotNull { it.get() }
}

fun newNamedThreadPool(coreSize: Int, poolSize: Int, queueCapacity: Int, keepAliveSeconds: Long, poolName: String, isDaemonThread: Boolean): ThreadPoolExecutor {

    return ThreadPoolExecutor(
        coreSize,
        poolSize,
        keepAliveSeconds,
        TimeUnit.SECONDS,
        LinkedBlockingQueue(queueCapacity),
        object : ThreadFactory {

            private val group: ThreadGroup = Thread.currentThread().threadGroup
            private val threadNumber = AtomicInteger(1)
            private val namePrefix: String = "$poolName-thread-"

            override fun newThread(job: Runnable): Thread = Thread(
                group, job,
                namePrefix + threadNumber.getAndIncrement(),
                0
            ).apply {
                isDaemon = isDaemonThread
                if (priority != Thread.NORM_PRIORITY) priority = Thread.NORM_PRIORITY
            }
        },
        AbortPolicy()
    )
}

const val DEFAULT_CONCATE_DOWNLOADER_POOL_SIZE = 64
const val DEFAULT_CONCATE_DOWNLOADER_TASK_TTL = 7
const val DEFAULT_CONCATE_DOWNLOADER_KTOR_BUFFER_SIZE = 256 * 1024L