package org.shadow.studio.concatenate.backend.util


class ListBuilder<T>(private val addTo: MutableList<T>) {
    operator fun List<T>.unaryPlus() {
        addTo.addAll(this)
    }
    operator fun T.unaryPlus() {
        addTo.add(this)
    }
}

inline fun <T> buildList(block: ListBuilder<T>.() -> Unit): List<T> {
    return mutableListOf<T>().apply {
        block(ListBuilder(this))
    }
}