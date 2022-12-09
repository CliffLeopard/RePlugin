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
                        if (!entry.isDirectory && entry.name.endsWith(".class")) {
                            val transformClassInfo = buildTransformClassInfo(entry.name, jarInput, out)
                            val bytes = IOUtils.toByteArray(jar)
                            try {
                                val changedBytes =
                                    transForm.transformClass(transformClassInfo, bytes)
                                if (changedBytes == null) {
                                    outJar.putNextEntry(outEntry)
                                    IOUtils.write(bytes, outJar)
                                    Log.detail(tag, "TransformedJarClass:NotChanged: ${entry.name}")
                                } else if (changedBytes.isNotEmpty()) {
                                    outJar.putNextEntry(outEntry)
                                    IOUtils.write(changedBytes, outJar)
                                    Log.i(tag, "TransformedJarClass: ${entry.name}")
                                } else {
                                    Log.i(tag, "TransformedJarClass:SkippedClass: ${entry.name}")
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                                Log.e(tag, e.localizedMessage)
                            }
                        } else {
                            outJar.putNextEntry(outEntry)
                            IOUtils.copy(jar, outJar)
                        }
                        entry = jar.nextEntry
                    }
                }
            }
        }
        return true
    }

    private fun buildTransformClassInfo(
        entryName: String,
        jarInput: JarInput,
        out: File
    ): TransformClassInfo {
        val name = entryName.removeSuffix(".class")
        val separatorIndex = entryName.lastIndexOf(File.separatorChar)
        val packageName =
            if (separatorIndex > -1)
                entryName
                    .substring(0, separatorIndex)
                    .removePrefix(File.separator)
                    .removeSuffix(File.separator)
            else ""
        val isInnerClass = entryName.contains("$")
        val className =
            if (separatorIndex > -1)
                entryName.substring(separatorIndex + 1)
                    .removeSuffix(".class")
            else
                entryName.removeSuffix(".class")

        return TransformClassInfo(
            name,
            packageName,
            className,
            jarInput.file.absolutePath,
            out.absolutePath,
            jarInput,
            true,
            isInnerClass
        )
    }

    private fun copyEntry(entry: ZipEntry): ZipEntry {
        val newEntry = ZipEntry(entry.name)
        newEntry.comment = entry.comment
        newEntry.extra = entry.extra
        return newEntry
    }
}