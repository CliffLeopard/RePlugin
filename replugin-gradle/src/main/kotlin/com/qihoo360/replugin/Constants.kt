package com.qihoo360.replugin

/**
 * author:gaoguanling
 * date:2021/7/4
 * time:21:36
 * email:gaoguanling@360.cn
 * link:
 */
object Constants {
    //版本号:不要手动修改,当发布时，会根据rp-config.gradle中的RP_VERSION自动替换
    private const val VER = "2.3.9-SNAPSHOT"

    /** 打印信息时候的前缀 */
    const val HOST_TAG = "< replugin-host-$VER >"

    /** 外部用户配置信息 */
    const val HOST_CONFIG = "repluginHostConfig"

    /** 用户Task组 */
    const val HOST_TASKS_GROUP = "replugin-host"

    /** Task前缀 */
    private const val TASKS_PREFIX = "rp"

    /** 用户Task:安装插件 */
    const val TASK_SHOW_PLUGIN = TASKS_PREFIX + "ShowPlugins"

    /** 用户Task:Generate任务 */
    const val TASK_GENERATE = TASKS_PREFIX + "Generate"


    const val PLUGIN_TAG = "< replugin-plugin-$VER >"
    const val PLUGIN_CONFIG = "repluginPluginConfig"
    const val PLUGIN_TASKS_GROUP = "replugin-plugin"

    /** 用户Task:强制停止宿主app */
    const val TASK_FORCE_STOP_HOST_APP = TASKS_PREFIX + "StopHost"

    /** 用户Task:启动宿主app */
    const val TASK_START_HOST_APP = TASKS_PREFIX + "StartHost"

    /** 用户Task:重启宿主app */
    const val TASK_RESTART_HOST_APP = TASKS_PREFIX + "RestartHost"

    /** 用户Task:安装插件 */
    const val TASK_INSTALL_PLUGIN = TASKS_PREFIX + "InstallPlugin"

    /** 用户Task:安装插件 */
    const val TASK_UNINSTALL_PLUGIN = TASKS_PREFIX + "UninstallPlugin"

    /** 用户Task:运行插件 */
    const val TASK_RUN_PLUGIN = TASKS_PREFIX + "RunPlugin"

    /** 用户Task:安装并运行插件 */
    const val TASK_INSTALL_AND_RUN_PLUGIN = TASKS_PREFIX + "InstallAndRunPlugin"
    const val SKIP_ANNOTATION=
        "Lcom/qihoo360/replugin/base.annotation/SkipChange;"
    const val MODIFIED_ANNOTATION=
        "Lcom/qihoo360/replugin/base.annotation/PluginModified;"
}