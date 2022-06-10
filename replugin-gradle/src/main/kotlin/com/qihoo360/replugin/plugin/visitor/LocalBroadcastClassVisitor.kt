package com.qihoo360.replugin.plugin.visitor

import com.qihoo360.replugin.transform.bean.InstrumentationContext
import com.qihoo360.replugin.transform.visitor.PluginClassVisitor
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.FieldVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes.ASM9

/**
 * author:gaoguanling
 * date:2021/9/8
 * time:10:16
 * email:gaoguanling@360.cn
 * link:
 */
class LocalBroadcastClassVisitor(cv: ClassVisitor?, context: InstrumentationContext) : PluginClassVisitor(cv,context) {
    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        className = name
        super.visit(version, access, name, signature, superName, interfaces)
    }

    override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor {
        if (className == originClass)
            return super.visitAnnotation(descriptor, visible)

        if(!descriptor.isNullOrEmpty() && descriptor.contains(originClass))
            context.classModified = true

        return LocalBroadcastAnnotationVisitor(context,
            super.visitAnnotation(
                descriptor?.replace(
                    originClass,
                    targetClass
                ) ?: descriptor, visible
            )
        )
    }

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        if (className == originClass)
            return super.visitMethod(access, name, descriptor, signature, exceptions)


        if(!descriptor.isNullOrEmpty() && descriptor.contains(originClass))
            context.classModified = true

        val originMV = super.visitMethod(
            access,
            name,
            descriptor?.replace(originClass, targetClass) ?: descriptor,
            signature,
            exceptions
        )
        return LocalBroadcastMethodVisitor(className, originMV)
    }

    override fun visitField(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        value: Any?
    ): FieldVisitor {
        if (className == originClass)
            return super.visitField(access, name, descriptor, signature, value)

        if(!descriptor.isNullOrEmpty() && descriptor.contains(originClass))
            context.classModified = true

        return super.visitField(
            access,
            name,
            descriptor?.replace(originClass, targetClass) ?: descriptor,
            signature,
            value
        )

    }

    class LocalBroadcastAnnotationVisitor(private val context: InstrumentationContext, av: AnnotationVisitor) : AnnotationVisitor(ASM9, av) {
        override fun visit(name: String?, value: Any?) {
            return if (value != null && value is String) {
                if(value.contains(originClass))
                    context.classModified = true

                super.visit(name, value.replace(originClass, targetClass))
            }else
                super.visit(name, value)
        }

        override fun visitArray(name: String?): AnnotationVisitor {
            return LocalBroadcastAnnotationVisitor(context,super.visitArray(name))
        }
    }

    companion object {
        const val originClass = "androidx/localbroadcastmanager/content/LocalBroadcastManager"
        const val targetClass = "com/qihoo360/replugin/loader/b/PluginLocalBroadcastManager"
    }
}