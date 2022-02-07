package com.qihoo360.replugin.hook

import com.android.build.gradle.AppExtension
import com.qihoo360.replugin.transform.AbstractTransform
import com.qihoo360.replugin.transform.bean.InstrumentationContext
import com.qihoo360.replugin.transform.bean.TransformClassInfo
import com.qihoo360.replugin.transform.visitor.HookCallMethodClassVisitor
import com.qihoo360.replugin.transform.visitor.HookDefineMethodClassVisitor
import com.qihoo360.replugin.transform.visitor.HookLambdaClassVisitor
import org.objectweb.asm.ClassVisitor

/**
 * author:gaoguanling
 * date:2022/1/27
 * time:17:59
 * email:gaoguanling@360.cn
 * link:
 */
open class HookTransform(appExtension: AppExtension, override val extension: HookExtension) : AbstractTransform(appExtension, extension) {
    override fun isIncremental(): Boolean = true
    override fun isExcludeClass(classInfo: TransformClassInfo): Boolean {
        extension.excludedClasses?.forEach { excludeClass ->
            if (classInfo.fromJar == excludeClass.fromJar
                && classInfo.content.scopes.contains(excludeClass.getScopeByValue())
            ) {
                excludeClass.classNameRegex.forEach { regex ->
                    if (Regex(regex).matches(classInfo.name)) {
                        return true
                    }
                }
            }
        }
        return false
    }

    override fun isSkipClass(classInfo: TransformClassInfo): Boolean {
        extension.skipClasses?.forEach { skipClass ->
            if (classInfo.fromJar == skipClass.fromJar
                && classInfo.content.scopes.contains(skipClass.getScopeByValue())
            ) {
                skipClass.classNameRegex.forEach { regex ->
                    if (Regex(regex).matches(classInfo.name)) {
                        return true
                    }
                }
            }
        }
        return false
    }

    override fun transformVisitor(visitor: ClassVisitor, context: InstrumentationContext): ClassVisitor {
        var classVisitor: ClassVisitor = visitor
        val hookConfig = HookMethodContainer.getInstance(extension)
        if (!hookConfig.isEmpty())
            classVisitor = HookDefineMethodClassVisitor(HookCallMethodClassVisitor(classVisitor, context, hookConfig), context, hookConfig)

        val lambdaConfig = HookLambdaContainer.getInstance(extension).findByClassName(context.classInfo.name)
        if (!lambdaConfig.isEmpty()) {
            classVisitor = HookLambdaClassVisitor(classVisitor, context, lambdaConfig)
        }
        return classVisitor
    }

}