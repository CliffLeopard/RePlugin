package com.qihoo360.replugin.plugin

import com.android.build.api.transform.QualifiedContent
import com.android.build.gradle.AppExtension
import com.qihoo360.replugin.config.BaseExtension
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
                && classInfo.content.scopes.contains(getScopeByValue(excludeClass.scope))
            ) {
                return ByteArray(0)
            }
        }

        extension.skipClasses.forEach { skipClass ->
            if (classInfo.fromJar == skipClass.fromJar
                && Regex(skipClass.classNameRegex).matches(classInfo.className)
                && classInfo.content.scopes.contains(getScopeByValue(skipClass.scope))
            ) {
                return null
            }
        }
        return ClassReWriter.transform(classInfo, inputBytes, extension)
    }

    private fun getScopeByValue(value:Int): QualifiedContent.Scope{
        return when(value){
            0x01->
                QualifiedContent.Scope.PROJECT
            0x04->
                QualifiedContent.Scope.SUB_PROJECTS
            0x10->
                QualifiedContent.Scope.EXTERNAL_LIBRARIES
            0x20->
                QualifiedContent.Scope.TESTED_CODE
            0x40->
                QualifiedContent.Scope.PROVIDED_ONLY
            0x02->
                QualifiedContent.Scope.SUB_PROJECTS_LOCAL_DEPS
            0x08->
                QualifiedContent.Scope.PROJECT_LOCAL_DEPS
            else -> QualifiedContent.Scope.PROJECT
        }
    }
}