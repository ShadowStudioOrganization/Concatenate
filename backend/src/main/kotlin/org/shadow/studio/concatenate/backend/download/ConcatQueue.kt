package org.shadow.studio.concatenate.backend.download

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.*


inline fun <T> buildConcatQueue(block: ConcatQueue<T>.() -> Unit): ConcatQueue<T> {
    return ConcatQueue<T>().apply(block)
}

inline fun <T> buildAsyncConcatQueue(block: AsyncConcatQueue<T>.() -> Unit): AsyncConcatQueue<T> {
    return AsyncConcatQueue<T>().apply(block)
}

open class ConcatQueue<T>(protected val deque: ArrayDeque<T> = ArrayDeque()): Queue<T> by deque {

    open fun enqueue(element: T) {
        deque.addLast(element)
    }

    open fun dequeue(): T? {
        return if (deque.isNotEmpty()) {
            deque.removeFirst()
        } else null
    }
}

open class AsyncConcatQueue<T>(deque: ArrayDeque<T> = ArrayDeque()): ConcatQueue<T>(deque) {

    private val mutex = Mutex()

    suspend fun enqueueAsync(element: T) = mutex.withLock {
        super.enqueue(element)
    }

    suspend fun dequeueAsync(): T? = mutex.withLock {
        return super.dequeue()
    }
}