package org.shadow.studio.concatenate.backend.util

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.ArrayDeque

open class AsyncConcatQueue<T>(deque: ArrayDeque<T> = ArrayDeque()): ConcatQueue<T>(deque) {

    private val mutex = Mutex()

    suspend fun enqueueAsync(element: T) = mutex.withLock {
        super.enqueue(element)
    }

    suspend fun dequeueAsync(): T? = mutex.withLock {
        return super.dequeue()
    }
}