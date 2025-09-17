package cn.karsonluos.aos.common.core

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

private const val ARG_REGISTRY = "cn.karsonluos.aos.common.core.ArgRegistry"
class KsArgItem(val inject : (Bundle) -> Unit, val save : (Bundle) -> Unit)

@Suppress("UNCHECKED_CAST")
class KsArgVar<T>(defaultValue: T, registry : KsArgRegistry, private val fetcher : (Bundle) -> T, private val save : (Bundle, T) -> Unit) : ReadWriteProperty<Any, T> {
    private var value : T = defaultValue

    init {
        registry.add(KsArgItem({ bundle -> value = fetcher(bundle) }, { bundle -> save(bundle, value) }))
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return value
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        this.value = value
    }
}

class KsArgRegistry {
    private val registry = mutableListOf<KsArgItem>()

    fun inject(bundle: Bundle) {
        registry.forEach {
            it.inject(bundle)
        }
    }

    fun save(bundle: Bundle) {
        registry.forEach {
            it.save(bundle)
        }
    }

    fun add(argItem: KsArgItem) {
        registry.add(argItem)
    }
}

fun Any.argRegistry() : KsArgRegistry{
    if (this !is Activity && this !is Fragment){
        throw IllegalArgumentException("Only Activity or Fragment can use this method")
    }
    var registry = KsExtras.get<KsArgRegistry>(this, ARG_REGISTRY)
    if (registry == null) {
        registry = KsArgRegistry()
        KsExtras.set(this, ARG_REGISTRY, registry)
    }
    return registry
}


