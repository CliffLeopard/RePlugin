package com.qihoo360.replugin.transform.bean

import com.qihoo360.replugin.Log
import com.qihoo360.replugin.config.BaseExtension
import java.util.*

/**
 * author:gaoguanling
 * date:2021/9/12
 * time:09:37
 * email:gaoguanling@360.cn
 * link:
 */
open class InstrumentationContext(
    val classInfo: TransformClassInfo,
    open val extension: BaseExtension
) {
    private val skippedMethods: HashMap<String, String> = hashMapOf()
    var superClassName: String? = null
    var classModified: Boolean = false
    var skipClass: Boolean = false

    fun addSkippedMethod(name: String, desc: String) {
        Log.i(
            "InstrumentationContext",
            "Will skip all tracing in method  ${classInfo.name}#$name:$desc"
        )
        skippedMethods[classInfo.name + "#" + name] = desc
    }

    fun isSkippedMethod(name: String, desc: String): Boolean {
        return searchMethodMap(skippedMethods, name, desc)
    }

    private fun searchMethodMap(map: Map<String, String>, name: String, desc: String): Boolean {
        val methodDesc = map[classInfo.name + "#" + name]
        return methodDesc != null && methodDesc == desc
    }
}