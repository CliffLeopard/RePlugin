package com.qihoo360.replugin.transform.visitor

import com.qihoo360.replugin.Constants
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes.ASM9

/**
 * author:gaoguanling
 * date:2021/9/11
 * time:22:53
 * email:gaoguanling@360.cn
 * link:
 */
class FilterClassVisitor(context: InstrumentationContext) : PluginClassVisitor(context) {
    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        context.superClassName = superName
        if (name.isNullOrEmpty())
            return

    }

    override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor? {
        if (!descriptor.isNullOrEmpty()) {
            if (descriptor == Constants.SKIP_ANNOTATION) {
                context.skipClass = true
            }
            if (descriptor == Constants.MODIFIED_ANNOTATION) {
                context.classModified = true
            }
        }
        return null
    }

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        return object : MethodVisitor(ASM9) {
            override fun visitAnnotation(
                annotationDesc: String,
                visible: Boolean
            ): AnnotationVisitor? {
                if (!name.isNullOrEmpty() && !descriptor.isNullOrEmpty() && annotationDesc == Constants.SKIP_ANNOTATION) {
                    context.addSkippedMethod(name, descriptor)
                }
                return null
            }
        }
    }
}