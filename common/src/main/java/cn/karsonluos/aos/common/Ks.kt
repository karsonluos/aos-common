package cn.karsonluos.aos.common

import android.app.Application
import android.util.TypedValue
import androidx.annotation.StringRes
import cn.karsonluos.aos.common.core.KsArgs
import cn.karsonluos.aos.common.exceptions.KsMessageException
import cn.karsonluos.aos.common.exceptions.KsApiException
import cn.karsonluos.aos.common.utils.KsToastUtil

lateinit var globalContext : Application
fun resToString(@StringRes id: Int, vararg args : Any) : String{
    return globalContext.getString(id, *args)
}

fun Number.dpToPx() : Int{
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), globalContext.resources.displayMetrics).toInt()
}

object Ks {
    var errorMessageFetcher = { throwable : Throwable, defaultMessageId : Int? ->
        val message = when(throwable){
            is KsApiException, is KsMessageException -> throwable.message
            else -> if (defaultMessageId != null){
                resToString(defaultMessageId)
            }else{
                throwable.message
            }
        }

        message ?: resToString(R.string.ks_unknown_error)
    }

    fun init(application: Application){
        globalContext = application
        KsToastUtil.init(application)
        KsArgs.init(application)
    }

    fun errorMessage(throwable : Throwable, @StringRes defaultMessageId : Int? = null) : String{
        return errorMessageFetcher(throwable, defaultMessageId)
    }
}