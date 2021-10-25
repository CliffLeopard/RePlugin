package com.qihoo360.replugin.transform.pool

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format
import com.android.build.api.transform.Status
import com.android.build.api.transform.TransformOutputProvider
import com.android.utils.FileUtils
import com.qihoo360.replugin.Log
import com.qihoo360.replugin.transform.bean.TransformClassInfo
import java.io.File

/**
 * author:gaoguanling
 * date:2021/8/31
 * time:13:52
 * email:gaoguanling@360.cn
 * link:
 * 生产并发任务，
 * 每个文件并发任务能会处理1个或者多个文件。
 * 每个Jar并发任务，处理1个或多个jar包。 是以Jar包为单位,而非Entry为单位。 这样是为了减少任务的切换频率，同时方式Entry游标混乱
 */
object ClassFileProcessor {
    private const val tag = "FileProcessor"
    fun processClass(
        isIncremental: Boolean,
        directoryInput: DirectoryInput,
        outputProvider: TransformOutputProvider
    ): Sequence<TransformClassInfo> {

        val outDir = outputProvider.getContentLocation(
            directoryInput.name,
            directoryInput.contentTypes,
            directoryInput.scopes,
            Format.DIRECTORY
        )
        val realIncremental = isIncremental && outDir.exists()
        Log.i(tag,"isIncremental:$realIncremental")
        outDir.mkdirs()
        val outDirPath = outDir.absolutePath
        val inputDirPath = directoryInput.file.absolutePath
        val fileSequence: Sequence<File> = if (realIncremental) {
            directoryInput.changedFiles
                .filter { (file, status) ->
                    val relativeClassPath = file.absolutePath.substring(inputDirPath.length)
                    val outFile = File(outDirPath, relativeClassPath)

                    when (status!!) {
                        Status.NOTCHANGED -> {
                            val exist = outFile.exists()
                            !exist
                        }

                        Status.REMOVED -> {
                            false
                        }
                        Status.ADDED, Status.CHANGED -> {
                            true
                        }
                    }
                }
                .map { (file, _) -> file }
                .asSequence()
        } else {
            FileUtils.getAllFiles(directoryInput.file)
                .asSequence()
                .filter { file ->
                    (file != null) && file.exists()
                }
        }


        return fileSequence
            .mapNotNull { classFile ->
                val relativeClassPath = classFile.absolutePath.substring(inputDirPath.length)
                val outFile = File(outDirPath, relativeClassPath)
                if (outFile.exists()) {
                    outFile.delete()
                }
                outFile.parentFile.mkdirs()
                if (!classFile.name.endsWith(".class")) {
                    copyFile(classFile, outFile)
                    null
                } else {
                    val name = classFile.absolutePath
                        .removePrefix(inputDirPath)
                        .removePrefix(File.separator)
                        .removeSuffix(".class")
                    val packageName = classFile.absolutePath
                        .removePrefix(inputDirPath)
                        .removePrefix(File.separator)
                        .removeSuffix(classFile.name)
                        .removeSuffix(File.separator)
                    val isInnerClass = classFile.name.contains('$')
                    val className = classFile.name
                        .removeSuffix(".class")

                    TransformClassInfo(
                        name,
                        packageName,
                        className,
                        classFile.absolutePath,
                        outFile.absolutePath,
                        directoryInput,
                        false,
                        isInnerClass
                    )
                }
            }
    }

    private fun copyFile(classFile: File, outFile: File) {
        FileUtils.copyFile(classFile, outFile)
    }
}