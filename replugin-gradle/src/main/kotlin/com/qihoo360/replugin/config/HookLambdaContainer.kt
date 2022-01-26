package com.qihoo360.replugin.config

/**
 * author:gaoguanling
 * date:2022/1/25
 * time:16:51
 * email:gaoguanling@360.cn
 * link:
 */
class HookLambdaContainer(private val hookLambdas: Set<HookLambda>) {
    companion object {
        fun getInstance(extension: BaseExtension): HookLambdaContainer {
            return if (extension.hookLambdas == null)
                HookLambdaContainer(extension.defaultHookLambda.toSet())
            else
                HookLambdaContainer(extension.defaultHookLambda.toSet().plus(extension.hookLambdas!!.toSet()))
        }
    }

    fun isEmpty(): Boolean {
        return hookLambdas.isEmpty()
    }

    fun get(): Set<HookLambda> {
        return hookLambdas
    }

    fun findByClassName(className: String): HookLambdaContainer {
        return HookLambdaContainer(hookLambdas.filter { lambda -> lambda.className == className }.toSet())
    }

    fun findByMethodNameAndDesc(methodName: String, methodDesc: String): HookLambdaContainer {
        return HookLambdaContainer(hookLambdas.filter { lambda -> lambda.methodName == methodName && lambda.methodDesc == methodDesc }.toSet())
    }

    fun findByLambdaMethodNameAndDesc(lambdaMethodName: String, lambdaMethodDesc: String): HookLambdaContainer {
        return HookLambdaContainer(hookLambdas.filter { lambda -> lambda.lambdaMethodName == lambdaMethodName && lambda.lambdaMethodDesc == lambdaMethodDesc }
            .toSet())
    }

    fun findByType(type: HookLambda.HookType): HookLambdaContainer {
        return HookLambdaContainer(hookLambdas.filter { lambda -> lambda.hookType == type.value }.toSet())
    }
}