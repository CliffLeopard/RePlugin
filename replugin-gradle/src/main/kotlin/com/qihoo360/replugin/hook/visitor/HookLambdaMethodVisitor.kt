package com.qihoo360.replugin.hook.visitor

import com.qihoo360.replugin.Log
import com.qihoo360.replugin.hook.HookLambda
import com.qihoo360.replugin.hook.HookLambdaContainer
import com.qihoo360.replugin.transform.bean.InstrumentationContext
import org.objectweb.asm.*
import org.objectweb.asm.commons.AdviceAdapter
import org.objectweb.asm.commons.Method

/**
 * author:gaoguanling
 * date:2022/1/25
 * time:16:41
 * email:gaoguanling@360.cn
 * link:
 */
class HookLambdaMethodVisitor(
    private val classVisitor: ClassVisitor,
    private val constant: InstrumentationContext,
    methodVisitor: MethodVisitor,
    access: Int,
    name: String?,
    descriptor: String?,
    private val hookConfig: HookLambdaContainer
) : AdviceAdapter(Opcodes.ASM9, methodVisitor, access, name, descriptor) {

    private val wrappedHandles = mutableSetOf<ChangeState>()
    private val owner: String = constant.classInfo.name

    override fun visitInvokeDynamicInsn(
        name: String?,
        descriptor: String?,
        bootstrapMethodHandle: Handle?,
        vararg bootstrapMethodArguments: Any?
    ) {
        if (name.isNullOrEmpty() || descriptor.isNullOrEmpty())
            return super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, *bootstrapMethodArguments)

        val methodType: Type = bootstrapMethodArguments[0] as Type
        val handle: Handle = bootstrapMethodArguments[1] as Handle
        var desc = handle.desc
        var tag = handle.tag

        val config = hookConfig.findByLambdaMethodNameAndDesc(name, desc)
        if (config.isEmpty())
            return super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, *bootstrapMethodArguments)

        if (handle.owner != owner) {
            tag = Opcodes.H_INVOKESTATIC
            if (handle.tag != Opcodes.H_INVOKESTATIC) {
                desc = "(L${handle.owner};${handle.desc.removePrefix("(")}"
            }
        }
        val changedHandle = Handle(
            tag,
            owner,
            getTargetMethodName(handle.name),
            desc,
            handle.isInterface
        )
        wrappedHandles.add(ChangeState(changedHandle, handle, config))
        constant.classModified = true
        super.visitInvokeDynamicInsn(
            name,
            descriptor,
            bootstrapMethodHandle,
            methodType,
            changedHandle,
            methodType
        )
    }

    override fun visitEnd() {
        super.visitEnd()
        createMethod(classVisitor)
    }

    private fun printLog(hooker: HookLambdaContainer, handle: Handle, type: String) {
        if (hooker.get().size > 1) {
            val message = "在同一个方法中同一个Lambda表达式只能定义一个$type: class:${constant.classInfo.name} method:${handle.name} desc:${handle.desc}"
            Log.e(tag, message)
            throw Exception(message)
        }
    }

    private fun createMethod(classVisitor: ClassVisitor) {
        val iterator = wrappedHandles.iterator()
        while (iterator.hasNext()) {
            val state = iterator.next()
            val lambdaHooker = state.config
            val handle = state.handle
            val originHandle = state.originHandle

            val beforeHooker = lambdaHooker.findByType(HookLambda.HookType.BEFORE)
            val afterHooker = lambdaHooker.findByType(HookLambda.HookType.AFTER)
            val replaceHooker = lambdaHooker.findByType(HookLambda.HookType.REPLACE)

            printLog(beforeHooker, handle, "beforeHooker")
            printLog(afterHooker, handle, "afterHooker")
            printLog(replaceHooker, handle, "replaceHooker")

            val originMethod = Method(originHandle.name, originHandle.desc)
            val resultObj = if (Type.VOID_TYPE != originMethod.returnType) newLocal(originMethod.returnType) else -1
            val isStatic = handle.tag == Opcodes.H_INVOKESTATIC
            val hasOtherObj = handle.desc != originHandle.desc
            val access = Opcodes.ACC_PRIVATE or Opcodes.ACC_SYNTHETIC or (if (isStatic) Opcodes.ACC_STATIC else Opcodes.ACC_FINAL)
            val methodVisitor = classVisitor.visitMethod(access, handle.name, handle.desc, null, null)
            methodVisitor.visitCode()
            val label0 = Label()
            methodVisitor.visitLabel(label0)

            val preIndex = if (isStatic && !hasOtherObj) 0 else 1   // 正式参数开始位置
            val localIndex = if (isStatic) 0 else 1
            val arguments = Type.getType(handle.desc).argumentTypes
            val argLength = if (hasOtherObj) arguments.size - 1 else arguments.size // 正式参数长度

            // beforeHooker
            run {
                val beforeIterator = beforeHooker.get().iterator()
                while (beforeIterator.hasNext()) {
                    val before = beforeIterator.next()
                    for (i in 0 until argLength)
                        methodVisitor.visitVarInsn(Opcodes.ALOAD, preIndex + i)
                    methodVisitor.visitMethodInsn(
                        Opcodes.INVOKESTATIC,
                        before.targetClass,
                        before.targetMethod,
                        handle.desc,
                        false
                    )
                }
            }

            // replace or  originMethod
            run {
                if (!isStatic)
                    methodVisitor.visitVarInsn(Opcodes.ALOAD, 0)
                for (index in arguments.indices) {
                    methodVisitor.visitVarInsn(Opcodes.ALOAD, index + localIndex)
                }

                if (replaceHooker.isEmpty()) {
                    val originAccess = when (originHandle.tag) {
                        Opcodes.H_INVOKESTATIC -> Opcodes.INVOKESTATIC
                        Opcodes.H_INVOKEVIRTUAL -> Opcodes.INVOKEVIRTUAL
                        Opcodes.H_INVOKESPECIAL -> Opcodes.INVOKESPECIAL
                        Opcodes.H_INVOKEINTERFACE -> Opcodes.INVOKEINTERFACE
                        else -> {
                            Opcodes.INVOKESTATIC
                        }
                    }
                    methodVisitor.visitMethodInsn(
                        originAccess,
                        originHandle.owner,
                        originHandle.name,
                        originHandle.desc,
                        handle.isInterface
                    )
                } else {
                    val replace = replaceHooker.get().iterator().next()
                    methodVisitor.visitMethodInsn(
                        Opcodes.INVOKESTATIC,
                        replace.targetClass,
                        replace.targetMethod,
                        handle.desc,
                        false
                    )
                }
                // 存储原始函数返回结果
                if (resultObj != -1)
                    storeLocal(resultObj)
            }

            // afterHooker
            run {
                val afterIterator = afterHooker.get().iterator()
                while (afterIterator.hasNext()) {
                    val after = afterIterator.next()
                    for (i in 0 until argLength)
                        methodVisitor.visitVarInsn(Opcodes.ALOAD, preIndex + i)
                    methodVisitor.visitMethodInsn(
                        Opcodes.INVOKESTATIC,
                        after.targetClass,
                        after.targetMethod,
                        handle.desc,
                        false
                    )
                }
            }

            // 如果原始函数返回非NULL，返回原始函数结果
            if (resultObj != -1)
                loadLocal(resultObj, originMethod.returnType)
            methodVisitor.visitInsn(Opcodes.RETURN)
            val label1 = Label()
            methodVisitor.visitLabel(label1)
            if (!isStatic) {
                methodVisitor.visitLocalVariable("this", "L${handle.owner};", null, label0, label1, 0)
            }
            arguments.forEachIndexed { index, arg ->
                methodVisitor.visitLocalVariable("var$index", arg.descriptor, null, label0, label1, localIndex + index)
            }
            methodVisitor.visitMaxs(0, 0)
            methodVisitor.visitEnd()
        }
    }

    data class ChangeState(
        val handle: Handle,
        val originHandle: Handle,
        val config: HookLambdaContainer
    )

    companion object {
        private const val tag = "HookLambdaMethodVisitor"
        private const val codeBaseHandleTail = "CodeWrapImpl"
        fun getTargetMethodName(name: String): String {
            return "$name$$codeBaseHandleTail"
        }
    }
}