package com.cleo.sample_host

import android.content.ComponentName
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.qihoo360.i.Factory
import com.qihoo360.replugin.RePlugin

class InfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)
    }

    fun clickHostInfo(view: View) {
        val componentName = ComponentName(applicationContext, InfoActivity::class.java)
        var flags = 0
        if (Build.VERSION.SDK_INT >= 29) {
            flags = (PackageManager.MATCH_DIRECT_BOOT_AUTO
                    or PackageManager.MATCH_DIRECT_BOOT_AWARE
                    or PackageManager.MATCH_DIRECT_BOOT_UNAWARE)
        } else if (Build.VERSION.SDK_INT >= 24) {
            flags = (PackageManager.MATCH_DIRECT_BOOT_AWARE
                    or PackageManager.MATCH_DIRECT_BOOT_UNAWARE)
        }
        val aInfo = applicationContext.packageManager.getActivityInfo(componentName, flags)
        Log.e("HostInfo", aInfo.toString())
    }

    fun clickPluginInfo(view: View) {
        val activity = "com.cleo.sample.plugin.activity.ShowInfoActivity"
        val intent = RePlugin.createIntent(Data.pluginId, activity)
        val activityInfo = Factory.getActivityInfo(Data.pluginId, activity, intent)
        Log.e("PluginInfo", activityInfo.toString())

    }
}