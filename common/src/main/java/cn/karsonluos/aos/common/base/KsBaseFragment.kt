package cn.karsonluos.aos.common.base

import androidx.fragment.app.Fragment
import cn.karsonluos.aos.common.components.KsLoadingDialog

open class KsBaseFragment : Fragment() {
    private val mLoadingDialog by lazy {
        KsLoadingDialog()
    }

    protected fun bindBaseViewModel(viewModel: KsBaseViewModel) {
        viewModel.dialogLoadingTasks.observe(this) { tasks ->
            if (tasks.isEmpty()) {
                mLoadingDialog.dismissAllowingStateLoss()
            } else {
                mLoadingDialog.setMessage(tasks.lastOrNull()?.message)
                mLoadingDialog.show(childFragmentManager, "LoadingDialog")
            }
        }
    }
}