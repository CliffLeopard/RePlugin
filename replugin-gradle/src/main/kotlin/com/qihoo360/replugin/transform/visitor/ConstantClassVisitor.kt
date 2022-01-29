package com.qihoo360.replugin.transform.visitor

import com.qihoo360.replugin.plugin.PluginExtension
import com.qihoo360.replugin.transform.bean.InstrumentationContext
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

/**
 * author:gaoguanling
 * date:2021/9/14
 * time:14:55
 * email:gaoguanling@360.cn
 * link:
 */
open class ConstantClassVisitor(cv: ClassVisitor, context: InstrumentationContext) :
    PluginClassVisitor(cv, context) {

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        val mv = super.visitMethod(access, name, descriptor, signature, exceptions)
        if (context.classInfo.name == originClass && access == Opcodes.ACC_STATIC && name == "<clinit>" && descriptor == "()V") {
            return ConstantMethodVisitor(mv)
        }
        return mv
    }

    inner class ConstantMethodVisitor(mv: MethodVisitor) :
        MethodVisitor(Opcodes.ASM9, mv) {
        override fun visitFieldInsn(
            opcode: Int,
            owner: String?,
            name: String?,
            descriptor: String?
        ) {
            if (opcode == Opcodes.PUTSTATIC && owner == originClass && name == "PLUGIN_NAME" && descriptor == "Ljava/lang/String;") {
                visitInsn(Opcodes.POP)
                val pluginExtension = context.extension as PluginExtension
                visitLdcInsn(pluginExtension.pluginName ?: pluginExtension.applicationId)
                context.classModified = true
                super.visitFieldInsn(opcode, owner, name, descriptor)
            } else
                super.visitFieldInsn(opcode, owner, name, descriptor)
        }
    }

    companion object {
        const val originClass = "com/qihoo/appstore/export/proxy/Constant"
    }
}