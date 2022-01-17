package com.qihoo360.replugin.config

import groovy.lang.Closure
import org.gradle.api.NamedDomainObjectContainer

/**
 * author:gaoguanling
 * date:2021/7/4
 * time:21:28
 * email:gaoguanling@360.cn
 * link:
 */
open class PluginExtension : BaseExtension() {
    /** 编译的 App Module 的名称 */
    var appModule = ":app"

    /** 用户声明要忽略的注入器 */
    var ignoredInjectors = mutableListOf<String>()

    /** 执行 LoaderActivity 替换时，用户声明不需要替换的 Activity */
    var ignoredActivities = mutableListOf<String>()

    /** 自定义的注入器 */
    var customInjectors = mutableListOf<String>()

    /** 插件名字,默认null */
    var pluginName: String? = null

    /** 手机存储目录,默认"/sdcard/" */
    var phoneStorageDir: String? = "/sdcard/"

    /** 宿主包名,默认null */
    var hostApplicationId: String? = null

    /** 宿主launcherActivity,默认null */
    var hostAppLauncherActivity: String? = null

    /** 旧代码lib包里的代码和module的packageName无关，是混乱的，为了兼容，唉。。 **/
    override var libPackages:Set<String>? = setOf("Lcom/qihoo360/replugin")
    override var targetClasses = setOf(
        "com/qihoo360/replugin/loader/a/PluginActivity",
        "com/qihoo360/replugin/loader/b/PluginLocalBroadcastManager",
        "com/qihoo360/replugin/loader/p/PluginProviderClient"
    )
    var skipClasses: NamedDomainObjectContainer<TargetClass>? = null
    var excludedClasses: NamedDomainObjectContainer<TargetClass>? = null
    fun excludedClasses(closure: Closure<TargetClass>) {
        this.excludedClasses?.configure(closure)
    }
    fun skipClasses(closure: Closure<TargetClass>) {
        this.skipClasses?.configure(closure)
    }
}