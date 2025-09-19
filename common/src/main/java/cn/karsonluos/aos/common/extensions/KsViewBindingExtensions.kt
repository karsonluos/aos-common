package cn.karsonluos.aos.common.extensions

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import cn.karsonluos.aos.common.components.KsViewBindingViewHolder

fun <T: ViewBinding> Class<T>.viewBinding(parent: ViewGroup, attach : Boolean = false) : T{
    val method = this.getDeclaredMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
    @Suppress("UNCHECKED_CAST")
    return method.invoke(null, LayoutInflater.from(parent.context), parent, attach) as T
}

fun <T: ViewBinding> Class<T>.viewBinding(inflater: LayoutInflater) : T{
    val method = getDeclaredMethod("inflate", LayoutInflater::class.java)
    @Suppress("UNCHECKED_CAST")
    return method.invoke(null, inflater) as T
}

fun <T: ViewBinding> Class<T>.viewBinding(context: Context) : T{
    return this.viewBinding(LayoutInflater.from(context))
}

inline fun <reified T: ViewBinding> viewbinding(parent: ViewGroup, attach : Boolean = false) : T{
    val clazz = T::class.java
    val method = clazz.getDeclaredMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
    return method.invoke(null, LayoutInflater.from(parent.context), parent, attach) as T
}

inline fun <reified T: ViewBinding> viewbinding(context: Context) : T{
    return viewbinding(LayoutInflater.from(context))
}

inline fun <reified T: ViewBinding> viewbinding(inflater: LayoutInflater) : T{
    val clazz = T::class.java
    val method = clazz.getDeclaredMethod("inflate", LayoutInflater::class.java)
    return method.invoke(null, inflater) as T
}

fun <T: ViewBinding> T.toViewHolder() : KsViewBindingViewHolder<T>{
    return KsViewBindingViewHolder(this)
}

inline fun <reified T: ViewBinding> viewBindingHolder(parent: ViewGroup, attach : Boolean = false) : KsViewBindingViewHolder<T>{
    return viewbinding<T>(parent, attach).toViewHolder()
}