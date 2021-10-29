package com.cleo.sample.plugin.activity

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cleo.sample.plugin.util.SimplePluginUtil
import com.cleo.sample.plugin.LocalBroadcastTest
import com.cleo.sample.plugin.R

class IdentifyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_identify)
        SimplePluginUtil.println()
    }

    override fun onResume() {
        super.onResume()
        val bc = LocalBroadcastTest()
        bc.sendBroadCast(this)
        getId()
        val kk = resources.getIdentifier("hello", "id", applicationContext.packageName)
        val textView = findViewById<TextView>(kk)
        textView.setText("Changed")
    }

    private fun getId() {
        val id = resources.getIdentifier("hello", "id", packageName)
        Toast.makeText(this, "id:$id", Toast.LENGTH_SHORT).show()
    }
}