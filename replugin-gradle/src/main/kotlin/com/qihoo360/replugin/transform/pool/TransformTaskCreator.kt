package com.qihoo360.replugin.transform.pool

import com.android.build.api.transform.Format
import com.android.build.api.transform.Status
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformOutputProvider
import com.qihoo360.replugin.transform.AbstractTransform
import org.apache.commons.codec.digest.DigestUtils

/**
 * author:gaoguanling
 * date:2021/9/3
 * time:16:44
 * email:gaoguanling@360.cn
 * link:
 */
object TransformTaskCreator {
    private const val FILE_NUM_PEAR_TASK = 10 // 默认一个TASK 顺序处理10个class文件
    private const val JAR_NUM_PEAR_TASK = 2 // 默认一个TASK顺序处理2个jar文件
    private const val tag = "TransformTaskCreator"

    fun createTask(
        isIncremental: Boolean,
        transformInput: TransformInput,
        outputProvider: TransformOutputProvider,
        transForm: AbstractTransform,
        processTask: (TransformTask) -> Unit
    ) {
        createFileTask(isIncremental, transformInput, outputProvider, transForm, processTask)
        createJarTask(isIncremental, transformInput, outputProvider, transForm, processTask)
    }

    private fun createFileTask(
        isIncremental: Boolean,
        transformInput: TransformInput,
        outputProvider: TransformOutputProvider,
        transForm: AbstractTransform,
        processTask: (TransformTask) -> Unit
    ) {
        transformInput
            .directoryInputs
            .asSequence()
            .flatMap { directoryInput ->
                ClassFileProcessor.processClass(
                    isIncremental,
                    directoryInput,
                    outputProvider
                )
            }
            .chunked(FILE_NUM_PEAR_TASK)
            .forEach { classInfoList ->
                processTask(FileTransformTask(classInfoList, transForm))
            }
    }

    private fun createJarTask(
        isIncremental: Boolean,
        transformInput: TransformInput,
        outputProvider: TransformOutputProvider,
        transForm: AbstractTransform,
        processTask: (TransformTask) -> Unit
    ) {
        transformInput
            .jarInputs
            .asSequence()
            .filter { jarInput ->
                if (isIncremental) {
                    val md5 = DigestUtils.md5Hex(jarInput.file.absolutePath)
                    val out = outputProvider.getContentLocation(
                        md5, jarInput.contentTypes, jarInput.scopes, Format.JAR
                    )

                    when (jarInput.status!!) {
                        Status.NOTCHANGED -> {
                            val exist = out.exists()
                            !exist
                        }
                        Status.REMOVED -> {
                            if (out.exists())
                                out.delete()
                            false
                        }
                        Status.ADDED, Status.CHANGED -> {
                            true
                        }

                    }
                } else {
                    true
                }
            }
            .chunked(JAR_NUM_PEAR_TASK)
            .forEach { jarInputList ->
                processTask(
                    JarTransformTask(
                        jarInputList,
                        outputProvider,
                        transForm
                    )
                )
            }
    }
}