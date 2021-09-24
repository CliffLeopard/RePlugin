package com.cleo.sample_plugin.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.cleo.sample_plugin.R

class BroadcastActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_broadcast)
    }

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