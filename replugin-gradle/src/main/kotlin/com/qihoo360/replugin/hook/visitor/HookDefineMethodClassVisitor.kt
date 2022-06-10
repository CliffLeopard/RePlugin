package com.qihoo360.replugin.hook.visitor

import com.qihoo360.replugin.hook.HookMethod
import com.qihoo360.replugin.hook.HookMethodContainer
import com.qihoo360.replugin.transform.bean.InstrumentationContext
import com.qihoo360.replugin.transform.visitor.PluginClassVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.commons.AdviceAdapter
import org.objectweb.asm.commons.GeneratorAdapter
import org.objectweb.asm.commons.Method

/**
 * author:gaoguanling
 * date:2021/11/18
 * time:10:46
 * email:gaoguanling@360.cn
 * link:
 */
class HookDefineMethodClassVisitor(cv: ClassVisitor, context: InstrumentationContext, private val hookMethods: HookMethodContainer) :
    PluginClassVisitor(cv, context) {
    override fun visit(version: Int, access: Int, name: String?, signature: String?, superName: String?, interfaces: Array<out String>?) {
        super.visit(version, access, name, signature, superName, interfaces)
        className = name
    }

    override fun visitMethod(access: Int, name: String?, descriptor: String?, signature: String?, exceptions: Array<out String>?): MethodVisitor? {
        val superVisitor = super.visitMethod(access, name, descriptor, signature, exceptions)
        if (className == null || name == null || name.isEmpty() || descriptor == null || descriptor.isEmpty())
            return superVisitor

        val methods = hookMethods.getMethodConfig(className!!, name, descriptor)
        if (methods == null || methods.isEmpty())
            return superVisitor

        val beforeHook = if (access and Opcodes.ACC_STATIC > 0) {
            methods[HookMethod.HookType.DEFINE_STATIC_BEFORE_METHOD]
        } else {
            methods[HookMethod.HookType.DEFINE_NORMAL_BEFORE_METHOD]
        }

        val afterHook = if (access and Opcodes.ACC_STATIC > 0) {
            methods[HookMethod.HookType.DEFINE_STATIC_AFTER_METHOD]
        } else {
            methods[HookMethod.HookType.DEFINE_NORMAL_AFTER_METHOD]
        }

        val replaceHook = if (access and Opcodes.ACC_STATIC > 0) {
            methods[HookMethod.HookType.DEFINE_STATIC_REPLACE_METHOD]
        } else {
            methods[HookMethod.HookType.DEFINE_NORMAL_REPLACE_METHOD]
        }

        val originMethod = Method(name, descriptor)
        if (!isEmpty(replaceHook)) {
            if (replaceHook!!.size > 1) {
                throw Exception("replaceHook同一个方法只能定义一个: class:$className  method:$name  desc:$descriptor")
            }
            return ReplaceMethodVisitor(superVisitor, replaceHook[0], access, name, descriptor)
        }

        val afterMethod = when {
            isEmpty(afterHook) -> null
            afterHook!!.size > 1 -> {
                throw Exception("afterHook同一个方法只能定义一个: class:$className  method:$name  desc:$descriptor")
            }
            else -> afterHook[0]
        }

        if (afterMethod != null || !isEmpty(beforeHook)) {
            return DefineMethodVisitor(superVisitor, originMethod, beforeHook, afterMethod, access, name, descriptor)
        }
        return superVisitor
    }

    private fun isEmpty(methods: List<HookMethod>?): Boolean {
        return methods == null || methods.isEmpty()
    }

    inner class ReplaceMethodVisitor(
        private val methodVisitor: MethodVisitor,
        private val hookMethod: HookMethod,
        access: Int,
        name: String,
        descriptor: String
    ) : GeneratorAdapter(Opcodes.ASM9, null, access, name, descriptor) {
        override fun visitCode() {
            methodVisitor.visitCode()
            HookHelper.prepareTargetMethodParam(
                methodVisitor,
                access,
                access and Opcodes.ACC_STATIC > 0,
                hookMethod,
                false
            )
            HookHelper.callTargetMethod(methodVisitor, hookMethod, false)
            methodVisitor.visitInsn(Type.getReturnType(hookMethod.methodDesc).getOpcode(Opcodes.IRETURN))
            context.classModified = true
        }

        override fun visitEnd() {
            if (access and Opcodes.ACC_ABSTRACT == 0) {
                methodVisitor.visitMaxs(0, 0)
            }
            methodVisitor.visitEnd()
        }
    }

    inner class DefineMethodVisitor(
        methodVisitor: MethodVisitor,
        private val originMethod: Method,
        private val beforeMethods: List<HookMethod>?,
        private val afterMethod: HookMethod?,
        access: Int,
        name: String,
        descriptor: String
    ) : AdviceAdapter(Opcodes.ASM9, methodVisitor, access, name, descriptor) {
        override fun onMethodEnter() {
            if (!isEmpty(beforeMethods)) {
                for (hookMethod in beforeMethods!!) {
                    HookHelper.prepareTargetMethodParam(
                        this,
                        access,
                        access and ACC_STATIC > 0,
                        hookMethod,
                        false
                    )
                    HookHelper.callTargetMethod(this, hookMethod, false)
                    context.classModified = true
                }
            }
        }

        override fun onMethodExit(opcode: Int) {
            if (afterMethod != null) {
                val resultObj = if (Type.VOID_TYPE != originMethod.returnType) newLocal(originMethod.returnType) else -1
                if (resultObj != -1)
                    storeLocal(resultObj)
                HookHelper.prepareTargetMethodParam(
                    this,
                    access,
                    access and ACC_STATIC > 0,
                    afterMethod,
                    false
                )
                if (resultObj != -1)
                    loadLocal(resultObj, originMethod.returnType)
                HookHelper.callTargetMethod(this, afterMethod, false)
                context.classModified = true
            }
        }
    }
}