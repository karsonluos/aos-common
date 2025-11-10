package cn.karsonluos.aos.common.utils

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri
import cn.karsonluos.aos.common.R

object KsSystemUtils {
    fun viewH5(context : Context, chooserTitle : String, url: String) {
        val uri: Uri = url.toUri()
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.addCategory(Intent.CATEGORY_BROWSABLE)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val chooserIntent = Intent.createChooser(intent,chooserTitle)
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context.startActivity(chooserIntent)
        }catch (_: ActivityNotFoundException){
            KsToastUtil.error(R.string.ks_not_found_web_client)
        }
    }

    @SuppressLint("UseKtx")
    fun sendEmail(context: Context, to : String, subject : String, text : String = "", chooserTitle: String){
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            // 这里用 Uri.parse，而不是 toUri()，用toUri gmail不能识别
            data = Uri.parse("mailto:${to}")
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT,text)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context.startActivity(Intent.createChooser(intent, chooserTitle).also {
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            })
        } catch (_: ActivityNotFoundException) {
            KsToastUtil.error(R.string.ks_not_found_email_client)
        }
    }
}