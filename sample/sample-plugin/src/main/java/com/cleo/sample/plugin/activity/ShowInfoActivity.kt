package com.cleo.sample.plugin.activity

import android.content.ComponentName
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.cleo.sample.plugin.R
import com.qihoo360.replugin.RePlugin

class ShowInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_info)
        val applicationContext =
            if (RePlugin.isHostInitialized())
                RePlugin.getHostContext()
            else
                this.applicationContext

        val componentName = ComponentName(applicationContext, ShowInfoActivity::class.java)
        var flags = 0
        if (Build.VERSION.SDK_INT >= 29) {
            flags = (PackageManager.MATCH_DIRECT_BOOT_AUTO
                    or PackageManager.MATCH_DIRECT_BOOT_AWARE
                    or PackageManager.MATCH_DIRECT_BOOT_UNAWARE)
        } else if (Build.VERSION.SDK_INT >= 24) {
            flags = (PackageManager.MATCH_DIRECT_BOOT_AWARE
                    or PackageManager.MATCH_DIRECT_BOOT_UNAWARE)
        }

        RePlugin.getPluginContext()

        val activityInfo = this.applicationContext.packageManager.getActivityInfo(componentName, flags)
        Log.e("ShowInfoActivity", activityInfo.toString())
    }
}