package com.cleo.sample.plugin

import android.app.Application
import android.content.ComponentName
import android.content.Intent

/**
 * author:gaoguanling
 * date:2021/11/4
 * time:18:52
 * email:gaoguanling@360.cn
 * link:
 */
class PluginApplication : Application() {
    override fun startActivity(intent: Intent?) {
        if (intent == null || intent.component == null)
            return super.startActivity(intent)

        if (Data.changeApplicationStartActivity) {
            val component = intent.component!!
            intent.component = ComponentName("com.cleo.sample.plugin", component.className)
        }

        super.startActivity(intent)
    }
}