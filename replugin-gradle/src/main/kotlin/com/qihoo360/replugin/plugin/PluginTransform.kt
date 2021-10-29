package com.qihoo360.replugin.plugin

import com.android.build.gradle.AppExtension
import com.qihoo360.replugin.config.PluginExtension
import com.qihoo360.replugin.transform.AbstractTransform
import com.qihoo360.replugin.transform.ClassReWriter
import com.qihoo360.replugin.transform.bean.TransformClassInfo

/**
 * author:CliffLeopard
 * date:5/3/21
 * time:3:48 PM
 * email:precipiceleopard@gmail.com
 * link:
 */
open class PluginTransform(appExtension: AppExtension, override val extension: PluginExtension) :
    AbstractTransform(appExtension, extension) {

    override fun isIncremental(): Boolean {
        return true
    }

    override fun transformClass(classInfo: TransformClassInfo, inputBytes: ByteArray): ByteArray? {
        extension.excludedClasses.forEach { excludeClass ->
            if (classInfo.fromJar == excludeClass.fromJar
                && Regex(excludeClass.classNameRegex).matches(classInfo.name)
                && classInfo.content.scopes.contains(excludeClass.getScopeByValue())
            ) {
                return ByteArray(0)
            }
        }

        extension.skipClasses.forEach { skipClass ->
            if (classInfo.fromJar == skipClass.fromJar
                && Regex(skipClass.classNameRegex).matches(classInfo.className)
                && classInfo.content.scopes.contains(skipClass.getScopeByValue())
            ) {
                return null
            }
        }
        return ClassReWriter.transform(classInfo, inputBytes, extension)
    }
}