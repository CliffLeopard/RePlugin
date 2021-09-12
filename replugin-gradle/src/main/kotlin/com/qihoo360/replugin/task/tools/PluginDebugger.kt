package com.qihoo360.replugin.task.tools

import com.android.build.gradle.AppExtension
import com.android.build.gradle.internal.api.ApplicationVariantImpl
import com.qihoo360.replugin.Constants
import com.qihoo360.replugin.Log
import com.qihoo360.replugin.config.PluginExtension
import org.apache.http.util.TextUtils
import java.io.File

/**
 * author:gaoguanling
 * date:2021/8/2
 * time:16:56
 * email:gaoguanling@360.cn
 * link:
 */
class PluginDebugger(
    private val extension: AppExtension,
    private val config: PluginExtension
) {
    private var adbFile: File? = extension.adbExecutable

    /**
     * 安装插件
     */
    fun installPlugin(variant: ApplicationVariantImpl): Boolean {
        if (illegal())
            return false

        val apkFile: File? = extension.buildOutputs.getByName(variant.name).outputFile
        if (apkIllegal(apkFile))
            return false

        // push to sdCard
        val cmdPush =
            "${adbFile!!.absolutePath} push ${apkFile!!.absolutePath} ${config.phoneStorageDir}"
        if (0 != syncExecute(cmdPush))
            return false

        var pluginApkPath = config.phoneStorageDir
        if (TextUtils.isEmpty(pluginApkPath)) {
            return false
        }
        if (!pluginApkPath!!.endsWith("/"))
            pluginApkPath += "/"
        pluginApkPath += apkFile.name

        // broadcast install
        val cmdInstall =
            "${adbFile!!.absolutePath} shell am broadcast -a ${config.hostApplicationId}.replugin.install -e path $pluginApkPath -e immediately true "
        return syncExecute(cmdInstall) == 0
    }

    /**
     * 卸载插件
     */
    fun uninstallPlugin(): Boolean {
        if (illegal())
            return false
        val cmdUninstall =
            "${adbFile!!.absolutePath} shell am broadcast -a ${config.hostApplicationId}.replugin.uninstall -e plugin ${config.pluginName}"
        return syncExecute(cmdUninstall) == 0
    }

    fun stopHost(): Boolean {
        if (illegal())
            return false
        val cmdStopHost =
            "${adbFile!!.absolutePath} shell am force-stop ${config.hostApplicationId}"
        return syncExecute(cmdStopHost) == 0
    }

    /**
     * 启动宿主
     */
    fun startHost(): Boolean {
        if (illegal())
            return false
        val cmdStartHost =
            "${adbFile!!.absolutePath} shell am start -n \"${config.hostApplicationId}/${config.hostAppLauncherActivity}\" -a android.intent.action.MAIN -c android.intent.category.LAUNCHER"
        return syncExecute(cmdStartHost) == 0
    }

    /**
     * 启动插件
     */
    fun runPlugin(): Boolean {
        if (illegal())
            return false
        val cmdRunPlugin =
            "${adbFile!!.absolutePath} shell am broadcast -a ${config.hostApplicationId}.replugin.start_activity -e plugin ${config.pluginName}"
        return syncExecute(cmdRunPlugin) == 0
    }

    /**
     * 执行adb shell 命令
     */
    private fun syncExecute(cmd: String): Int {
        Log.i(Constants.PLUGIN_TAG, "runCmd:$cmd")
        return try {
            val process: Process = Runtime.getRuntime().exec(cmd)
            process.inputStream.bufferedReader().readLines().forEach { line ->
                Log.i(Constants.PLUGIN_TAG, line)
            }
            process.waitFor()
            process.exitValue()
        } catch (e: Exception) {
            Log.e(Constants.PLUGIN_TAG, "runError: $cmd   ${e.message}")
            -1
        }
    }

    /**
     * 参数验证
     */
    private fun illegal(): Boolean {

        if (adbFile == null || !adbFile!!.exists()) {
            Log.e(Constants.HOST_TAG, "Could not find the adb file !!!")
            return true
        }

        if (config.hostApplicationId == null) {
            Log.e(Constants.HOST_TAG, "the config hostApplicationId can not be null!!!")
            return true
        }

        if (config.hostAppLauncherActivity == null) {
            Log.e(Constants.HOST_TAG, "the config hostAppLauncherActivity can not be null!!!")
            return true
        }
        return false
    }

    private fun apkIllegal(apkFile: File?): Boolean {
        if (apkFile == null || !apkFile.exists()) {
            Log.e(Constants.HOST_TAG, "Could not find the apk file !!!")
            return true
        }
        return false
    }
}