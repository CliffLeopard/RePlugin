package com.qihoo360.replugin.plugin

import com.android.build.gradle.AppExtension
import com.qihoo360.replugin.AbstractPlugin
import com.qihoo360.replugin.Constants
import com.qihoo360.replugin.config.HookLambda
import com.qihoo360.replugin.config.PluginExtension
import com.qihoo360.replugin.config.TargetClass
import com.qihoo360.replugin.config.HookMethod
import com.qihoo360.replugin.transform.AbstractTransform
import org.gradle.api.Project

/**
 * author:CliffLeopard
 * date:5/1/21
 * time:4:38 PM
 * email:precipiceleopard@gmail.com
 * link:
 */
open class PluginPlugin : AbstractPlugin<PluginExtension>() {
    override fun createExtension(project: Project) {
        project.extensions.create(Constants.PLUGIN_CONFIG, PluginExtension::class.java)
    }

    override fun initExtension(project: Project, android: AppExtension) {
        extension = project.extensions.getByName(Constants.PLUGIN_CONFIG) as PluginExtension
        if (extension == null)
            throw Exception("请在build.gradle 文件中配置 repluginPluginConfig!!")
        else {
            extension!!.applicationId = android.defaultConfig.applicationId
            extension!!.excludedClasses = project.container(TargetClass::class.java)
            extension!!.skipClasses = project.container(TargetClass::class.java)
            extension!!.hookMethods = project.container(HookMethod::class.java)
            extension!!.hookLambdas = project.container(HookLambda::class.java)
        }
    }

    override fun registerProjectTask(
        project: Project,
        android: AppExtension,
        extension: PluginExtension
    ) {
        PluginTaskRegister.registerProjectTask(project, android, extension)
    }

    override fun getTransform(project: Project, android: AppExtension): AbstractTransform {
        return PluginTransform(android, extension!!)
    }
}