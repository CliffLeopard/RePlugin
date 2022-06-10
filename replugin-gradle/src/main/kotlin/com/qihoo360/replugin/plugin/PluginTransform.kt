package com.qihoo360.replugin.plugin

import com.android.build.gradle.AppExtension
import com.qihoo360.replugin.hook.HookTransform
import com.qihoo360.replugin.plugin.visitor.*
import com.qihoo360.replugin.transform.bean.InstrumentationContext
import com.qihoo360.replugin.transform.bean.TransformClassInfo
import org.objectweb.asm.ClassVisitor

/**
 * author:CliffLeopard
 * date:5/3/21
 * time:3:48 PM
 * email:precipiceleopard@gmail.com
 * link:
 */
open class PluginTransform(appExtension: AppExtension, override val extension: PluginExtension) :
    HookTransform(appExtension, extension) {

    override fun isIncremental(): Boolean {
        return true
    }

    override fun isSkipClass(classInfo: TransformClassInfo): Boolean {
        return super.isSkipClass(classInfo) || extension.isTargetClass(classInfo.name)
    }

    override fun transformVisitor(visitor: ClassVisitor, context: InstrumentationContext): ClassVisitor {
        var classVisitor = super.transformVisitor(visitor, context)
        classVisitor = ActivityClassVisitor(classVisitor, context)
        classVisitor = LocalBroadcastClassVisitor(classVisitor, context)
        classVisitor = ProviderClassClassVisitor(classVisitor, context)
        classVisitor = IdentifierClassVisitor(classVisitor, context)
        classVisitor = ConstantClassVisitor(classVisitor, context)
        return classVisitor
    }
}