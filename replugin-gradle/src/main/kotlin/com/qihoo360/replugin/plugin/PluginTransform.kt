package com.qihoo360.replugin.plugin

import com.android.build.gradle.AppExtension
import com.qihoo360.replugin.config.BaseExtension
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
open class PluginTransform(appExtension: AppExtension, extension: BaseExtension) :
    AbstractTransform(appExtension, extension) {

    override fun isIncremental(): Boolean {
        return true
    }

    override fun transformClass(classInfo: TransformClassInfo, inputBytes: ByteArray): ByteArray? {
        return ClassReWriter.transform(classInfo, inputBytes, extension)
    }
}