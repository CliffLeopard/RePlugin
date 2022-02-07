package com.qihoo360.replugin.hook

import com.android.build.gradle.AppExtension
import com.qihoo360.replugin.AbstractPlugin
import org.gradle.api.Project

/**
 * author:gaoguanling
 * date:2022/1/28
 * time:10:07
 * email:gaoguanling@360.cn
 * link:
 */
abstract class HookAbstractPlugin<T : HookExtension> : AbstractPlugin<HookExtension>() {
    override fun configExtension(project: Project, android: AppExtension) {
        super.configExtension(project, android)
        extension?.excludedClasses = project.container(TargetClass::class.java)
        extension?.skipClasses = project.container(TargetClass::class.java)
        extension?.hookMethods = project.container(HookMethod::class.java)
        extension?.hookLambdas = project.container(HookLambda::class.java)
    }
}