package com.cleo.sample.plugin.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi

class ForegroundService : Service() {
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            doSomething()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun doSomething() {
        val channel = NotificationChannel("9000", "通知", NotificationManager.IMPORTANCE_DEFAULT)
        channel.enableVibration(false)
        channel.setSound(null, null)
        val manager: NotificationManager = getSystemService<NotificationManager>(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
        val builder = Notification.Builder(this, "9000")
        val notification = builder.build()
        startForeground(9000, notification)
        stopForeground(true)
        Toast.makeText(this,"成功开启了前台服务",Toast.LENGTH_LONG).show()
        Log.e("ForegroundService","startForeground and stopForeground success")
    }
}