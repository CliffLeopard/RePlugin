package com.cleo.sample_plugin.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.cleo.sample_plugin.R
import com.cleo.sample_plugin.ui.main.CFragment

class CFragmentActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, CFragment.newInstance())
                .commitNow()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Toast.makeText(
            this,
            "CFragmentActivity.onActivityResult rqCode:$requestCode  rtCode:$resultCode",
            Toast.LENGTH_SHORT
        ).show()
    }
}