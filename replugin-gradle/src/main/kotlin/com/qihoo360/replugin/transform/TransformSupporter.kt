package com.qihoo360.replugin.transform

import com.android.build.gradle.AppExtension
import org.gradle.api.Project

/**
 * author:gaoguanling
 * date:2021/9/7
 * time:09:52
 * email:gaoguanling@360.cn
 * link:
 */
interface TransformSupporter {
    fun getTransform(project: Project, android: AppExtension): AbstractTransform?
}