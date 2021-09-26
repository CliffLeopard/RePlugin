package com.qihoo360.replugin.transform.pool

import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.TransformOutputProvider
import com.qihoo360.replugin.Log
import com.qihoo360.replugin.transform.AbstractTransform
import com.qihoo360.replugin.transform.bean.TransformClassInfo
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.IOUtils
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

/**
 * author:gaoguanling
 * date:2021/8/31
 * time:15:42
 * email:gaoguanling@360.cn
 * link:
 */
class JarTransformTask(
    private val jarInputList: List<JarInput>,
    private val outputProvider: TransformOutputProvider,
    private val transForm: AbstractTransform
) : TransformTask() {
    private val tag = "JarTransformTask"
    override fun compute(): Boolean {
        jarInputList.forEach { jarInput ->

            val md5 = DigestUtils.md5Hex(jarInput.file.absolutePath)
            val out = outputProvider.getContentLocation(
                md5, jarInput.contentTypes, jarInput.scopes, Format.JAR
            )
            out.parentFile.mkdirs()
            if (out.exists())
                out.delete()

            Log.detail(
                tag,
                "TransformJar Thread: ${Thread.currentThread().name}\n  From: ${jarInput.file.absolutePath}\n    To: ${out.absolutePath}"
            )

            ZipOutputStream(FileOutputStream(out)).use { outJar ->
                ZipInputStream(FileInputStream(jarInput.file)).use { jar ->
                    var entry: ZipEntry? = jar.nextEntry
                    while (entry != null) {
                        val outEntry: ZipEntry = copyEntry(entry)
                        outJar.putNextEntry(outEntry)
                        if (!entry.isDirectory && entry.name.endsWith(".class")) {
                            val name = entry.name.removeSuffix(".class")
                            val separatorIndex = entry.name.lastIndexOf(File.separatorChar)
                            val packageName =
                                if (separatorIndex > -1)
                                    entry.name
                                        .substring(0, separatorIndex)
                                        .removePrefix(File.separator)
                                        .removeSuffix(File.separator)
                                else ""
                            val isInnerClass = entry.name.contains("$")
                            val className =
                                if (separatorIndex > -1)
                                    entry.name.substring(separatorIndex + 1)
                                        .removeSuffix(".class")
                                else
                                    entry.name.removeSuffix(".class")

                            val transformClassInfo = TransformClassInfo(
                                name,
                                packageName,
                                className,
                                jarInput.file.absolutePath,
                                out.absolutePath,
                                true,
                                isInnerClass
                            )
                            val bytes = IOUtils.toByteArray(jar)
                            val changedBytes = transForm.transformClass(transformClassInfo, bytes)
                            IOUtils.write(changedBytes ?: bytes, outJar)
                            if (changedBytes != null)
                                Log.detail(tag, "TransformedJarClass: ${entry.name}")
                            else
                                Log.detail(tag, "TransformedJarClass:NotChanged: ${entry.name}")
                        } else {
                            IOUtils.copy(jar, outJar)
                        }
                        entry = jar.nextEntry
                    }
                }
            }
        }
        return true
    }

    private fun copyEntry(entry: ZipEntry): ZipEntry {
        val newEntry = ZipEntry(entry.name)
        newEntry.comment = entry.comment
        newEntry.extra = entry.extra
        return newEntry
    }
}