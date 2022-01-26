package com.qihoo360.replugin.transform.visitor

import com.qihoo360.replugin.config.HookLambdaContainer
import com.qihoo360.replugin.transform.bean.InstrumentationContext
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor

/**
 * author:gaoguanling
 * date:2022/1/25
 * time:16:40
 * email:gaoguanling@360.cn
 * link:
 */
class HookLambdaClassVisitor(cv: ClassVisitor, context: InstrumentationContext, private val hookLambdaConfig: HookLambdaContainer) :
    PluginClassVisitor(cv, context) {

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        val originMv = super.visitMethod(access, name, descriptor, signature, exceptions)
        if (name.isNullOrEmpty() || descriptor.isNullOrEmpty())
            return originMv
        val config = hookLambdaConfig.findByMethodNameAndDesc(name, descriptor)
        return if (config.isEmpty())
            originMv
        else
            HookLambdaMethodVisitor(this, context, originMv, access, name, descriptor, config)
    }

}