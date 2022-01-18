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
                try {
                    val bytes = IOUtils.toByteArray(inputStream)
                    val changedBytes = transForm.transformClass(classInfo, bytes)
                    var status = 2
                    FileOutputStream(classInfo.toPath).use { outputStream ->
                        if (changedBytes == null) {
                            IOUtils.write(bytes, outputStream)
                            status = 0
                        } else if (changedBytes.isNotEmpty()) {
                            IOUtils.write(changedBytes, outputStream)
                            status = 1
                        }
                    }
                    printLog(classInfo, status)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e(tag, e.localizedMessage)
                }
            }
        }
        return true
    }


    private fun printLog(classInfo: TransformClassInfo, status: Int) {
        when (status) {
            0 ->
                Log.i(
                    tag,
                    "TransformedClass:${classInfo.className}  Thread:${Thread.currentThread().name}\nFrom:${classInfo.fromPath} \n  To: ${classInfo.toPath}"
                )
            1 -> {
                Log.detail(
                    tag,
                    "TransformedClass:${classInfo.className}  Thread:${Thread.currentThread().name}\nNotChanged:${classInfo.fromPath}"
                )
            }
            2 -> {
                Log.i(
                    tag,
                    "TransformedClass:${classInfo.className}  Thread:${Thread.currentThread().name}\nSkipped:${classInfo.fromPath}"
                )
            }
        }
    }
}