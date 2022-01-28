package com.qihoo360.replugin.config

import groovy.lang.Closure
import org.gradle.api.NamedDomainObjectContainer

/**
 * author:gaoguanling
 * date:2022/1/28
 * time:09:26
 * email:gaoguanling@360.cn
 * link:
 */
open class HookExtension : BaseExtension() {
    open var skipClasses: NamedDomainObjectContainer<TargetClass>? = null
    open var excludedClasses: NamedDomainObjectContainer<TargetClass>? = null
    open var hookMethods: NamedDomainObjectContainer<HookMethod>? = null
    open var hookLambdas: NamedDomainObjectContainer<HookLambda>? = null
    open val defaultHookMethod: Set<HookMethod> = setOf()
    open val defaultHookLambda: Set<HookLambda> = setOf()

    fun excludedClasses(closure: Closure<TargetClass>) {
        this.excludedClasses?.configure(closure)
    }

    fun skipClasses(closure: Closure<TargetClass>) {
        this.skipClasses?.configure(closure)
    }

    fun hookMethods(closure: Closure<HookMethod>) {
        this.hookMethods?.configure(closure)
    }

    fun hookLambdas(closure: Closure<HookLambda>) {
        this.hookLambdas?.configure(closure)
    }
}