package cn.karsonluos.aos.common.base

import android.os.Bundle
import android.view.WindowManager
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.WindowCompat
import androidx.fragment.app.DialogFragment

open class KsBaseFullScreenDialogFragment : DialogFragment() {
    private lateinit var mDialogFragmentStyleConfig : KsDialogFragmentStyleConfig
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDialogFragmentStyleConfig = provideStyleConfig(KsDialogFragmentStyleConfig())
        setStyle(mDialogFragmentStyleConfig.style, mDialogFragmentStyleConfig.theme)
    }

    protected open fun provideStyleConfig(defaultStyle : KsDialogFragmentStyleConfig) : KsDialogFragmentStyleConfig {
        return defaultStyle
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

