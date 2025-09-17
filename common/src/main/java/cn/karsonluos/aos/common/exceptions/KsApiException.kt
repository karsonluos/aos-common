package cn.karsonluos.aos.common.exceptions

class KsApiException(message: String, private val body : Any? = null) : Exception(message) {
    fun <T> getBody() : T{
        @Suppress("UNCHECKED_CAST")
        return body as T
    }
}