package cn.karsonluos.aos.common.components

import kotlinx.coroutines.*
import kotlin.coroutines.resume

abstract class KsInitRef<T> {
    companion object{
        fun <T> create(block : suspend (onSuccess: (data: T) -> Unit,
                                        onFailed: (ex: Exception) -> Unit)-> T) : KsInitRef<T>{
            return object : KsInitRef<T>(){
                override suspend fun onInit(onSuccess: (T) -> Unit, onFailed: (Exception) -> Unit) {
                    block.invoke(onSuccess, onFailed)
                }
            }
        }
    }

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    // 保存初始化状态
    @Volatile
    private var _instance: T? = null
    private var initJob: Deferred<T>? = null

    /**
     * 实际的初始化逻辑，子类实现
     */
    abstract suspend fun onInit(
        onSuccess: (data: T) -> Unit,
        onFailed: (ex: Exception) -> Unit
    )

    /**
     * 主动触发初始化
     */
    fun init(): KsInitRef<T> {
        startNewJob()
        return this
    }

    private fun startNewJob() : Deferred<T>{
        synchronized(this){
            val currentJob = initJob
            if (currentJob != null && currentJob.isActive){
                return currentJob
            }

            val job =  scope.async {
                suspendCancellableCoroutine { cont ->
                    scope.launch {
                        try {
                            onInit(
                                onSuccess = { data ->
                                    _instance = data
                                    cont.resume(data)
                                },
                                onFailed = { ex ->
                                    cont.resumeWith(Result.failure(ex))
                                }
                            )
                        } catch (ex: Exception) {
                            cont.resumeWith(Result.failure(ex))
                        }
                    }
                }
            }
            initJob = job
            return job
        }
    }

    /**
     * 获取实例：
     * - 已初始化成功 → 返回
     * - 正在初始化 → 等待
     * - 初始化失败 → 重新初始化
     */
    suspend fun instance(): T? {
        // 已经成功
        _instance?.let { return it }

        try {
            return startNewJob().await()
        }catch (ex : Exception){
            ex.printStackTrace()
            return null
        }
    }

    /**
     * 释放资源
     */
    open fun destroy() {
        scope.cancel()
        _instance = null
        initJob = null
    }
}
