package com.cleo.sample_plugin.activity

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cleo.sample_plugin.LocalBroadcastTest
import com.cleo.sample_plugin.R

class IdentifyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_identify)
    }

    override fun onResume() {
        super.onResume()
        Log.e("GGL", "OnResume")
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