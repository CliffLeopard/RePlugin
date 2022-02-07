package com.qihoo360.replugin.transform.visitor

import com.qihoo360.replugin.transform.bean.InstrumentationContext
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Opcodes.ASM9

/**
 * author:gaoguanling
 * date:2021/9/9
 * time:13:36
 * email:gaoguanling@360.cn
 * link:
 */
class ProviderMethodVisitor(
    private val context: InstrumentationContext,
    val className: String?,
    mv: MethodVisitor?
) : MethodVisitor(ASM9, mv) {
    override fun visitMethodInsn(
        opcode: Int,
        owner: String?,
        name: String?,
        descriptor: String?,
        isInterface: Boolean
    ) {
        if (!notChange(owner) && owner == originClass && replacedMethods.contains(name) && !descriptor.isNullOrEmpty()) {
            context.classModified = true
            return super.visitMethodInsn(
                Opcodes.INVOKESTATIC, targetClass, name,
                "(Landroid/content/ContentResolver;" + descriptor.removePrefix("("),
                isInterface
            )
        } else {
            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface)
        }
    }

    private fun notChange(owner: String?): Boolean {
        return owner == targetClass || className == originClass
    }

    companion object {
        const val originClass = "android/content/ContentResolver"
        const val targetClass = "com/qihoo360/replugin/loader/p/PluginProviderClient"
        val replacedMethods = setOf(
            "query",
            "getType",
            "insert",
            "bulkInsert",
            "delete",
            "update",
            "openInputStream",
            "openOutputStream",
            "openFileDescriptor",
            "registerContentObserver",
            "acquireContentProviderClient",
            "notifyChange",
            "toCalledUri",
        )
    }
}