package cn.karsonluos.aos.common.utils

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

object KsReflectUtils {
    @Suppress("UNCHECKED_CAST")
    fun <T> findGenericParameter(
        childClass: Class<*>,
        targetSuperclass: Class<*>,
        index: Int
    ): Class<T> {
        var current: Class<*>? = childClass

        while (current != null && current != Any::class.java) {
            val generic = current.genericSuperclass
            if (generic is ParameterizedType) {
                val rawType = generic.rawType
                if (rawType == targetSuperclass) {
                    val args: Array<Type> = generic.actualTypeArguments
                    if (index in args.indices) {
                        val arg = args[index]
                        if (arg is Class<*>) {
                            return arg as Class<T>
                        }
                    }
                }
            }
            current = current.superclass
        }

        throw IllegalArgumentException(
            "Class ${childClass.name} does not specify generic parameter $index for ${targetSuperclass.name}"
        )
    }
}