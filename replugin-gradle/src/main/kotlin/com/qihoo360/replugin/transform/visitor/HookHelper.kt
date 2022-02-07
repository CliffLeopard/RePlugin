package com.qihoo360.replugin.transform.visitor

import com.qihoo360.replugin.hook.HookMethod
import com.qihoo360.replugin.hook.MethodInfoDesc
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.Method

/**
 * author:gaoguanling
 * date:2021/11/18
 * time:19:38
 * email:gaoguanling@360.cn
 * link:
 */
object HookHelper {
    fun prepareTargetMethodParam(
        methodVisitor: MethodVisitor,
        opcode: Int,
        isStatic: Boolean,
        hookMethod: HookMethod,
        isInterface: Boolean
    ) {
        val originMethod = Method(hookMethod.methodName, hookMethod.methodDesc)
        newMethodInfo(methodVisitor, opcode, hookMethod.className, originMethod.name, originMethod.descriptor, isInterface)
        var beginIndex = 0
        if (!isStatic) {
            methodVisitor.visitVarInsn(Opcodes.ALOAD, 0)
            beginIndex = 1
        }

        for (i in originMethod.argumentTypes.indices) {
            methodVisitor.visitVarInsn(originMethod.argumentTypes[i].getOpcode(Opcodes.ILOAD), i + beginIndex)
        }
    }

    fun callTargetMethod(
        methodVisitor: MethodVisitor,
        hookMethod: HookMethod,
        isInterface: Boolean,
    ) {
        methodVisitor.visitMethodInsn(
            Opcodes.INVOKESTATIC,
            hookMethod.targetClassName,
            hookMethod.targetMethodName,
            hookMethod.targetMethodDesc,
            isInterface
        )
    }

    fun newMethodInfo(mv: MethodVisitor, opcode: Int, owner: String, name: String, desc: String, isInterface: Boolean) {
        mv.visitTypeInsn(Opcodes.NEW, methodInfoDesc.owner)
        mv.visitInsn(Opcodes.DUP)
        mv.visitLdcInsn(owner)
        mv.visitLdcInsn(name)
        mv.visitLdcInsn(desc)
        mv.visitIntInsn(Opcodes.SIPUSH, opcode)
        if (isInterface)
            mv.visitInsn(Opcodes.ICONST_1)
        else
            mv.visitInsn(Opcodes.ICONST_0)

        mv.visitMethodInsn(
            Opcodes.INVOKESPECIAL,
            methodInfoDesc.owner,
            methodInfoDesc.name,
            methodInfoDesc.desc,
            methodInfoDesc.isInterface
        )
    }

    val methodInfoDesc = MethodInfoDesc(
        "com/qihoo360/replugin/base/hook/MethodInfo",
        "<init>",
        "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;IZ)V",
        false
    )
}