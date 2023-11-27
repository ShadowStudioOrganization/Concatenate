package org.shadow.studio.concatenate.backend.util

import java.util.*


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

