package com.qihoo360.replugin.transform.pool

import com.qihoo360.replugin.Log
import com.qihoo360.replugin.transform.AbstractTransform
import com.qihoo360.replugin.transform.bean.TransformClassInfo
import org.apache.commons.io.IOUtils
import java.io.FileInputStream
import java.io.FileOutputStream

/**
 * author:gaoguanling
 * date:2021/8/31
 * time:15:42
 * email:gaoguanling@360.cn
 * link:
 */
class FileTransformTask(
    private val classInfoList: List<TransformClassInfo>,
    val transForm: AbstractTransform
) : TransformTask() {
    private val tag = "FileTransformTask"
    override fun compute(): Boolean {
        classInfoList.forEach { classInfo ->
            FileInputStream(classInfo.fromPath).use { inputStream ->
                val bytes = IOUtils.toByteArray(inputStream)
                val changedBytes = transForm.transformClass(classInfo, bytes)
                FileOutputStream(classInfo.toPath).use { outputStream ->
                    IOUtils.write(changedBytes ?: bytes, outputStream)
                }
                printLog(classInfo, changedBytes == null)
            }
        }
        return true
    }

    private fun printLog(classInfo: TransformClassInfo, changed: Boolean) {
        if (changed) {
            Log.detail(
                tag,
                "TransformedClass:${classInfo.className}  Thread:${Thread.currentThread().name}\nFrom:${classInfo.fromPath} \n  To: ${classInfo.toPath}"
            )
        } else {
            Log.detail(
                tag,
                "TransformedClass:${classInfo.className}  Thread:${Thread.currentThread().name}\nNotChanged:${classInfo.fromPath}"
            )
        }
    }
}