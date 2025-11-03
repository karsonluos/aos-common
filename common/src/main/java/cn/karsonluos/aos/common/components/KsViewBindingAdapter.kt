package cn.karsonluos.aos.common.components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import cn.karsonluos.aos.common.extensions.viewBinding
import cn.karsonluos.aos.common.utils.KsReflectUtils

abstract class KsViewBindingAdapter<T, VB: ViewBinding>(val items: MutableList<T> = mutableListOf()) : RecyclerView.Adapter<KsViewBindingViewHolder<VB>>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KsViewBindingViewHolder<VB> {
        val vbClazz = KsReflectUtils.findGenericParameter<VB>(javaClass, KsViewBindingAdapter::class.java, 1)
        val layoutInflater = LayoutInflater.from(parent.context)
        val vb = vbClazz.viewBinding(layoutInflater)
        return KsViewBindingViewHolder(vb)
    }

    fun replaceAll(newData : List<T>){
        val oldCount = this.items.size
        this.items.clear()
        this.notifyItemRangeRemoved(0, oldCount)
        this.items.addAll(newData)
        this.notifyItemRangeInserted(0, newData.size)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}