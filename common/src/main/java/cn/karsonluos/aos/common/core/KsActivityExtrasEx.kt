package cn.karsonluos.aos.common.core

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable

fun Activity.intArg(key : String, defaultValue: Int = 0) = KsArgVar(defaultValue, argRegistry(), { bundle -> bundle.getInt(key, defaultValue) }, { bundle, value -> bundle.putInt(key, value) })
fun Activity.longArg(key : String, defaultValue: Long = 0) = KsArgVar(defaultValue, argRegistry(), { bundle -> bundle.getLong(key, defaultValue) }, { bundle, value -> bundle.putLong(key, value) })
fun Activity.stringArg(key : String, defaultValue: String? = null) = KsArgVar(defaultValue, argRegistry(), { bundle -> bundle.getString(key, defaultValue) }, { bundle, value -> bundle.putString(key, value) })
fun Activity.booleanArg(key : String, defaultValue: Boolean = false) = KsArgVar(defaultValue, argRegistry(), { bundle -> bundle.getBoolean(key, defaultValue) }, { bundle, value -> bundle.putBoolean(key, value) })
fun Activity.floatArg(key : String, defaultValue: Float = 0f) = KsArgVar(defaultValue, argRegistry(), { bundle -> bundle.getFloat(key, defaultValue) }, { bundle, value -> bundle.putFloat(key, value) })
fun Activity.doubleArg(key : String, defaultValue: Double = 0.0) = KsArgVar(defaultValue, argRegistry(), { bundle -> bundle.getDouble(key, defaultValue) }, { bundle, value -> bundle.putDouble(key, value) })
fun Activity.bundleArg(key : String, defaultValue: Bundle? = null) = KsArgVar(defaultValue, argRegistry(), { bundle -> bundle.getBundle(key) }, { bundle, value -> bundle.putBundle(key, value) })
fun Activity.stringArrayArg(key : String, defaultValue: Array<String>? = null) = KsArgVar(defaultValue, argRegistry(), { bundle -> bundle.getStringArray(key) }, { bundle, value -> bundle.putStringArray(key, value) })
fun Activity.stringArrayListArg(key : String, defaultValue: ArrayList<String>? = null) = KsArgVar(defaultValue, argRegistry(), { bundle -> bundle.getStringArrayList(key) }, { bundle, value -> bundle.putStringArrayList(key, value) })
fun Activity.intArrayArg(key : String, defaultValue: IntArray? = null) = KsArgVar(defaultValue, argRegistry(), { bundle -> bundle.getIntArray(key) }, { bundle, value -> bundle.putIntArray(key, value) })
fun Activity.longArrayArg(key : String, defaultValue: LongArray? = null) = KsArgVar(defaultValue, argRegistry(), { bundle -> bundle.getLongArray(key) }, { bundle, value -> bundle.putLongArray(key, value) })
fun Activity.floatArrayArg(key : String, defaultValue: FloatArray? = null) = KsArgVar(defaultValue, argRegistry(), { bundle -> bundle.getFloatArray(key) }, { bundle, value -> bundle.putFloatArray(key, value) })
fun Activity.doubleArrayArg(key : String, defaultValue: DoubleArray? = null) = KsArgVar(defaultValue, argRegistry(), { bundle -> bundle.getDoubleArray(key) }, { bundle, value -> bundle.putDoubleArray(key, value) })
fun Activity.charSequenceArrayArg(key : String, defaultValue: Array<CharSequence>? = null) = KsArgVar(defaultValue, argRegistry(), { bundle -> bundle.getCharSequenceArray(key) }, { bundle, value -> bundle.putCharSequenceArray(key, value) })
fun Activity.charSequenceArrayListArg(key : String, defaultValue: ArrayList<CharSequence>? = null) = KsArgVar(defaultValue, argRegistry(), { bundle -> bundle.getCharSequenceArrayList(key) }, { bundle, value -> bundle.putCharSequenceArrayList(key, value) })
inline fun <reified T : Parcelable> Activity.parcelableArrayArg(key : String, defaultValue: Array<T>? = null) = KsArgVar(defaultValue, argRegistry(), { bundle ->
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        bundle.getParcelableArray(key, T::class.java)
    } else {
        bundle.getParcelableArray(key)
    }
}, { bundle, value -> bundle.putParcelableArray(key, value) })
inline fun <reified T : Parcelable> Activity.parcelableArrayListArg(key : String, defaultValue: ArrayList<T>? = null) = KsArgVar(defaultValue, argRegistry(), { bundle ->
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        bundle.getParcelableArrayList(key, T::class.java)
    } else {
        bundle.getParcelableArrayList(key)
    }
}, { bundle, value -> bundle.putParcelableArrayList(key, value) })
inline fun <reified T : Parcelable> Activity.parcelableArg(key : String, defaultValue: T? = null) = KsArgVar(defaultValue, argRegistry(), { bundle ->
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        bundle.getParcelable(key, T::class.java)
    } else {
        bundle.getParcelable(key)
    }
}, { bundle, value -> bundle.putParcelable(key, value) })
inline fun <reified T : Serializable> Activity.serializableArg(key : String, defaultValue: T? = null) = KsArgVar(defaultValue, argRegistry(), { bundle ->
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        bundle.getSerializable(key, T::class.java) as T
    } else {
        bundle.getSerializable(key) as T
    }
}, { bundle, value -> bundle.putSerializable(key, value) })