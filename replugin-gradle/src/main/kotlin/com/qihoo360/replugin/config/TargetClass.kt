package com.qihoo360.replugin.config

/**
 * author:gaoguanling
 * date:2021/10/25
 * time:20:13
 * email:gaoguanling@360.cn
 * link:
 */
open class TargetClass(var name: String) {
    var classNameRegex: String = ""
    var fromJar: Boolean = true
    var scope: Int = 0x04  // 16进制数值
}