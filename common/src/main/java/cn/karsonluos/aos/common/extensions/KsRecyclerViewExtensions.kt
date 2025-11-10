package cn.karsonluos.aos.common.extensions

import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.removeAllItemDecorations(){
    while (itemDecorationCount > 0){
        removeItemDecorationAt(0)
    }
}

fun RecyclerView.setItemDecoration(itemDecoration: RecyclerView.ItemDecoration){
    removeAllItemDecorations()
    addItemDecoration(itemDecoration)
}