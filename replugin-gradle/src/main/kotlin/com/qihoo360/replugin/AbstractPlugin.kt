package com.qihoo360.replugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.internal.plugins.AppPlugin
import com.qihoo360.replugin.config.BaseExtension
import com.qihoo360.replugin.task.TaskRegister
import com.qihoo360.replugin.transform.AbstractTransform
import com.qihoo360.replugin.transform.TransformSupporter
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * author:CliffLeopard
 * date:5/3/21
 * time:6:01 PM
 * email:precipiceleopard@gmail.com
 * link:
 */
abstract class AbstractPlugin<T : BaseExtension> : Plugin<Project>, TaskRegister<T>, TransformSupporter {
    protected var extension: T? = null
    private lateinit var project: Project
    override fun apply(project: Project) {
        beforeApply(project)
        this.project = project
        if (project.plugins.hasPlugin(AppPlugin::class.java)) {
            createExtension(project)
            val android: AppExtension = project.extensions.getByType(AppExtension::class.java)
            initExtension(project,android)
            getTransform(project, android)?.let { transform ->
                android.registerTransform(transform)
            }

            extension?.let {
                registerProjectTask(project, android, it)
            }
        }
        afterApply(project)
    }

    open fun beforeApply(project: Project) {

    }

    open fun afterApply(project: Project) {

    }

    abstract fun createExtension(project: Project)
    abstract fun initExtension(project: Project,android: AppExtension)
    override fun getTransform(project: Project, android: AppExtension): AbstractTransform? = null
}