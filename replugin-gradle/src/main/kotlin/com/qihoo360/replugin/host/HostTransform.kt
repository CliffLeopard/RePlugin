package com.qihoo360.replugin.host

import com.android.build.gradle.AppExtension
import com.qihoo360.replugin.config.BaseExtension
import com.qihoo360.replugin.transform.AbstractTransform
import com.qihoo360.replugin.transform.bean.TransformClassInfo

/**
 * author:CliffLeopard
 * date:5/3/21
 * time:3:45 PM
 * email:precipiceleopard@gmail.com
 * link:
 * 此类目前没有用到，因为Host不需要transform操作;有新需求需要操作时再继续写
 */
open class HostTransform(appExtension: AppExtension, extension: BaseExtension) :
    AbstractTransform(appExtension, extension) {

    override fun isIncremental(): Boolean {
        return true
    }

    override fun transformClass(classInfo: TransformClassInfo, inputBytes: ByteArray): ByteArray? {
        return null
    }
}