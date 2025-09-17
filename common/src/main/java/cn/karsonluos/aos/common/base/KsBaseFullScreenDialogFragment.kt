package cn.karsonluos.aos.common.base

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.WindowCompat
import androidx.fragment.app.DialogFragment
import cn.karsonluos.aos.common.R

open class KsBaseFullScreenDialogFragment : DialogFragment() {
    private lateinit var mDialogFragmentStyleConfig : KsDialogFragmentStyleConfig
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDialogFragmentStyleConfig = provideStyleConfig()
        setStyle(mDialogFragmentStyleConfig.style, mDialogFragmentStyleConfig.theme)
    }

    protected open fun provideStyleConfig() : KsDialogFragmentStyleConfig {
        return KsDialogFragmentStyleConfig()
    }

    override fun onStart() {
        super.onStart()
        val dialog = requireDialog()
        val window = dialog.window
        if (window == null) return
        window.setDimAmount(mDialogFragmentStyleConfig.dimAmount)
        if (mDialogFragmentStyleConfig.dimAmount > 0){
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }else{
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }
        window.setBackgroundDrawable(mDialogFragmentStyleConfig.windowBackgroundColor.toDrawable())
        window.setLayout(
            mDialogFragmentStyleConfig.width,
            mDialogFragmentStyleConfig.height
        )
        window.setGravity(mDialogFragmentStyleConfig.gravity)
        window.setWindowAnimations(mDialogFragmentStyleConfig.animation)
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }
}

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