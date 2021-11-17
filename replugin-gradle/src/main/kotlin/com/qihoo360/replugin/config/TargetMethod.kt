package com.qihoo360.replugin.config

/**
 * author:gaoguanling
 * date:2021/11/15
 * time:17:53
 * email:gaoguanling@360.cn
 * link:
 */
open class TargetMethod(var name: String) {
    var className: String = "com/qihoo360/replugin/base/method/Origin"
    var methodName: String = "f"
    var methodDesc: String = "(Lcom/qihoo360/replugin/base/method/A;Lcom/qihoo360/replugin/base/method/B;)Lcom/qihoo360/replugin/base/method/R"
    var targetClassName: String = "com/qihoo360/replugin/base/method/Target"
    var targetMethodName: String = "f"
    var targetMethodDesc: String = "(Lcom/qihoo360/replugin/base/method/A;Lcom/qihoo360/replugin/base/method/B;)Lcom/qihoo360/replugin/base/method/R"
    var hookType: Int = HookType.DEFINE_NORMAL_BEFORE_METHOD.value

    constructor(
        name: String,
        className: String, methodName: String, methodDesc: String,
        targetClassName: String, targetMethodName: String, targetMethodDesc: String,
        hookType: Int
    ) : this(name) {
        this.className = className
        this.methodName = methodName
        this.methodDesc = methodDesc
        this.targetClassName = targetClassName
        this.targetMethodName = targetMethodName
        this.targetMethodDesc = targetMethodDesc
        this.hookType = hookType
    }

    enum class HookType(val value: Int) {
        DEFINE_NORMAL_BEFORE_METHOD(0x01),
        DEFINE_NORMAL_AFTER_METHOD(0x02),
        DEFINE_NORMAL_REPLACE_METHOD(0x03),
        DEFINE_STATIC_BEFORE_METHOD(0x04),
        DEFINE_STATIC_AFTER_METHOD(0x05),
        DEFINE_STATIC_REPLACE_METHOD(0x06),
        CALL_NORMAL_BEFORE_METHOD(0x07),
        CALL_NORMAL_AFTER_METHOD(0x08),
        CALL_NORMAL_REPLACE_METHOD(0x09),
        CALL_STATIC_BEFORE_METHOD(0x0a),
        CALL_STATIC_AFTER_METHOD(0x0b),
        CALL_STATIC_REPLACE_METHOD(0x0c)
    }

    companion object {
        fun getHookTypeByValue(value: Int): HookType {
            return when (value) {
                0x01 ->
                    HookType.DEFINE_NORMAL_BEFORE_METHOD
                0x02 ->
                    HookType.DEFINE_NORMAL_AFTER_METHOD
                0x03 ->
                    HookType.DEFINE_NORMAL_REPLACE_METHOD
                0x04 ->
                    HookType.DEFINE_STATIC_BEFORE_METHOD
                0x05 ->
                    HookType.DEFINE_STATIC_AFTER_METHOD
                0x06 ->
                    HookType.DEFINE_STATIC_REPLACE_METHOD
                0x07 ->
                    HookType.CALL_NORMAL_BEFORE_METHOD
                0x08 ->
                    HookType.CALL_NORMAL_AFTER_METHOD
                0x09 ->
                    HookType.CALL_NORMAL_REPLACE_METHOD
                0x0a ->
                    HookType.CALL_STATIC_BEFORE_METHOD
                0x0b ->
                    HookType.CALL_STATIC_AFTER_METHOD
                0x0c ->
                    HookType.CALL_STATIC_REPLACE_METHOD
                else ->
                    HookType.DEFINE_NORMAL_BEFORE_METHOD
            }
        }
    }

    override fun toString(): String {
        return "TargetMethod(name='$name', className='$className', methodName='$methodName', methodDesc='$methodDesc', targetClassName='$targetClassName', targetMethodName='$targetMethodName', targetMethodDesc='$targetMethodDesc', hookType=$hookType)"
    }
}