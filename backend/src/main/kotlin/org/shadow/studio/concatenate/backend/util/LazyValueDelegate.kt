package org.shadow.studio.concatenate.backend.util

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class LazyValueDelegate <V, T> (private val lock: Any? = null, initializer: () -> V): ReadWriteProperty<T, V> {

    @Volatile private var _value: V? = null
    private val _initializer: (() -> V) = initializer

    override fun getValue(thisRef: T, property: KProperty<*>): V {
        return synchronized(lock ?: this) {
            _value ?: run {
                _initializer().apply {
                    _value = this
                }
            }
        }
    }

    override fun setValue(thisRef: T, property: KProperty<*>, value: V) {
        synchronized(lock ?: this) {
            _value = value
        }
    }
}