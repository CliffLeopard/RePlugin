package com.qihoo360.replugin.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.internal.api.ApplicationVariantImpl
import com.qihoo360.replugin.Constants
import com.qihoo360.replugin.config.PluginExtension
import com.qihoo360.replugin.task.TaskRegister
import com.qihoo360.replugin.task.tools.PluginDebugger
import org.gradle.api.Project
import java.util.*

/**
 * author:gaoguanling
 * date:2021/9/6
 * time:17:35
 * email:gaoguanling@360.cn
 * link:
 */
object PluginTaskRegister : TaskRegister<PluginExtension> {
    override fun registerProjectTask(
        project: Project,
        android: AppExtension,
        extension: PluginExtension
    ) {
        val debugger = PluginDebugger(android, extension)
        // 卸载插件
        val uninstallPluginTask = project.task(Constants.TASK_UNINSTALL_PLUGIN)
        uninstallPluginTask.doLast {
            debugger.uninstallPlugin()
        }
        uninstallPluginTask.group = Constants.PLUGIN_TASKS_GROUP

        // 启动宿主
        val startHostTask = project.task(Constants.TASK_START_HOST_APP)
        startHostTask.doLast {
            debugger.startHost()
        }
        startHostTask.group = Constants.PLUGIN_TASKS_GROUP

        // 关闭宿主
        val stopHostTask = project.task(Constants.TASK_FORCE_STOP_HOST_APP)
        stopHostTask.doLast {
            debugger.stopHost()
        }
        stopHostTask.group = Constants.PLUGIN_TASKS_GROUP

        // 重启宿主
        val restartHostTask = project.task(Constants.TASK_RESTART_HOST_APP)
        restartHostTask.doLast {
            debugger.stopHost()
            debugger.startHost()
        }
        restartHostTask.group = Constants.PLUGIN_TASKS_GROUP

        android.applicationVariants.all { variant ->
            when (variant) {
                is ApplicationVariantImpl -> {
                    registerVariantTask(project, variant, debugger)
                }
            }
        }
    }

    private fun registerVariantTask(
        project: Project,
        variant: ApplicationVariantImpl,
        debugger: PluginDebugger
    ) {
        val assembleTask = variant.assembleProvider.get()
        //-- 安装插件
        val installPluginTaskName =
            getTaskName(Constants.TASK_INSTALL_PLUGIN, variant.name, "")
        val installPluginTask = project.task(installPluginTaskName)
        installPluginTask.doLast {
            debugger.startHost()
            debugger.uninstallPlugin()
            debugger.stopHost()
            debugger.startHost()
            debugger.installPlugin(variant)
        }
        installPluginTask.group = Constants.PLUGIN_TASKS_GROUP
        installPluginTask.dependsOn(assembleTask)


        //-- 运行插件
        val runPluginTaskName =
            getTaskName(Constants.TASK_RUN_PLUGIN, variant.name, "")
        val runPluginTask = project.task(runPluginTaskName)
        runPluginTask.doLast {
            debugger.runPlugin()
        }
        runPluginTask.group = Constants.PLUGIN_TASKS_GROUP


        //-- 安装并运行插件
        val installAndRunPluginTaskName =
            getTaskName(Constants.TASK_INSTALL_AND_RUN_PLUGIN, variant.name, "")
        val installAndRunPluginTask = project.task(installAndRunPluginTaskName)
        installAndRunPluginTask.doLast {
            debugger.runPlugin()
        }
        installAndRunPluginTask.group = Constants.TASK_INSTALL_AND_RUN_PLUGIN
        installAndRunPluginTask.dependsOn(installPluginTask)
    }


    private fun getTaskName(prefix: String, variantName: String, tail: String): String {
        return prefix + toUpperCase(variantName) + tail
    }

    private fun toUpperCase(name: String): String {
        return name.substring(0, 1).toUpperCase(Locale.ROOT) + name.substring(1)
    }
}