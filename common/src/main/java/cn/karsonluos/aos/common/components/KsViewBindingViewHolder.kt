package cn.karsonluos.aos.common.components

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class KsViewBindingViewHolder<T: ViewBinding>(val viewBinding: T) : RecyclerView.ViewHolder(viewBinding.root)