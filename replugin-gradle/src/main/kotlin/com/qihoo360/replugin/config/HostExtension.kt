package com.qihoo360.replugin.config

/**
 * author:gaoguanling
 * date:2021/7/4
 * time:21:28
 * email:gaoguanling@360.cn
 * link:
 */
open class HostExtension : BaseExtension() {
    /** 自定义进程的数量(除 UI 和 Persistent 进程) */
    var countProcess = 3

    /** 是否使用常驻进程？ */
    var persistentEnable = true

    /** 常驻进程名称（也就是上面说的 Persistent 进程，开发者可自定义）*/
    var persistentName = ":GuardService"

    /** 背景不透明的坑的数量 */
    var countNotTranslucentStandard = 6
    var countNotTranslucentSingleTop = 2
    var countNotTranslucentSingleTask = 3
    var countNotTranslucentSingleInstance = 2

    /** 背景透明的坑的数量 */
    var countTranslucentStandard = 2
    var countTranslucentSingleTop = 2
    var countTranslucentSingleTask = 2
    var countTranslucentSingleInstance = 3

    /** 宿主中声明的 TaskAffinity 的组数 */
    var countTask = 2

    /**
     * 是否使用 AppCompat 库
     * com.android.support:appcompat-v7:28.0.0
     * androidx.appcompat
     */
    var useAppCompat = true

    /** HOST 向下兼容的插件版本 */
    var compatibleVersion = 10

    /** HOST 插件版本 */
    var currentVersion = 12

    /** plugins-builtin.json 文件名自定义,默认是 "plugins-builtin.json" */
    var builtInJsonFileName = "plugins-builtin.json"

    /** 是否自动管理 plugins-builtin.json 文件,默认自动管理 */
    var autoManageBuiltInJsonFile = true

    /** assert目录下放置插件文件的目录自定义,默认是 assert 的 "plugins" */
    var pluginDir = "plugins"

    /** 插件文件的后缀自定义,默认是".jar" 暂时支持 jar 格式*/
    var pluginFilePostfix = ".jar"

    /** 当发现插件目录下面有不合法的插件 jar (有可能是特殊定制 jar)时是否停止构建,默认是 true */
    var enablePluginFileIllegalStopBuild = true
}