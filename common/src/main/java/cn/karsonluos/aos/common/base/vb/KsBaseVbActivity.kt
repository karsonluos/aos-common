package cn.karsonluos.aos.common.base.vb

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import cn.karsonluos.aos.common.base.KsBaseActivity
import cn.karsonluos.aos.common.extensions.viewBinding
import cn.karsonluos.aos.common.utils.KsReflectUtils

open class KsBaseVbActivity<T : ViewBinding> : KsBaseActivity(){
    protected val mViewBinding by lazy {
        val vbClazz = KsReflectUtils.findGenericParameter<T>(javaClass, KsBaseVbActivity::class.java, 0)
        vbClazz.viewBinding(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mViewBinding.root)
    }
}