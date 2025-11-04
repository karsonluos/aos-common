package cn.karsonluos.aos.common.core

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import java.io.Serializable

fun Fragment.intArg(key : String, defaultValue: Int = 0) = KsArgVar(defaultValue, argRegistry(), { bundle -> bundle.getInt(key, defaultValue) }, { bundle, value -> bundle.putInt(key, value) })
fun Fragment.longArg(key : String, defaultValue: Long = 0) = KsArgVar(defaultValue, argRegistry(), { bundle -> bundle.getLong(key, defaultValue) }, { bundle, value -> bundle.putLong(key, value) })
fun Fragment.stringArg(key : String, defaultValue: String? = null) = KsArgVar(defaultValue, argRegistry(), { bundle -> bundle.getString(key, defaultValue) }, { bundle, value -> bundle.putString(key, value) })
fun Fragment.booleanArg(key : String, defaultValue: Boolean = false) = KsArgVar(defaultValue, argRegistry(), { bundle -> bundle.getBoolean(key, defaultValue) }, { bundle, value -> bundle.putBoolean(key, value) })
fun Fragment.floatArg(key : String, defaultValue: Float = 0f) = KsArgVar(defaultValue, argRegistry(), { bundle -> bundle.getFloat(key, defaultValue) }, { bundle, value -> bundle.putFloat(key, value) })
fun Fragment.doubleArg(key : String, defaultValue: Double = 0.0) = KsArgVar(defaultValue, argRegistry(), { bundle -> bundle.getDouble(key, defaultValue) }, { bundle, value -> bundle.putDouble(key, value) })
fun Fragment.bundleArg(key : String, defaultValue: Bundle? = null) = KsArgVar(defaultValue, argRegistry(), { bundle -> bundle.getBundle(key) }, { bundle, value -> bundle.putBundle(key, value) })
fun Fragment.stringArrayArg(key : String, defaultValue: Array<String>? = null) = KsArgVar(defaultValue, argRegistry(), { bundle -> bundle.getStringArray(key) }, { bundle, value -> bundle.putStringArray(key, value) })
fun Fragment.stringArrayListArg(key : String, defaultValue: ArrayList<String>? = null) = KsArgVar(defaultValue, argRegistry(), { bundle -> bundle.getStringArrayList(key) }, { bundle, value -> bundle.putStringArrayList(key, value) })
fun Fragment.intArrayArg(key : String, defaultValue: IntArray? = null) = KsArgVar(defaultValue, argRegistry(), { bundle -> bundle.getIntArray(key) }, { bundle, value -> bundle.putIntArray(key, value) })
fun Fragment.longArrayArg(key : String, defaultValue: LongArray? = null) = KsArgVar(defaultValue, argRegistry(), { bundle -> bundle.getLongArray(key) }, { bundle, value -> bundle.putLongArray(key, value) })
fun Fragment.floatArrayArg(key : String, defaultValue: FloatArray? = null) = KsArgVar(defaultValue, argRegistry(), { bundle -> bundle.getFloatArray(key) }, { bundle, value -> bundle.putFloatArray(key, value) })
fun Fragment.doubleArrayArg(key : String, defaultValue: DoubleArray? = null) = KsArgVar(defaultValue, argRegistry(), { bundle -> bundle.getDoubleArray(key) }, { bundle, value -> bundle.putDoubleArray(key, value) })
fun Fragment.charSequenceArrayArg(key : String, defaultValue: Array<CharSequence>? = null) = KsArgVar(defaultValue, argRegistry(), { bundle -> bundle.getCharSequenceArray(key) }, { bundle, value -> bundle.putCharSequenceArray(key, value) })
fun Fragment.charSequenceArrayListArg(key : String, defaultValue: ArrayList<CharSequence>? = null) = KsArgVar(defaultValue, argRegistry(), { bundle -> bundle.getCharSequenceArrayList(key) }, { bundle, value -> bundle.putCharSequenceArrayList(key, value) })
inline fun <reified T : Parcelable> Fragment.parcelableArrayArg(key : String, defaultValue: Array<T>? = null) = KsArgVar(defaultValue, argRegistry(), { bundle ->
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        bundle.getParcelableArray(key, T::class.java)
    } else {
        bundle.getParcelableArray(key) as Array<T>?
    }
}, { bundle, value -> bundle.putParcelableArray(key, value) })
inline fun <reified T : Parcelable> Fragment.parcelableArrayListArg(key : String, defaultValue: ArrayList<T>? = null) = KsArgVar(defaultValue, argRegistry(), { bundle ->
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        bundle.getParcelableArrayList(key, T::class.java)
    } else {
        bundle.getParcelableArrayList(key)
    }
}, { bundle, value -> bundle.putParcelableArrayList(key, value) })
inline fun <reified T : Parcelable> Fragment.parcelableArg(key : String, defaultValue: T? = null) = KsArgVar(defaultValue, argRegistry(), { bundle ->
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        bundle.getParcelable(key, T::class.java)
    } else {
        bundle.getParcelable(key)
    }
}, { bundle, value -> bundle.putParcelable(key, value) })
inline fun <reified T : Serializable> Fragment.serializableArg(key : String, defaultValue: T? = null) = KsArgVar(defaultValue, argRegistry(), { bundle ->
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        bundle.getSerializable(key, T::class.java)
    } else {
        bundle.getSerializable(key) as T?
    }
}, { bundle, value -> bundle.putSerializable(key, value) })