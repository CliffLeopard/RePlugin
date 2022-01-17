package com.qihoo360.replugin.config

/**
 * author:gaoguanling
 * date:2021/11/15
 * time:19:32
 * email:gaoguanling@360.cn
 * link:
 */
class HookMethodContainer(hookMethods: Set<HookMethod>) {

    private val methodConfig: Map<String, Map<HookMethod.HookType, List<HookMethod>>> = hookMethods
        .groupBy { getMethodKey(it) }
        .map { (methodKey, list) ->
            Pair(methodKey, list.groupBy { HookMethod.getHookTypeByValue(it.hookType) })
        }.associate { (first, second) ->
            Pair(first, second)
        }

    fun getMethodConfig(className: String, methodName: String, methodDesc: String): Map<HookMethod.HookType, List<HookMethod>>? {
        return methodConfig[getMethodKey(className, methodName, methodDesc)]
    }

    fun isEmpty(): Boolean {
        return methodConfig.isEmpty()
    }

    companion object {
        fun getInstance(extension: BaseExtension): HookMethodContainer {
            return if (extension.hookMethods == null)
                HookMethodContainer(extension.defaultHookMethod.toSet())
            else
                HookMethodContainer(extension.defaultHookMethod.toSet().plus(extension.hookMethods!!.toSet()))
        }

        fun getMethodKey(hookMethod: HookMethod): String {
            return getMethodKey(hookMethod.className, hookMethod.methodName, hookMethod.methodDesc)
        }

        fun getMethodKey(className: String, methodName: String, methodDesc: String): String {
            return "$className:$methodName$methodDesc"
        }
    }
}