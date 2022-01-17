package com.qihoo360.replugin

/**
 * author:gaoguanling
 * date:2021/7/2
 * time:08:06
 * email:gaoguanling@360.cn
 * link:
 */
object Log {
    private const val showDetail = false
    private const val publish = false
    fun i(tag: String, msg: String) {
        if (!publish)
            println("[$tag] $msg")
    }

    fun detail(tag: String, msg: String) {
        if (!publish && showDetail)
            println("[$tag] $msg")
    }

    fun e(tag: String, msg: String) {
        System.err.println("[$tag] $msg")
    }
}