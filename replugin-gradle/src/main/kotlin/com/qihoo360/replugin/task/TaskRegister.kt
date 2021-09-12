package com.qihoo360.replugin.task

import com.android.build.gradle.AppExtension
import com.qihoo360.replugin.config.BaseExtension
import org.gradle.api.Project

/**
 * author:gaoguanling
 * date:2021/9/6
 * time:17:26
 * email:gaoguanling@360.cn
 * link:
 */

interface TaskRegister<T : BaseExtension> {
    fun registerProjectTask(project: Project, android: AppExtension, extension: T)
}