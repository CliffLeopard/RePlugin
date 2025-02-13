package com.qihoo360.replugin.plugin

import com.android.build.gradle.AppExtension
import com.qihoo360.replugin.Constants
import com.qihoo360.replugin.hook.HookExtension
import com.qihoo360.replugin.hook.HookAbstractPlugin
import com.qihoo360.replugin.transform.AbstractTransform
import org.gradle.api.Project

/**
 * author:CliffLeopard
 * date:5/1/21
 * time:4:38 PM
 * email:precipiceleopard@gmail.com
 * link:
 */
open class PluginPlugin : HookAbstractPlugin<PluginExtension>() {
    override fun createExtension(project: Project) {
        project.extensions.create(Constants.PLUGIN_CONFIG, PluginExtension::class.java)
    }

    override fun initExtension(project: Project, android: AppExtension) {
        extension = project.extensions.getByName(Constants.PLUGIN_CONFIG) as PluginExtension
        if (extension == null)
            throw Exception("请在build.gradle 文件中配置 repluginPluginConfig!!")
    }

    override fun registerProjectTask(
        project: Project,
        android: AppExtension,
        extension: HookExtension
    ) {
        PluginTaskRegister.registerProjectTask(project, android, extension as PluginExtension)
    }

    override fun getTransform(project: Project, android: AppExtension): AbstractTransform {
        return PluginTransform(android, extension as PluginExtension)
    }
}