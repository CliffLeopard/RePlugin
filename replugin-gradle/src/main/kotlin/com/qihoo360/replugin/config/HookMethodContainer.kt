package com.qihoo360.replugin.config

/**
 * author:gaoguanling
 * date:2021/11/15
 * time:19:32
 * email:gaoguanling@360.cn
 * link:
 */
class HookMethodContainer(targetMethods: Set<TargetMethod>) {

    private val methodConfig: Map<String, Map<TargetMethod.HookType, List<TargetMethod>>> = targetMethods
        .groupBy { getMethodKey(it) }
        .map { (methodKey, list) ->
            Pair(methodKey, list.groupBy { TargetMethod.getHookTypeByValue(it.hookType) })
        }.associate { (first, second) ->
            Pair(first, second)
        }

    fun getMethodConfig(className: String, methodName: String, methodDesc: String): Map<TargetMethod.HookType, List<TargetMethod>>? {
        return methodConfig[getMethodKey(className, methodName, methodDesc)]
    }

    fun isEmpty(): Boolean {
        return methodConfig.isEmpty()
    }

    companion object {
        fun getInstance(extension: BaseExtension): HookMethodContainer {
            return HookMethodContainer(extension.defaultHookMethod.toSet().plus(extension.hookMethods.toSet()))
        }

        fun getMethodKey(targetMethod: TargetMethod): String {
            return getMethodKey(targetMethod.className, targetMethod.methodName, targetMethod.methodDesc)
        }

        fun getMethodKey(className: String, methodName: String, methodDesc: String): String {
            return "$className:$methodName$methodDesc"
        }
    }
}