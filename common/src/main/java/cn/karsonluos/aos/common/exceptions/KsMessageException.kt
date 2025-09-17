package cn.karsonluos.aos.common.exceptions

import androidx.annotation.StringRes
import cn.karsonluos.aos.common.globalContext

class KsMessageException : Exception {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
    constructor(@StringRes messageResId: Int, vararg params :  Any) : super(globalContext.getString(messageResId, *params))
    constructor(@StringRes messageResId: Int, cause: Throwable, vararg params :  Any) : super(globalContext.getString(messageResId, *params), cause)
}