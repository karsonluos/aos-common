package cn.karsonluos.aos.common.components

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import cn.karsonluos.aos.common.R
import cn.karsonluos.aos.common.base.KsBaseFullScreenDialogFragment
import cn.karsonluos.aos.common.base.KsDialogFragmentStyleConfig


open class KsLoadingDialog : KsBaseFullScreenDialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (savedInstanceState != null) {
            //不允许重建
            dismissAllowingStateLoss()
        }
        return inflater.inflate(provideContentLayout(), container, false)
    }

    protected open fun provideContentLayout(): Int {
        return R.layout.ks_dialog_loading
    }

    override fun provideStyleConfig(defaultStyle: KsDialogFragmentStyleConfig): KsDialogFragmentStyleConfig {
        return defaultStyle.also {
            it.height = ViewGroup.LayoutParams.MATCH_PARENT
            it.gravity= Gravity.CENTER
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        this.showNow(manager, tag)
    }

    @Deprecated("", level = DeprecationLevel.HIDDEN)
    override fun show(transaction: FragmentTransaction, tag: String?): Int {
        throw IllegalStateException("don't allow use this method")
    }

    override fun showNow(manager: FragmentManager, tag: String?) {
        try {
            manager.executePendingTransactions()
        }catch (_ : Exception){}

        if (manager.findFragmentByTag(tag) == null && !isAdded) {
            try {
                super.showNow(manager, tag)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }

    override fun dismiss() {
        this.dismissAllowingStateLoss()
    }

    override fun dismissAllowingStateLoss() {
        try {
            super.dismissAllowingStateLoss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}