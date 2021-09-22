package com.cleo.sample_plugin

import com.cleo.sample_plugin.activity.CFragmentActivity
import com.cleo.sample_plugin.activity.IdentifyActivity
import com.cleo.sample_plugin.activity.ProviderActivity

/**
 * author:gaoguanling
 * date:2021/9/22
 * time:15:58
 * email:gaoguanling@360.cn
 * link:
 */
object Data {
    val cases = listOf(
        "resources.getIdentifier" to IdentifyActivity::class.java.name,
        "Provider" to ProviderActivity::class.java.name,
        "startActivityFromFragment" to CFragmentActivity::class.java.name
    )

    const val pluginId = "com.cleo.sample_plugin"
    const val hostId = "com.cleo.sample_host"
}