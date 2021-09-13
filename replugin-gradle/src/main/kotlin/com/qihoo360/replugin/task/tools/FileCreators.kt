package com.qihoo360.replugin.task.tools

import com.android.build.gradle.internal.api.ApplicationVariantImpl
import com.qihoo360.replugin.config.HostExtension
import org.apache.http.util.TextUtils
import org.gradle.api.Project
import java.io.File

/**
 * author:gaoguanling
 * date:2021/7/4
 * time:21:50
 * email:gaoguanling@360.cn
 * link:
 */
object FileCreators {
    private fun create(creator: IFileCreator?) {
        if (creator == null || TextUtils.isEmpty(creator.getFileContent()))
            return

        val dir = creator.getFileDir()
        if (!dir.exists()) dir.mkdirs()

        val targetFile = File(dir, creator.getFileName())
        val fileContent = creator.getFileContent()
        targetFile.writeText(fileContent, Charsets.UTF_8)
    }

    fun createHostConfig(project: Project, variant: ApplicationVariantImpl, extension: HostExtension) {
        val creator = HostConfigCreator(project, variant, extension)
        create(creator)
    }

    fun createBuiltInJson(variant: ApplicationVariantImpl, extension: HostExtension) {
        if (extension.autoManageBuiltInJsonFile) {
            val creator = BuiltInJsonCreator(variant, extension)
            create(creator)
        }
    }
}

interface IFileCreator {
    /**
     * 要生成的文件所在目录
     */
    fun getFileDir(): File

    /**
     * 要生成的文件的名称
     */
    fun getFileName(): String

    /**
     * 要生成的文件内容
     */
    fun getFileContent(): String
}