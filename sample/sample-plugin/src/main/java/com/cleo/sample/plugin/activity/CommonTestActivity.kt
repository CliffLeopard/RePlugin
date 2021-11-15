package com.cleo.sample.plugin.activity

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.cleo.sample.plugin.Data
import com.cleo.sample.plugin.R
import com.qihoo360.replugin.RePlugin

class CommonTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common_test)

        val change: Button = findViewById(R.id.button0)
        change.text = "修改ApplicationStartActivity:${Data.changeApplicationStartActivity}"
        change.setOnClickListener {
            Data.changeApplicationStartActivity = !Data.changeApplicationStartActivity
            change.text = "修改ApplicationStartActivity:${Data.changeApplicationStartActivity}"
        }


        // 失败
        findViewById<Button>(R.id.button).setOnClickListener {
            val intent = Intent()
            intent.component = ComponentName("com.cleo.sample.plugin", "com.cleo.sample.plugin.activity.MainActivity")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            this@CommonTestActivity.application.startActivity(intent)
        }


        // 成功
        findViewById<Button>(R.id.button2).setOnClickListener {
            val intent = Intent()
            intent.component = ComponentName("com.cleo.sample.plugin", "com.cleo.sample.plugin.activity.MainActivity")
            this@CommonTestActivity.startActivity(intent)
        }


        // 成功
        findViewById<Button>(R.id.button3).setOnClickListener {
            val intent = Intent()
            intent.component = ComponentName("com.cleo.sample.plugin", "com.cleo.sample.plugin.activity.MainActivity")
            RePlugin.startActivity(this@CommonTestActivity, intent)
        }


        // Replugin类使用Application 失败
        findViewById<Button>(R.id.button4).setOnClickListener {
            val intent = Intent()
            intent.component = ComponentName("com.cleo.sample.plugin", "com.cleo.sample.plugin.activity.MainActivity")
            RePlugin.startActivity(this@CommonTestActivity.application, intent)
        }


        // Replugin类使用Application 成功
        findViewById<Button>(R.id.button5).setOnClickListener {
            val intent = Intent()
            intent.component = ComponentName("com.cleo.sample.plugin", "com.cleo.sample.plugin.activity.MainActivity")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            RePlugin.startActivity(this@CommonTestActivity.application, intent)
        }

    }
}