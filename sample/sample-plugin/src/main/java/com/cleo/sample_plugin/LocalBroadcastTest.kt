package com.cleo.sample_plugin

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.localbroadcastmanager.content.LocalBroadcastManager

/**
 * author:gaoguanling
 * date:2021/9/8
 * time:11:12
 * email:gaoguanling@360.cn
 * link:
 */
class LocalBroadcastTest {
    private var manager: LocalBroadcastManager? = null
    fun sendBroadCast(context: Context) {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {

            }
        }
        val filter = IntentFilter("")
        val intent = Intent("")
        val mManager = getManager(context)
        mManager.registerReceiver(receiver, filter)
        mManager.unregisterReceiver(receiver)
        mManager.sendBroadcast(intent)
        mManager.sendBroadcastSync(intent)

        val bManager = change(mManager)
        System.out.println(bManager.toString())
    }

    private fun getManager(context: Context): LocalBroadcastManager {
        manager = LocalBroadcastManager.getInstance(context)
        return manager!!
    }

    private fun change(paramManager: LocalBroadcastManager): LocalBroadcastManager {
        val innerManager = paramManager
        System.out.println(innerManager.toString())
        return innerManager
    }

}