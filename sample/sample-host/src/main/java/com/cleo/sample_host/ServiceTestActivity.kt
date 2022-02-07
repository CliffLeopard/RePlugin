package com.cleo.sample_host

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.qihoo360.replugin.RePlugin
import com.qihoo360.replugin.component.service.PluginServiceClient

class ServiceTestActivity : AppCompatActivity() {
    private lateinit var btnFore: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_test)
        btnFore = findViewById(R.id.btn_fore)
        initAction()
    }

    private fun initAction() {
        btnFore.setOnClickListener {
            val intent: Intent = RePlugin.createIntent("sample-plugin", "com.cleo.sample.plugin.service.ForegroundService")
            PluginServiceClient.startService(this@ServiceTestActivity, intent)
        }
    }
}