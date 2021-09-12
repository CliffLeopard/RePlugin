package com.qihoo360.replugin.base.annotation

/**
 * author:gaoguanling
 * date:2021/9/12
 * time:12:48
 * email:gaoguanling@360.cn
 * link:
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION,
    AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.EXPRESSION)
@Retention(AnnotationRetention.SOURCE)
annotation class SkipChange()
