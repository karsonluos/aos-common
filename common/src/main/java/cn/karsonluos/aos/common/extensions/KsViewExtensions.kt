package cn.karsonluos.aos.common.extensions

import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

fun View.fitSystemBarPadding(left : Boolean = false, top : Boolean = false, right : Boolean = false, bottom : Boolean = false){
    if (!left && !top && !right && !bottom) return
    val originalPaddingTop = paddingTop
    val originalPaddingBottom = paddingBottom
    val originalPaddingLeft = paddingLeft
    val originalPaddingRight = paddingRight
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        val inset = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        v.apply {
            val pl = if(left) (inset.left + originalPaddingLeft) else paddingLeft
            val pt = if(top) (inset.top + originalPaddingTop) else paddingTop
            val pr = if(right) (inset.right + originalPaddingRight) else paddingRight
            val pb = if(bottom) (inset.bottom + originalPaddingBottom) else paddingBottom
            setPadding(pl, pt, pr, pb)
        }
        insets
    }
}

fun View.fitSystemBarMargin(left : Boolean = false, top : Boolean = false, right : Boolean = false, bottom : Boolean = false){
    if (!left && !top && !right && !bottom) return
    val lp = layoutParams
    if (lp !is ViewGroup.MarginLayoutParams) return
    val originalMarginTop = lp.topMargin
    val originalMarginBottom = lp.bottomMargin
    val originalMarginLeft = lp.leftMargin
    val originalMarginRight = lp.rightMargin

    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        val inset = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        v.apply {
            val lp = layoutParams as ViewGroup.MarginLayoutParams
            lp.leftMargin = if(left) (inset.left + originalMarginLeft) else lp.leftMargin
            lp.topMargin = if(top) (inset.top + originalMarginTop) else lp.topMargin
            lp.rightMargin = if(right) (inset.right + originalMarginRight) else lp.rightMargin
            lp.bottomMargin = if(bottom) (inset.bottom + originalMarginBottom) else lp.bottomMargin
            layoutParams = lp
        }
        insets
    }
}