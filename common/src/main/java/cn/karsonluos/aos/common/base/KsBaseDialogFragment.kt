package cn.karsonluos.aos.common.base

import cn.karsonluos.aos.common.components.KsLoadingDialog

open class KsBaseDialogFragment : KsBaseFullScreenDialogFragment() {
    private val mKsLoadingDialog by lazy {
        KsLoadingDialog()
    }

    protected fun bindBaseViewModel(viewModel: KsBaseViewModel) {
        viewModel.dialogLoadingTasks.observe(this) { tasks ->
            if (tasks.isEmpty()) {
                mKsLoadingDialog.dismissAllowingStateLoss()
            } else {
                mKsLoadingDialog.setMessage(tasks.lastOrNull()?.message)
                mKsLoadingDialog.show(childFragmentManager, "LoadingDialog")
            }
        }
    }
}