package cn.karsonluos.aos.common.base

import android.graphics.Color
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import cn.karsonluos.aos.common.components.KsLoadingDialog

open class KsBaseActivity : AppCompatActivity() {
    private val mLoadingDialog by lazy {
        KsLoadingDialog()
    }

    protected fun edgeToEdge(dark : Boolean) {
        if (dark){
            enableEdgeToEdge(statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT), navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT))
        }else{
            enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT), navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT))
        }
    }

    protected fun bindBaseViewModel(viewModel: KsBaseViewModel) {
        viewModel.dialogLoadingTasks.observe(this) { tasks ->
            if (tasks.isEmpty()) {
                mLoadingDialog.dismissAllowingStateLoss()
            } else {
                mLoadingDialog.setMessage(tasks.lastOrNull()?.message)
                mLoadingDialog.show(supportFragmentManager, "LoadingDialog")
            }
        }
    }
}

