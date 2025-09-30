package cn.karsonluos.aos.common.utils

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.UUID
import kotlin.coroutines.resume
import kotlin.let

object KsRequestUtils {
    suspend fun requestPermissions(activity: FragmentActivity, permissions : Array<String>) : Map<String, @JvmSuppressWildcards Boolean>{
        return requireProxyFragment(activity).requestPermissions(permissions)
    }

    suspend fun requestPermission(activity: FragmentActivity, permission : String) : Boolean{
        return requireProxyFragment(activity).requestPermission(permission)
    }
    suspend fun pickVisualMedia(activity: FragmentActivity, request : PickVisualMediaRequest) : Uri?{
        return requireProxyFragment(activity).pickVisualMedia(request)
    }

    suspend fun pickMultiVisualMedia(activity: FragmentActivity, request: PickVisualMediaRequest, maxCount : Int) : List<Uri>?{
        return requireOnceProxyFragment(activity, PickMultiVisualMediaProxyFragment.create(maxCount), "${activity.packageName}_PickMultiVisualMediaProxyFragment")
            .pickMultiVisualMedia(request)
    }

    suspend fun takePicture(activity: FragmentActivity, uri : Uri) : Boolean{
        return requireProxyFragment(activity).takePicture(uri)
    }

    suspend fun takePicturePreview(activity: FragmentActivity) : Bitmap?{
        return requireProxyFragment(activity).takePicturePreview()
    }

    suspend fun startActivityForResult(activity: FragmentActivity, intent: Intent) : ActivityResult{
        return requireProxyFragment(activity).startActivityForResult(intent)
    }

    suspend fun <O> doTask(activity: FragmentActivity, task : (fragment : Fragment, callback : (result : Result<O>)-> Unit)-> Unit) : O{
        return UniversalOnceProxyFragment().execute(activity.supportFragmentManager, task)
    }

    private fun requireProxyFragment(activity: FragmentActivity): RequestProxyFragment {
        val tag = "${activity.packageName}_RequestProxyFragment"
        val fragmentManager = activity.supportFragmentManager
        try {
            fragmentManager.executePendingTransactions()
        }catch (_: Exception){}
        var proxyFragment = fragmentManager.findFragmentByTag(tag)
        if (proxyFragment == null) {
            proxyFragment = RequestProxyFragment()
            fragmentManager.beginTransaction().add(proxyFragment, tag).commit()
        }
        return proxyFragment as RequestProxyFragment
    }

    private fun <T : Fragment> requireOnceProxyFragment(activity: FragmentActivity, fragment: T, tag : String): T {
        val fragmentManager = activity.supportFragmentManager
        try {
            fragmentManager.executePendingTransactions()
        }catch (_: Exception){}
        val oldFragment = fragmentManager.findFragmentByTag(tag)
        if (oldFragment != null) {
            fragmentManager.beginTransaction().remove(oldFragment).add(fragment, tag).commit()
        }
        return fragment
    }
}

private class Launcher<I,O>(
    private val onceMode : Boolean = false,
    private var launcher : ActivityResultLauncher<I>? = null,
    private var conti : CancellableContinuation<O>? = null,
    private var pendingRequest : I? = null
){
    fun register(fragment: Fragment, contract : ActivityResultContract<I, O>){
        val launcher = fragment.registerForActivityResult(contract){
            val conti = this.conti ?: return@registerForActivityResult
            if (conti.isActive){
                conti.resume(it)
            }
            if (onceMode){
                try {
                    fragment.parentFragmentManager.beginTransaction().remove(fragment).commit()
                } catch (e: Exception) {
                    KsLogUtils.e(msg = e.message ?: "", ex = e)
                }
            }
        }
        this.launcher = launcher
        pendingRequest?.let {
            pendingRequest = null
            launcher.launch(it)
        }
    }

    suspend fun request(request : I) : O{
        return withContext(Dispatchers.Main) {
            val result = suspendCancellableCoroutine {
                conti = it
                val launcher = this@Launcher.launcher
                if (launcher == null) {
                    pendingRequest = request
                } else {
                    launcher.launch(request)
                }
            }
            conti = null
            result
        }
    }
}

internal class RequestProxyFragment : Fragment(){
    private val mRequestPermissionsLauncher = Launcher<Array<String>, Map<String, Boolean>>()
    private val mPickVisualMediaLauncher = Launcher<PickVisualMediaRequest, Uri?>()
    private val mTakePictureLauncher = Launcher<Uri, Boolean>()
    private val mTakePicturePreviewLauncher = Launcher<Void?, Bitmap?>()

    private val mStartActivityForResultLauncher = Launcher<Intent, ActivityResult>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mRequestPermissionsLauncher.register(this, ActivityResultContracts.RequestMultiplePermissions())
        mPickVisualMediaLauncher.register(this, ActivityResultContracts.PickVisualMedia())
        mTakePictureLauncher.register(this, ActivityResultContracts.TakePicture())
        mTakePicturePreviewLauncher.register(this, ActivityResultContracts.TakePicturePreview())
        mStartActivityForResultLauncher.register(this, ActivityResultContracts.StartActivityForResult())
    }

    suspend fun requestPermissions(permissions : Array<String>) : Map<String, @JvmSuppressWildcards Boolean> {
        return mRequestPermissionsLauncher.request(permissions)
    }

    suspend fun requestPermission(permission : String) : Boolean {
        val result = requestPermissions(arrayOf(permission))
        return result[permission] ?: false
    }

    suspend fun pickVisualMedia(request : PickVisualMediaRequest) : Uri? {
        return mPickVisualMediaLauncher.request(request)
    }

    suspend fun takePicture(uri : Uri) : Boolean {
        return mTakePictureLauncher.request(uri)
    }

    suspend fun takePicturePreview() : Bitmap? {
        return mTakePicturePreviewLauncher.request(null)
    }

    suspend fun startActivityForResult(intent : Intent) : ActivityResult {
        return mStartActivityForResultLauncher.request(intent)
    }
}

internal class PickMultiVisualMediaProxyFragment : Fragment() {
    companion object{
        private const val KEY_MAX_COUNT = "maxCount"
        fun create(maxCount : Int) : PickMultiVisualMediaProxyFragment{
            return PickMultiVisualMediaProxyFragment().apply {
                arguments = Bundle().apply {
                    putInt(KEY_MAX_COUNT, maxCount)
                }
            }
        }
    }
    private var maxCount = 10
    private val mPickMultiVisualMediaLauncher = Launcher<PickVisualMediaRequest, List<@JvmSuppressWildcards Uri>>(onceMode = true)

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_MAX_COUNT, maxCount)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        maxCount = savedInstanceState?.getInt(KEY_MAX_COUNT) ?: arguments?.getInt(KEY_MAX_COUNT) ?: maxCount
        mPickMultiVisualMediaLauncher.register(this, ActivityResultContracts.PickMultipleVisualMedia(maxCount))
    }

    suspend fun pickMultiVisualMedia(request : PickVisualMediaRequest) : List<@JvmSuppressWildcards Uri> {
        return mPickMultiVisualMediaLauncher.request(request)
    }
}


internal class UniversalOnceProxyFragment : DialogFragment(){
    private var mPendingTask : ((Fragment)-> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val task = mPendingTask
        if (task == null){
            dismiss()
            return
        }else{
            task.invoke(this)
        }
    }

    suspend fun <O> execute(fragmentManager: FragmentManager, task : (fragment : Fragment, callback : (result : Result<O>)-> Unit)-> Unit) : O{
        return suspendCancellableCoroutine { cont->
            cont.invokeOnCancellation {
                dismiss()
            }

            mPendingTask = {fragment->
                task.invoke(fragment){result->
                    if (cont.isActive){
                        cont.resumeWith(result)
                        dismiss()
                    }
                }
            }

            show(fragmentManager, UUID.randomUUID().toString())
        }
    }
}