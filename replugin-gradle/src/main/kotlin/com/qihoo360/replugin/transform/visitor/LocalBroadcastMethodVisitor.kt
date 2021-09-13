package com.qihoo360.replugin.transform.visitor

import com.qihoo360.replugin.transform.visitor.LocalBroadcastClassVisitor.Companion.originClass
import com.qihoo360.replugin.transform.visitor.LocalBroadcastClassVisitor.Companion.targetClass
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes.ASM9

/**
 * author:gaoguanling
 * date:2021/9/8
 * time:10:50
 * email:gaoguanling@360.cn
 * link:
 */
class LocalBroadcastMethodVisitor(val className: String?, mv: MethodVisitor?) :
    MethodVisitor(ASM9, mv) {
    override fun visitMethodInsn(
        opcode: Int,
        owner: String?,
        name: String?,
        descriptor: String?,
        isInterface: Boolean
    ) {
        if (notChange(owner)) {
            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface)
        } else {
            super.visitMethodInsn(
                opcode,
                owner?.replace(originClass, targetClass), name,
                descriptor?.replace(originClass, targetClass), isInterface
            )
        }
    }

    override fun visitLocalVariable(
        name: String?,
        descriptor: String?,
        signature: String?,
        start: Label?,
        end: Label?,
        index: Int
    ) {
        super.visitLocalVariable(
            name,
            descriptor?.replace(originClass, targetClass) ?: descriptor,
            signature,
            start,
            end,
            index
        )
    }

    override fun visitFieldInsn(opcode: Int, owner: String?, name: String?, descriptor: String?) {
        if (notChange(owner))
            super.visitFieldInsn(opcode, owner, name, descriptor)
        else
            super.visitFieldInsn(opcode, owner, name, descriptor?.replace(originClass, targetClass))
    }

    private fun notChange(owner: String?): Boolean {
        return owner == targetClass || className == originClass
    }
}