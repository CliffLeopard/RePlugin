package com.qihoo360.replugin.transform.visitor

import com.qihoo360.replugin.config.HookMethodContainer
import com.qihoo360.replugin.config.MethodInfoDesc
import com.qihoo360.replugin.config.TargetMethod
import com.qihoo360.replugin.transform.bean.InstrumentationContext
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.commons.GeneratorAdapter
import org.objectweb.asm.commons.Method

/**
 * author:gaoguanling
 * date:2021/11/15
 * time:19:31
 * email:gaoguanling@360.cn
 * link: 此类只在方法调用处Hook
 */
class MethodHookClassVisitor(cv: ClassVisitor, context: InstrumentationContext) :
    PluginClassVisitor(cv, context) {
    private lateinit var hookMethods: HookMethodContainer

    override fun visit(version: Int, access: Int, name: String?, signature: String?, superName: String?, interfaces: Array<out String>?) {
        super.visit(version, access, name, signature, superName, interfaces)
        className = name
        hookMethods = HookMethodContainer.getInstance(context.extension)
    }

    override fun visitMethod(access: Int, name: String, descriptor: String, signature: String?, exceptions: Array<out String>?): MethodVisitor {
        return HookMethodVisitor(super.visitMethod(access, name, descriptor, signature, exceptions), access, name, descriptor)
    }

    inner class HookMethodVisitor(methodVisitor: MethodVisitor, access: Int, name: String, descriptor: String) :
        GeneratorAdapter(Opcodes.ASM9, methodVisitor, access, name, descriptor) {

        override fun visitMethodInsn(opcode: Int, owner: String, name: String, descriptor: String, isInterface: Boolean) {
            when (opcode) {
                Opcodes.INVOKEDYNAMIC ->
                    super.visitMethodInsn(opcode, owner, name, descriptor, isInterface)
                else ->
                    if (!hookMethod(opcode, owner, name, descriptor, isInterface))
                        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface)
                    else
                        context.classModified = true
            }
        }

        private fun hookMethod(opcode: Int, owner: String, name: String, desc: String, isInterface: Boolean): Boolean {
            val methods = context.hookMethodConfig.getMethodConfig(owner, name, desc)
            if (methods == null || methods.isEmpty())
                return false
            val beforeHook = if (opcode == Opcodes.INVOKESTATIC) {
                methods[TargetMethod.HookType.CALL_STATIC_BEFORE_METHOD]
            } else {
                methods[TargetMethod.HookType.CALL_NORMAL_BEFORE_METHOD]
            }

            val afterHook = if (opcode == Opcodes.INVOKESTATIC) {
                methods[TargetMethod.HookType.CALL_STATIC_AFTER_METHOD]
            } else {
                methods[TargetMethod.HookType.CALL_NORMAL_AFTER_METHOD]
            }

            val replaceHook = if (opcode == Opcodes.INVOKESTATIC) {
                methods[TargetMethod.HookType.CALL_STATIC_REPLACE_METHOD]
            } else {
                methods[TargetMethod.HookType.CALL_STATIC_REPLACE_METHOD]
            }

            if (isEmpty(beforeHook) && isEmpty(afterHook) && isEmpty(replaceHook))
                return false

            val originMethod = Method(name, desc)
            val locals = IntArray(originMethod.argumentTypes.size)

            for (i in locals.indices.reversed()) {
                locals[i] = newLocal(originMethod.argumentTypes[i])
                storeLocal(locals[i])
            }

            val thisObj = if (opcode != Opcodes.INVOKESTATIC) newLocal(Type.getType("L$owner")) else -1
            if (opcode != Opcodes.INVOKESTATIC)
                storeLocal(thisObj)

            newMethodInfo(opcode, owner, name, desc, isInterface)
            val info = newLocal(Type.getType("L${methodInfoDesc.owner}"))
            storeLocal(info)

            val replaceResult = !isEmpty(replaceHook) && replaceHookMethod(afterHook!!, isInterface, locals, thisObj, info)
            if (replaceResult)
                return true

            val beforeResult = !isEmpty(beforeHook) && beforeHookMethod(beforeHook!!, isInterface, locals, thisObj, info)
            loadLocalVariable(locals, thisObj, -1)
            super.visitMethodInsn(opcode, owner, name, desc, isInterface)
            val afterResult =
                !isEmpty(afterHook) && afterHookMethod(afterHook!!, originMethod, isInterface, locals, thisObj, info)

            return beforeResult or afterResult
        }

        private fun beforeHookMethod(
            methods: List<TargetMethod>, isInterface: Boolean,
            locals: IntArray, thisObj: Int, info: Int
        ): Boolean {
            methods.forEach { method ->
                loadLocalVariable(locals, thisObj, info)
                super.visitMethodInsn(Opcodes.INVOKESTATIC, method.targetClassName, method.targetMethodName, method.targetMethodDesc, isInterface)
            }
            return true
        }

        private fun afterHookMethod(
            methods: List<TargetMethod>, originMethod: Method, isInterface: Boolean,
            locals: IntArray, thisObj: Int, info: Int
        ): Boolean {
            if (methods.size != 1)
                return false
            val resultObj = if (Type.VOID_TYPE != originMethod.returnType) newLocal(originMethod.returnType) else -1
            if (resultObj != -1)
                storeLocal(resultObj)
            loadLocalVariable(locals, thisObj, info, resultObj)
            super.visitMethodInsn(
                Opcodes.INVOKESTATIC,
                methods[0].targetClassName,
                methods[0].targetMethodName,
                methods[0].targetMethodDesc,
                isInterface
            )
            return true
        }

        private fun replaceHookMethod(
            methods: List<TargetMethod>, isInterface: Boolean,
            locals: IntArray, thisObj: Int, info: Int
        ): Boolean {
            if (methods.size != 1)
                return false
            loadLocalVariable(locals, thisObj, info, -1)
            super.visitMethodInsn(
                Opcodes.INVOKESTATIC,
                methods[0].targetClassName,
                methods[0].targetMethodName,
                methods[0].targetMethodDesc,
                isInterface
            )
            return false
        }


        private fun isEmpty(methods: List<TargetMethod>?): Boolean {
            return methods == null || methods.isEmpty()
        }

        /**
         * 创建MethodInfo信息
         */
        private fun newMethodInfo(opcode: Int, owner: String, name: String, desc: String, isInterface: Boolean) {
            visitTypeInsn(Opcodes.NEW, methodInfoDesc.owner)
            visitInsn(Opcodes.DUP)
            visitLdcInsn(owner)
            visitLdcInsn(name)
            visitLdcInsn(desc)
            visitIntInsn(Opcodes.SIPUSH, opcode)
            if (isInterface)
                visitInsn(Opcodes.ICONST_1)
            else
                visitInsn(Opcodes.ICONST_0)

            visitMethodInsn(
                Opcodes.INVOKESPECIAL,
                methodInfoDesc.owner,
                methodInfoDesc.name,
                methodInfoDesc.desc,
                methodInfoDesc.isInterface
            )
        }

        private fun loadLocalVariable(locals: IntArray, thisObj: Int = -1, info: Int = -1, result: Int = -1) {
            if (info != -1)
                loadLocal(info)
            if (thisObj != -1)
                loadLocal(thisObj)
            for (l in locals)
                loadLocal(l)
            if (result != -1) {
                loadLocal(result)
            }
        }
    }

    companion object {
        val methodInfoDesc = MethodInfoDesc(
            "com/qihoo360/replugin/base/hook/MethodInfo",
            "<init>",
            "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;IZ)V",
            false
        )
    }
}