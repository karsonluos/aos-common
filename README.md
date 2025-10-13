[![](https://jitpack.io/v/karsonluos/aos-common.svg)](https://jitpack.io/#karsonluos/aos-common)

### KsRequestUtils.doTask示例
```kotlin
suspend fun cropImage(activity: FragmentActivity, inputUri: Uri) : Uri?{
    val outputUri = Uri.fromFile(File.createTempFile("crop_", ".png", globalContext.externalCacheDir ?: globalContext.cacheDir))
    return ProxyFragmentUtil.doTask(activity){ fragment, callback ->
        val launcher = fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            when (it.resultCode) {
                RESULT_OK -> {
                    val resultUri = UCrop.getOutput(it.data!!)
                    callback.invoke(Result.success(resultUri))
                }
                UCrop.RESULT_ERROR -> {
                    val cropError = UCrop.getError(it.data!!)!!
                    callback.invoke(Result.failure(cropError))
                }
                else -> {
                    callback.invoke(Result.failure(Exception("unknown crop error")))
                }
            }
        }
        UCrop.of(inputUri, outputUri)
            .withOptions(UCrop.Options().apply {
                setCompressionFormat(Bitmap.CompressFormat.PNG)
                setCompressionQuality(100)
                setFreeStyleCropEnabled(true)
            })
            .start(activity, launcher)
    }
}
```