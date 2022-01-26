package com.cleo.sample.plugin

import com.cleo.sample.plugin.activity.*
import com.cleo.sample.plugin.hook.lam.LambdaOriginActivity

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
        "startActivityFromFragment" to CFragmentActivity::class.java.name,
        "CommonTest" to CommonTestActivity::class.java.name,
        "HookTest" to HookActivity::class.java.name,
        "LambdaCases" to LambdaOriginActivity::class.java.name
    )

    const val pluginId = "com.cleo.sample.plugin"
    const val hostId = "com.cleo.sample_host"
    var changeApplicationStartActivity = false
}