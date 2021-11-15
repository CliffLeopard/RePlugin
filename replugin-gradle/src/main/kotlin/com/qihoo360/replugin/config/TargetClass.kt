package com.qihoo360.replugin.config

import com.android.build.api.transform.QualifiedContent

/**
 * author:gaoguanling
 * date:2021/10/25
 * time:20:13
 * email:gaoguanling@360.cn
 * link:
 */
open class TargetClass(var name: String) {
    var classNameRegex = mutableListOf<String>()
    var fromJar: Boolean = true
    var scope: Int = 0x04  // 16进制数值

    fun getScopeByValue(): QualifiedContent.Scope {
        return when (scope) {
            0x01 ->
                QualifiedContent.Scope.PROJECT
            0x04 ->
                QualifiedContent.Scope.SUB_PROJECTS
            0x10 ->
                QualifiedContent.Scope.EXTERNAL_LIBRARIES
            0x20 ->
                QualifiedContent.Scope.TESTED_CODE
            0x40 ->
                QualifiedContent.Scope.PROVIDED_ONLY
            0x02 ->
                QualifiedContent.Scope.SUB_PROJECTS_LOCAL_DEPS
            0x08 ->
                QualifiedContent.Scope.PROJECT_LOCAL_DEPS
            else -> QualifiedContent.Scope.PROJECT
        }
    }
}