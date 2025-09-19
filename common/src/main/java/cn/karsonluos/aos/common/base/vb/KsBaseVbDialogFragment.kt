package cn.karsonluos.aos.common.base.vb

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import cn.karsonluos.aos.common.base.KsBaseDialogFragment
import cn.karsonluos.aos.common.extensions.viewBinding
import cn.karsonluos.aos.common.utils.KsReflectUtils

open class KsBaseVbDialogFragment<T: ViewBinding> : KsBaseDialogFragment() {
    protected lateinit var mViewBinding : T

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val vbClazz = KsReflectUtils.findGenericParameter<T>(javaClass, KsBaseVbDialogFragment::class.java, 0)
        val vb = vbClazz.viewBinding(layoutInflater)
        this.mViewBinding = vb
        return vb.root
    }
}