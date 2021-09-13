package com.qihoo360.replugin.transform.visitor

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes.ASM9


/**
 * author:gaoguanling
 * date:2021/9/8
 * time:10:01
 * email:gaoguanling@360.cn
 * link:
 */
open class PluginClassVisitor(cv: ClassVisitor?, val context: InstrumentationContext) :
    ClassVisitor(ASM9, cv) {
    constructor(context: InstrumentationContext) : this(null, context)

    protected var className: String? = null
}