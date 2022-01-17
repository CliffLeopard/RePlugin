package com.qihoo360.replugin.config

import groovy.lang.Closure
import org.gradle.api.NamedDomainObjectContainer
import java.io.File

/**
 * author:gaoguanling
 * date:2021/9/3
 * time:17:23
 * email:gaoguanling@360.cn
 * link:
 */
open class BaseExtension {
    var applicationId: String? = null
    open var libPackages: Set<String>? = setOf()
    open var targetClasses = setOf<String>()
    open var hookMethods: NamedDomainObjectContainer<HookMethod>? = null

    fun isTargetClass(desc: String): Boolean {
        return targetClasses.contains(
            if (desc.contains(".")) desc.replace(
                '.',
                File.separatorChar
            ) else desc
        )
    }

    fun hookMethods(closure: Closure<HookMethod>) {
        this.hookMethods?.configure(closure)
    }

    val defaultHookMethod: Set<HookMethod> = setOf()
}