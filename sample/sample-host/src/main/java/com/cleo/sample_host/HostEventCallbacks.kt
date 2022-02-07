package com.cleo.sample_host

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.qihoo360.replugin.RePluginEventCallbacks
import com.qihoo360.replugin.model.PluginInfo

/**
 * author:gaoguanling
 * date:2021/9/22
 * time:18:01
 * email:gaoguanling@360.cn
 * link:
 */
class HostEventCallbacks(context: Context) : RePluginEventCallbacks(context) {
    private val TAG = "HostEventCallbacks"

    override fun onInstallPluginFailed(path: String, code: InstallResult) {
        // 大部分可以通过RePlugin.install的返回值来判断是否成功
        Log.e(TAG, "onInstallPluginFailed: Failed! path=$path; r=$code")
        super.onInstallPluginFailed(path, code)
    }

    override fun onInstallPluginSucceed(info: PluginInfo) {
        Log.e(
            TAG,
            "onInstallPluginSucceed: path=" + info.name + "; packageName=" + info.packageName
        )
        super.onInstallPluginSucceed(info)
    }

    override fun onPrepareAllocPitActivity(intent: Intent) {
        Log.e(TAG, "onPrepareAllocPitActivity:$intent")
        super.onPrepareAllocPitActivity(intent)
    }

    override fun onPrepareStartPitActivity(
        context: Context?,
        intent: Intent,
        pittedIntent: Intent
    ) {
        Log.e(TAG, "onPrepareStartPitActivity: intent:$intent  pittedIntent:$pittedIntent")
        super.onPrepareStartPitActivity(context, intent, pittedIntent)
    }

    override fun onBinderReleased() {
        super.onBinderReleased()
    }

    override fun onStartActivityCompleted(plugin: String?, activity: String, result: Boolean) {
        // FIXME 当打开Activity成功时触发此逻辑，可在这里做一些APM、打点统计等相关工作
        Log.e(TAG, "onStartActivityCompleted:$activity")
        super.onStartActivityCompleted(plugin, activity, result)
    }

    override fun onActivityDestroyed(activity: Activity) {
        Log.e(
            TAG,
            activity.javaClass.simpleName
        )
        super.onActivityDestroyed(activity)
    }
}