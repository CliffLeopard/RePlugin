package com.qihoo360.replugin.plugin.visitor

import com.qihoo360.replugin.transform.bean.InstrumentationContext
import com.qihoo360.replugin.transform.visitor.PluginClassVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.GeneratorAdapter

/**
 * author:gaoguanling
 * date:2021/9/9
 * time:19:32
 * email:gaoguanling@360.cn
 * link:
 */
class IdentifierClassVisitor(cv: ClassVisitor?, context: InstrumentationContext) :
    PluginClassVisitor(cv, context) {
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

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        val mv = super.visitMethod(access, name, descriptor, signature, exceptions)
        return IdentifierMethodVisitor(mv, access, name, descriptor)
    }

    inner class IdentifierMethodVisitor(
        mv: MethodVisitor?, access: Int,
        name: String?,
        descriptor: String?
    ) : GeneratorAdapter(Opcodes.ASM9, mv, access, name, descriptor) {
        override fun visitMethodInsn(
            opcode: Int,
            owner: String?,
            name: String?,
            descriptor: String?,
            isInterface: Boolean
        ) {
            if (owner == originClass && name == originMethod && className != originClass) {
                pop()
                push(context.extension.applicationId)
                context.classModified = true
            }
            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface)
        }
    }


    companion object {
        const val originClass = "android/content/res/Resources"
        const val originMethod = "getIdentifier"
    }
}