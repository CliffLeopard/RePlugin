package com.qihoo360.replugin.host

import com.android.build.gradle.AppExtension
import com.qihoo360.replugin.AbstractPlugin
import com.qihoo360.replugin.Constants
import com.qihoo360.replugin.config.HostExtension
import org.gradle.api.Project

/**
 * author:CliffLeopard
 * date:5/1/21
 * time:10:52 PM
 * email:precipiceleopard@gmail.com
 * link:
 */
@ExperimentalStdlibApi
open class HostPlugin : AbstractPlugin<HostExtension>() {
    override fun createExtension(project: Project) {
        project.extensions.create(Constants.HOST_CONFIG, HostExtension::class.java)
    }

    override fun initExtension(project: Project, android: AppExtension) {
        extension = project.extensions.getByName(Constants.HOST_CONFIG) as HostExtension
        if (extension == null)
            throw Exception("请在build.gradle 文件中配置 repluginHostConfig!!")
        else
            extension!!.applicationId = android.defaultConfig.applicationId
    }

    override fun registerProjectTask(
        project: Project,
        android: AppExtension,
        extension: HostExtension
    ) {
        HostTaskRegister.registerProjectTask(project, android, extension)
    }
}
