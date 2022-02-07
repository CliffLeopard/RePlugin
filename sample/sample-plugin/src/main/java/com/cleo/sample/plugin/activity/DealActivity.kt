package com.cleo.sample.plugin.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cleo.sample.plugin.R

class DealActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deal)
        setResult(-1)
        Toast.makeText(this, "Plugin:SetResult:-1", Toast.LENGTH_SHORT).show()
        finish()
    }
}