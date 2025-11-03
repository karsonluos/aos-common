package cn.karsonluos.aos.common.utils

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import cn.karsonluos.aos.common.exceptions.KsMessageException
import cn.karsonluos.aos.common.globalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID

object KsStorageUtil {
    fun getCacheDir(): File{
        return globalContext.externalCacheDir ?: globalContext.cacheDir
    }

    fun getFileDir(type : String? = null): File{
        return globalContext.getExternalFilesDir(type) ?: if (type != null){
            File(globalContext.filesDir, type).apply {
                mkdirs()
            }
        }else globalContext.filesDir
    }

    fun ensureFileExist(file : File) : Boolean{
        if (file.exists()) return true
        val parentFile = file.parentFile
        if (parentFile != null && !parentFile.exists()){
            val createDirResult = parentFile.mkdirs()
            if (!createDirResult){
                return false
            }
        }
        return file.createNewFile()
    }

    suspend fun saveImageToGallery(activity: FragmentActivity, uri: Uri, fileName : String = "${UUID.randomUUID()}.png", mimeType : String = "image/png", relativePath : String? = null): Result<Unit> = withContext(Dispatchers.IO)  {
        try {
            // API 29+ (Android 10+) 使用Scoped Storage，不需要存储权限
            // API 21-28 需要WRITE_EXTERNAL_STORAGE权限
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                // 检查是否已有权限
                if (ContextCompat.checkSelfPermission(
                        activity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // 请求权限
                    val granted = KsRequestUtils.requestPermission(
                        activity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    if (!granted) {
                        return@withContext Result.failure(KsMessageException("授予应用存储权限后再试"))
                    }
                }
            }

            val contentResolver = activity.contentResolver
            val inputStream = contentResolver.openInputStream(uri) ?: return@withContext Result.failure(KsMessageException("无效的Uri"))
            inputStream.use {
                // 创建文件信息
                val displayName = fileName
                // 使用MediaStore API保存（API 21+都支持）
                val contentValues = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, displayName)
                    put(MediaStore.Images.Media.MIME_TYPE, mimeType)

                    // API 29+ 使用RELATIVE_PATH，不需要WRITE_EXTERNAL_STORAGE权限
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        if (relativePath != null){
                            put(MediaStore.Images.Media.RELATIVE_PATH, relativePath)
                        }
                        put(MediaStore.Images.Media.IS_PENDING, 1)
                    }
                }

                val imageUri = contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues
                ) ?: return@withContext Result.failure(KsMessageException("写入存储失败"))

                // 写入文件内容
                contentResolver.openOutputStream(imageUri)?.use { outputStream ->
                    inputStream.copyTo(outputStream)
                }

                // API 29+ 需要取消pending状态
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    contentValues.clear()
                    contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                    contentResolver.update(imageUri, contentValues, null, null)
                }

                return@withContext Result.success(Unit)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext Result.failure(e)
        }
    }
}