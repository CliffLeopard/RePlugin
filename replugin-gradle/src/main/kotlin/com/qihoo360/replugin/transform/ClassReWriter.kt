package com.qihoo360.replugin.transform

import com.qihoo360.replugin.Log
import com.qihoo360.replugin.config.BaseExtension
import com.qihoo360.replugin.transform.bean.TransformClassInfo
import com.qihoo360.replugin.transform.visitor.*
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.util.CheckClassAdapter


/**
 * author:CliffLeopard
 * date:4/7/21
 * time:12:46 AM
 * email:precipiceleopard@gmail.com
 * link:
 */
object ClassReWriter {
    private const val tag = "ClassReWriter"
    fun transform(
        classInfo: TransformClassInfo,
        inputBytes: ByteArray,
        extension: BaseExtension
    ): ByteArray? {

        val context = InstrumentationContext(classInfo, extension)
        val classReader = ClassReader(inputBytes)
        val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)

        filerClass(context)
        //filerClassByReader(classReader, context)

        if (context.skipClass) {
            Log.detail(tag, "Skip: ${classInfo.name}")
            return null
        }
        val verifierVisitor = CheckClassAdapter(classWriter)
        val activityVisitor = ActivityClassVisitor(verifierVisitor, context)
        val broadCastVisitor = LocalBroadcastClassVisitor(activityVisitor, context)
        val providerVisitor = ProviderClassClassVisitor(broadCastVisitor, context)
        val identifierClassVisitor = IdentifierClassVisitor(providerVisitor, context)
        val constantClassVisitor = ConstantClassVisitor(identifierClassVisitor,context)

        classReader.accept(
            constantClassVisitor,
            ClassReader.SKIP_FRAMES or ClassReader.EXPAND_FRAMES
        )
        return if (context.classModified) {
            Log.i(
                tag,
                "ChangedClass:\n      class:${context.classInfo.name}\n      fromJar:${context.classInfo.fromJar}\n      toPath:${context.classInfo.toPath}"
            )
            classWriter.toByteArray()
        } else
            null
    }

    private fun filerClass(context: InstrumentationContext) {
        if (context.extension.isTargetClass(context.classInfo.name))
            context.skipClass = true
    }

    /**
     * 由于我们业务比较简单，不需要使用这种方式过滤，打出接口以后使用
     */
    private fun filerClassByReader(classReader: ClassReader, context: InstrumentationContext) {
        classReader.accept(
            FilterClassVisitor(context),
            ClassReader.SKIP_DEBUG or ClassReader.SKIP_CODE or ClassReader.SKIP_FRAMES
        )
    }
}