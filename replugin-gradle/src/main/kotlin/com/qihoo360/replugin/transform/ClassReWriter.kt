package com.qihoo360.replugin.transform

import com.qihoo360.replugin.Log
import com.qihoo360.replugin.config.BaseExtension
import com.qihoo360.replugin.transform.bean.InstrumentationContext
import com.qihoo360.replugin.transform.bean.TransformClassInfo
import com.qihoo360.replugin.transform.visitor.*
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter


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

        //        val visitor = CheckClassAdapter(classWriter)
        var visitor: ClassVisitor = ActivityClassVisitor(classWriter, context)
        visitor = LocalBroadcastClassVisitor(visitor, context)
        visitor = ProviderClassClassVisitor(visitor, context)
        visitor = IdentifierClassVisitor(visitor, context)
        visitor = ConstantClassVisitor(visitor, context)
        if (!context.hookMethodConfig.isEmpty())
            visitor = HookDefineMethodClassVisitor(HookCallMethodClassVisitor(visitor, context), context)

        val lambdaConfig = context.hookLambdaConfig.findByClassName(context.classInfo.name)
        if (!lambdaConfig.isEmpty())
            visitor = HookLambdaClassVisitor(visitor, context,lambdaConfig)


        classReader.accept(
            visitor,
            ClassReader.SKIP_FRAMES or ClassReader.EXPAND_FRAMES
        )
        return if (context.classModified) {
            Log.i(
                tag,
                "ChangedClass:\n class:${context.classInfo.name}\n fromJar:${context.classInfo.fromJar}\n toPath:${context.classInfo.toPath}"
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