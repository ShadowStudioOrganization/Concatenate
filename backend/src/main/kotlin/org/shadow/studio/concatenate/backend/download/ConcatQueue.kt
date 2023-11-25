package org.shadow.studio.concatenate.backend.download

import java.util.*

class ConcatQueue<T>(private val deque: ArrayDeque<T> = ArrayDeque()): Queue<T> by deque {

    fun enqueue(element: T) {
        deque.addLast(element)
    }

    fun dequeue(): T? {
        return if (deque.isNotEmpty()) {
            deque.removeFirst()
        } else null
    }
}