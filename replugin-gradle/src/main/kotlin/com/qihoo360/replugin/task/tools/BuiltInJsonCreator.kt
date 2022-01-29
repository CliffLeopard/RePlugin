package com.qihoo360.replugin.task.tools

import com.android.build.gradle.internal.api.ApplicationVariantImpl
import com.android.builder.dexing.isJarFile
import com.qihoo360.replugin.Constants
import com.qihoo360.replugin.Log
import com.qihoo360.replugin.host.HostExtension
import groovy.json.JsonOutput
import java.io.File

/**
 * author:gaoguanling
 * date:2021/7/5
 * time:10:30
 * email:gaoguanling@360.cn
 * link:
 */
class BuiltInJsonCreator(
    variant: ApplicationVariantImpl,
    private val extension: HostExtension
) : IFileCreator {

    private val fileDir: File
    private val fileName: String
    private val pluginInfoSet = mutableSetOf<PluginInfo>()

    init {
        val out = variant.mergeAssetsProvider.get().outputDir.get()
        fileDir = out.asFile
        fileName = extension.builtInJsonFileName
    }

    override fun getFileDir(): File {
        return fileDir
    }

    override fun getFileName(): String {
        return fileName
    }

    override fun createFileContent(): String {
        //查找插件文件并抽取信息,如果没有就直接返回null
        val pluginDirFile = File(fileDir.absolutePath + File.separator + extension.pluginDir)
        if (!pluginDirFile.exists()) {
            println("${Constants.HOST_TAG} The ${pluginDirFile.absolutePath} does not exist ")
            println("${Constants.HOST_TAG} pluginsInfo=null")
            return ""
        }

        var parser: PluginInfoParser? = null
        File(fileDir.absolutePath + File.separator + extension.pluginDir)
            .walk()
            .filter(isJarFile)
            .iterator()
            .forEach { jarFile ->
                try {
                    parser = PluginInfoParser(jarFile, extension)
                } catch (ext: Exception) {
                    if (extension.enablePluginFileIllegalStopBuild) {
                        Log.e(Constants.HOST_TAG, "the plugin ${jarFile.absolutePath} is illegal!")
                        throw  ext
                    }
                }
                parser?.getPluginInfo()?.let { pluginInfoSet.add(it) }
            }

        if (pluginInfoSet.isEmpty()) {
            println("${Constants.HOST_TAG} pluginsSize=0")
            println("${Constants.HOST_TAG} pluginsInfo=null")
            return ""
        }

        val pluginInfoJson: String = JsonOutput.toJson(pluginInfoSet)
        println("${Constants.HOST_TAG} pluginsSize=${pluginInfoSet.size}")
        println("${Constants.HOST_TAG} pluginsInfo=${JsonOutput.prettyPrint(pluginInfoJson)}")
        return pluginInfoJson
    }
}

class PluginInfo {
    /** 插件文件路径 */
    var path: String? = ""

    /** 插件包名 */
    var pkg = ""

    /** 插件名 */
    var name = ""

    /** 插件最低兼容版本 */
    var low: Long = -1

    /** 插件最高兼容版本 */
    var height: Long = -1

    /** 插件版本号 */
    var ver: Long = -1

    /** 框架版本号 */
    var from: Long = -1

}