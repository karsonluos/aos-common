package cn.karsonluos.aos.common.utils

import android.util.Log
import cn.karsonluos.aos.common.globalContext

object KsLogUtils {
    private val DEFAULT_TAG = globalContext.packageName

    @JvmStatic
    fun d(msg: String, tag: String? = null, ex: Throwable? = null) {
        Log.d(tag ?: DEFAULT_TAG, msg, ex)
    }

    @JvmStatic
    fun w(msg: String, tag: String? = null, ex: Throwable? = null) {
        Log.w(tag ?: DEFAULT_TAG, msg, ex)
    }

    @JvmStatic
    fun i(msg: String, tag: String? = null, ex: Throwable? = null) {
        Log.i(tag ?: DEFAULT_TAG, msg, ex)
    }

    @JvmStatic
    fun e(msg: String, tag: String? = null, ex: Throwable? = null) {
        Log.e(tag ?: DEFAULT_TAG, msg, ex)
    }
}