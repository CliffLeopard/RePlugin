package com.cleo.sample_host

/**
 * author:gaoguanling
 * date:2021/9/22
 * time:15:58
 * email:gaoguanling@360.cn
 * link:
 */
object Data {
    val cases = listOf(
        "PluginMainActivity" to "com.cleo.sample.plugin.activity.MainActivity",
        "ShowActivityInfo" to "com.cleo.sample_host.InfoActivity",
        "ServiceTest" to "com.cleo.sample_host.ServiceTestActivity"
    )

    const val pluginId = "com.cleo.sample.plugin"
    const val hostId = "com.cleo.sample_host"
}