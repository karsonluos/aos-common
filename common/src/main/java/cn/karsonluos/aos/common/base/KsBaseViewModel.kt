package cn.karsonluos.aos.common.base

import android.app.Application
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import cn.karsonluos.aos.common.Ks
import cn.karsonluos.aos.common.R
import cn.karsonluos.aos.common.components.KsSuperLiveData
import cn.karsonluos.aos.common.resToString
import cn.karsonluos.aos.common.utils.KsLogUtils
import cn.karsonluos.aos.common.utils.KsToastUtil
import java.util.UUID

enum class KsLoadingStatus{
    LOADING,
    SUCCESS,
    FAILED
}

data class KsErrorExtra(val exception : Throwable, val message : String, val onRetry : (() -> Unit))
data class KsLoadingTask(val id : String, val message : String? = null)
data class KsContentLoadingState(val status : KsLoadingStatus, val loadingMessage : String? = null, val errorExtra : KsErrorExtra? = null){
    companion object{
        fun success() : KsContentLoadingState{
            return KsContentLoadingState(KsLoadingStatus.SUCCESS)
        }

        fun loading(message : String? = null) : KsContentLoadingState{
            return KsContentLoadingState(KsLoadingStatus.LOADING, message)
        }

        fun failed(exception : Throwable, message : String = Ks.errorMessage( exception), onRetry : (() -> Unit)) : KsContentLoadingState{
            return KsContentLoadingState(KsLoadingStatus.FAILED, null, KsErrorExtra(exception, message,  onRetry))
        }
    }

    fun isLoading() : Boolean{
        return status == KsLoadingStatus.LOADING
    }
}
open class KsBaseViewModel(application: Application) : AndroidViewModel(application) {
    val dialogLoadingTasks = KsSuperLiveData<MutableList<KsLoadingTask>>()

    fun showLoading(message : String? = null, id : String = UUID.randomUUID().toString()) : String {
        dialogLoadingTasks.postValue{oldTasks, hasValue ->
            if (hasValue){
                val tasks = oldTasks ?: mutableListOf()
                tasks.add(KsLoadingTask(id, message))
                return@postValue tasks
            }else{
                return@postValue mutableListOf(KsLoadingTask(id, message))
            }
        }
        return id
    }

    fun dismissLoading(id : String) {
        dialogLoadingTasks.postValue{oldTasks, hasValue ->
            if (hasValue){
                val tasks = oldTasks ?: mutableListOf()
                tasks.removeAll{it.id == id}
                return@postValue tasks
            }else{
                return@postValue mutableListOf()
            }
        }
    }

    fun withContentLoadingTask(@StringRes defaultFailedMessageId : Int = R.string.ks_unknown_error, liveData : MutableLiveData<KsContentLoadingState>?, task : suspend ()-> Unit){
        viewModelScope.launch {
            liveData?.postValue(KsContentLoadingState.loading(resToString(R.string.ks_loading)))
            try {
                task.invoke()
                liveData?.postValue(KsContentLoadingState.success())
            }catch (ex : Throwable){
                KsLogUtils.e("任务执行失败", ex=ex)
                liveData?.postValue(KsContentLoadingState.failed(ex, Ks.errorMessage(ex,defaultFailedMessageId)){
                    withContentLoadingTask(defaultFailedMessageId, liveData, task)
                })
            }
        }
    }

    fun withDialogTask(@StringRes defaultFailedMessageId : Int = R.string.ks_unknown_error, onFailed : (Throwable)-> Unit = {
        KsLogUtils.e("任务执行失败", ex=it)
        KsToastUtil.error(Ks.errorMessage(it,defaultFailedMessageId))
    }, task : suspend ()-> Unit){
        viewModelScope.launch {
            val taskId = showLoading()
            try {
                task.invoke()
            }catch (ex : Throwable){
                onFailed.invoke(ex)
            }finally {
                dismissLoading(taskId)
            }
        }
    }

    fun withSilentTask(onFailed : (Throwable)-> Unit = {
        KsLogUtils.e("任务执行失败", ex=it)
    }, task : suspend ()-> Unit){
        viewModelScope.launch {
            try {
                task.invoke()
            }catch (ex : Throwable){
                onFailed.invoke(ex)
            }
        }
    }
}