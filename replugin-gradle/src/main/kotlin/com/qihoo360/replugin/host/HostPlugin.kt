package com.qihoo360.replugin.host

import com.android.build.gradle.AppExtension
import com.qihoo360.replugin.Constants
import com.qihoo360.replugin.hook.HookExtension
import com.qihoo360.replugin.hook.HookAbstractPlugin
import org.gradle.api.Project

/**
 * author:CliffLeopard
 * date:5/1/21
 * time:10:52 PM
 * email:precipiceleopard@gmail.com
 * link:
 */
@ExperimentalStdlibApi
open class HostPlugin : HookAbstractPlugin<HostExtension>() {
    override fun createExtension(project: Project) {
        project.extensions.create(Constants.HOST_CONFIG, HostExtension::class.java)
    }

    override fun initExtension(project: Project, android: AppExtension) {
        extension = project.extensions.getByName(Constants.HOST_CONFIG) as HostExtension
        if (extension == null)
            throw Exception("请在build.gradle 文件中配置 repluginHostConfig!!")
    }

    override fun registerProjectTask(project: Project, android: AppExtension, extension: HookExtension) {
        HostTaskRegister.registerProjectTask(project, android, extension as HostExtension)
    }
}
