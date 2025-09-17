package cn.karsonluos.aos.common.components

import androidx.lifecycle.MutableLiveData

open class KsSuperLiveData<T> : MutableLiveData<T> {
    companion object{
        val UN_SET = Object()
    }

    @Volatile
    private var latestValue : Any? = UN_SET

    constructor():super() {
        this.latestValue = UN_SET
    }

    constructor(value : T) : this(){
        this.latestValue = value
    }

    override fun setValue(value: T) {
        this.latestValue = value
        super.setValue(value)
    }
    override fun postValue(value: T) {
        this.latestValue = value
        super.postValue(value)
    }

    fun hasValidValue() : Boolean {
        return latestValue != UN_SET
    }

    @Suppress("UNCHECKED_CAST")
    fun getLastValueOrThrow() : T{
        if (latestValue == UN_SET) {
            throw IllegalStateException("No value present")
        }
        return latestValue as T
    }

    @Suppress("UNCHECKED_CAST")
    fun getLastValueWith(defaultValue : T) : T{
        if (latestValue == UN_SET) {
            return defaultValue
        }
        return latestValue as T
    }

    @Suppress("UNCHECKED_CAST")
    fun getLastValueOrNull() : T?{
        if (latestValue == UN_SET) {
            return null
        }
        return latestValue as T
    }

    @Suppress("USELESS_CAST")
    fun postValue(setter : (T?, Boolean)  -> T) {
        val hasValue = hasValidValue()
        val currentValue = if (hasValue) null else value
        val value = setter(currentValue as T?, hasValue)
        super.postValue(value)
    }
}