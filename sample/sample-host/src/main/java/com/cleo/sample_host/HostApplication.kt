package com.cleo.sample_host

import android.content.Context
import com.qihoo360.replugin.RePlugin
import com.qihoo360.replugin.RePluginApplication
import com.qihoo360.replugin.RePluginConfig

/**
 * author:gaoguanling
 * date:2021/9/22
 * time:17:57
 * email:gaoguanling@360.cn
 * link:
 */
open class HostApplication : RePluginApplication() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        RePlugin.enableDebugger(base, BuildConfig.DEBUG)
    }

    // ----------
    // 自定义行为
    // ----------
    override fun createConfig(): RePluginConfig? {
        val c = RePluginConfig()
        c.isUseHostClassIfNotFound = true
        c.verifySign = !BuildConfig.DEBUG
        c.eventCallbacks = HostEventCallbacks(this)
        return c
    }
}