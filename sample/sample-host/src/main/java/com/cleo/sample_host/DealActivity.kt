package com.cleo.sample_host

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DealActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deal)
        setResult(-1)
        Toast.makeText(this, "Host:SetResult:-1", Toast.LENGTH_SHORT).show()
        finish()
    }
}