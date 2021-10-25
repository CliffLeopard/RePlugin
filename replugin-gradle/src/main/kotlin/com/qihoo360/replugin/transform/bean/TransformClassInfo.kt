package com.qihoo360.replugin.transform.bean

import com.android.build.api.transform.QualifiedContent

/**
 * author:gaoguanling
 * date:2021/8/4
 * time:22:44
 * email:gaoguanling@360.cn
 * link:
 */
data class TransformClassInfo(
    val name:String,
    val packageName: String,
    val className: String,
    val fromPath: String,
    val toPath: String,
    val content:QualifiedContent,
    val fromJar: Boolean = false,
    val isInnerClass: Boolean = false,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as TransformClassInfo

        if (packageName != other.packageName) return false
        if (className != other.className) return false
        if (fromPath != other.fromPath) return false
        if (toPath != other.toPath) return false
        if (fromJar != other.fromJar) return false
        if (isInnerClass != other.isInnerClass) return false

        return true
    }

    override fun hashCode(): Int {
        var result = packageName.hashCode()
        result = 31 * result + className.hashCode()
        result = 31 * result + fromPath.hashCode()
        result = 31 * result + toPath.hashCode()
        result = 31 * result + fromJar.hashCode()
        result = 31 * result + isInnerClass.hashCode()
        return result
    }
}
