package com.qihoo360.replugin.config

import com.qihoo360.replugin.Log

/**
 * author:gaoguanling
 * date:2022/1/25
 * time:16:26
 * email:gaoguanling@360.cn
 * link:
 */
open class HookLambda(var name: String) {
    var className: String = ""
    var methodName: String = ""
    var methodDesc: String = ""
    var lambdaMethodName: String = ""
    var lambdaMethodDesc: String = ""
    var targetClass: String = ""
    var targetMethod: String = ""
    var hookType: Int = HookType.BEFORE.value

    constructor(
        name: String, className: String, methodName: String, methodDesc: String,
        lambdaMethod: String, lambdaMethodDesc: String, targetClass: String, targetMethod: String,
        hookType: Int
    ) : this(name) {
        this.className = className
        this.methodName = methodName
        this.methodDesc = methodDesc
        this.lambdaMethodName = lambdaMethod
        this.lambdaMethodDesc = lambdaMethodDesc
        this.targetClass = targetClass
        this.targetMethod = targetMethod
        this.hookType = hookType
    }

    enum class HookType(val value: Int) {
        BEFORE(0x01),
        AFTER(0x02),
        REPLACE(0x3)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as HookLambda
        return name == other.name
                && className == other.className
                && methodName == other.methodName && methodDesc == other.methodDesc
                && lambdaMethodName == other.lambdaMethodName
                && lambdaMethodDesc == other.lambdaMethodDesc
                && targetClass == other.targetClass
                && targetMethod == other.targetMethod
                && hookType == other.hookType
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + className.hashCode()
        result = 31 * result + methodName.hashCode()
        result = 31 * result + methodDesc.hashCode()
        result = 31 * result + lambdaMethodName.hashCode()
        result = 31 * result + lambdaMethodDesc.hashCode()
        result = 31 * result + targetClass.hashCode()
        result = 31 * result + targetMethod.hashCode()
        result = 31 * result + hookType.hashCode()
        return result
    }

    override fun toString(): String {
        return "HookLambda(name='$name', className='$className', methodName='$methodName', methodDesc='$methodDesc',lambdaMethod='$lambdaMethodName',lambdaMethodDesc='$lambdaMethodDesc',targetClass='$targetClass',targetMethod='$targetMethod',hookType='$hookType')"
    }

    companion object {
        fun getHookTypeByTypeValue(type: Int): HookType {
            return when (type) {
                0x01 -> HookType.BEFORE
                0x02 -> HookType.AFTER
                0x03 -> HookType.REPLACE
                else -> {
                    Log.e("HookLambda", "HookType Not Available")
                    HookType.BEFORE
                }
            }
        }
    }
}