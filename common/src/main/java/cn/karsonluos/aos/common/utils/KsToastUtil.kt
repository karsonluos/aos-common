package cn.karsonluos.aos.common.utils

import android.app.Application
import android.widget.Toast

object KsToastUtil {
    private lateinit var mApplication: Application

    fun init(application: Application) {
        this.mApplication = application
    }

    fun info(message: String) {
        Toast.makeText(mApplication, message, Toast.LENGTH_SHORT).show()
    }

    fun info(resId: Int, vararg args: Any) {
        info(mApplication.resources.getString(resId, args))
    }

    fun success(message: String) {
        Toast.makeText(mApplication, message, Toast.LENGTH_SHORT).show()
    }

    fun success(resId: Int, vararg args: Any) {
        info(mApplication.resources.getString(resId, args))
    }

    fun error(message: String) {
        Toast.makeText(mApplication, message, Toast.LENGTH_SHORT).show()
    }

    fun error(resId: Int, vararg args: Any) {
        info(mApplication.resources.getString(resId, args))
    }

    fun warn(message: String) {
        Toast.makeText(mApplication, message, Toast.LENGTH_SHORT).show()
    }

    fun warn(resId: Int, vararg args: Any) {
        info(mApplication.resources.getString(resId, args))
    }
}