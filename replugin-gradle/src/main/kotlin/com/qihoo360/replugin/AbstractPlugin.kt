package com.qihoo360.replugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.LibraryExtension
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
    protected open var extension: T? = null
    override fun apply(project: Project) {
        beforeApply(project)
        if (isApplicationPlugin() == project.plugins.hasPlugin(AppPlugin::class.java)) {
            if (isApplicationPlugin())
                registerAppPlugin(project)
            else
                registerLibraryPlugin(project)
        }
        afterApply(project)
    }

    private fun registerAppPlugin(project: Project) {
        createExtension(project)
        val android: AppExtension = project.extensions.getByType(AppExtension::class.java)
        initExtension(project, android)
        configExtension(project, android)
        getTransform(project, android)?.let { transform ->
            android.registerTransform(transform)
        }
        extension?.let {
            registerProjectTask(project, android, it)
        }
    }

    private fun registerLibraryPlugin(project: Project) {
        val library: LibraryExtension = project.extensions.getByType(LibraryExtension::class.java)
    }

    open fun configExtension(project: Project, android: AppExtension) {
        extension?.applicationId = android.defaultConfig.applicationId
    }

    open fun isApplicationPlugin() = true
    open fun beforeApply(project: Project) {}
    open fun afterApply(project: Project) {}
    abstract fun createExtension(project: Project)
    abstract fun initExtension(project: Project, android: AppExtension)
    override fun getTransform(project: Project, android: AppExtension): AbstractTransform? = null
}