package com.qihoo360.replugin.transform.bean

/**
 * author:gaoguanling
 * date:2021/9/8
 * time:13:17
 * email:gaoguanling@360.cn
 * link:
 */
data class MethodBean(
    val opcode: Int,
    val owner: String?,
    val name: String?,
    val descriptor: String?,
    val isInterface: Boolean
)
