package com.qihoo360.replugin.transform.visitor

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor

/**
 * author:gaoguanling
 * date:2021/9/9
 * time:14:06
 * email:gaoguanling@360.cn
 * link:
 */
class ProviderClassClassVisitor(cv: ClassVisitor?,context: InstrumentationContext) : PluginClassVisitor(cv,context) {
    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        this.className = name
        super.visit(version, access, name, signature, superName, interfaces)
    }

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        return ProviderMethodVisitor(
            context,
            className,
            super.visitMethod(
                access,
                name,
                descriptor,
                signature,
                exceptions
            )
        )
    }
}