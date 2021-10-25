package com.cleo.sample.plugin.activity

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cleo.sample.plugin.R
import com.qihoo360.replugin.RePlugin

class ProviderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_provider)
    }

    fun testProvider(context: Context, uri: Uri, values: ContentValues) {
        context.contentResolver.insert(uri, values)
    }

    fun getPlugin() {
        RePlugin.getPluginContext()
    }
}