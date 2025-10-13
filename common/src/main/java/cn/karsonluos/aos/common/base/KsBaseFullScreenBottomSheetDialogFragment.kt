package cn.karsonluos.aos.common.base

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.WindowCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

open class KsBaseFullScreenBottomSheetDialogFragment : BottomSheetDialogFragment() {
    private lateinit var mDialogFragmentStyleConfig : KsDialogFragmentStyleConfig
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDialogFragmentStyleConfig = provideStyleConfig(KsDialogFragmentStyleConfig(
            height = ViewGroup.LayoutParams.MATCH_PARENT
        ))
        setStyle(mDialogFragmentStyleConfig.style, mDialogFragmentStyleConfig.theme)
    }

    protected open fun provideStyleConfig(defaultStyle : KsDialogFragmentStyleConfig) : KsDialogFragmentStyleConfig {
        return defaultStyle
    }

    protected open fun fixedHeight() : Int?{
        return null
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

        val fixedHeight = fixedHeight()
        if (fixedHeight != null){
            val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let { sheet ->
                sheet.setBackgroundColor(Color.TRANSPARENT)
                val behavior = BottomSheetBehavior.from(sheet)
                sheet.layoutParams.height = fixedHeight
                behavior.peekHeight = sheet.layoutParams.height
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }
}