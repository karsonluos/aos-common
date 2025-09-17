package cn.karsonluos.aos.common.components

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class KsViewBindingAdapter<T, VB: ViewBinding>(val items: MutableList<T> = mutableListOf()) : RecyclerView.Adapter<KsViewBindingViewHolder<VB>>(){
    override fun getItemCount(): Int {
        return items.size
    }
}