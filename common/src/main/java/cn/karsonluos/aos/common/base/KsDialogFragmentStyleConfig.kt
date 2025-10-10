package cn.karsonluos.aos.common.base

import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import cn.karsonluos.aos.common.R

data class KsDialogFragmentStyleConfig(
    var style : Int = DialogFragment.STYLE_NORMAL,
    var theme : Int = R.style.Ks_DialogFragmentFullScreen,
    var dimAmount : Float = 0.5f,
    var windowBackgroundColor : Int = Color.TRANSPARENT,
    var width: Int = ViewGroup.LayoutParams.MATCH_PARENT,
    var height: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
    var gravity : Int = Gravity.BOTTOM,
    var animation : Int = if (gravity and Gravity.BOTTOM == Gravity.BOTTOM){
        R.style.Ks_BottomDialogSlideAnimation
    }else if (gravity and Gravity.CENTER_VERTICAL == Gravity.CENTER_VERTICAL && height != ViewGroup.LayoutParams.MATCH_PARENT){
        R.style.Ks_CenterDialogAnimation
    }else{
        R.style.Ks_DialogFadeAnimation
    }
)