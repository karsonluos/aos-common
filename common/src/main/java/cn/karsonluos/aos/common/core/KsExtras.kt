package cn.karsonluos.aos.common.core

import java.util.WeakHashMap

object KsExtras {
    private val extras = WeakHashMap<Any, MutableMap<String, Any?>>()

    fun set(target : Any, key: String, value: Any?) {
        val map = extras.getOrPut(target) { mutableMapOf() }
        map[key] = value
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> get(target : Any, key: String): T? {
        return extras[target]?.get(key) as T?
    }
}
