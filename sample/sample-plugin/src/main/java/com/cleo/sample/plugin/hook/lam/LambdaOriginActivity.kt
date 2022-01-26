package com.cleo.sample.plugin.hook.lam

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.cleo.sample.plugin.R

class LambdaOriginActivity : AppCompatActivity() {
    private lateinit var before: Button
    private lateinit var after: Button
    private lateinit var replace: Button
    private lateinit var both: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lambda_origin)
        before = findViewById(R.id.before)
        after = findViewById(R.id.after)
        replace = findViewById(R.id.replace)
        both = findViewById(R.id.both)
        action1()
        action2()
        action3()
        action4()
    }

    fun action1() {
        before.setOnClickListener {
            Log.e(tag, "press action1")
        }
    }

    private fun action2() {
        after.setOnClickListener {
            Log.e(tag, "press action2")
        }
    }

    fun action3() {
        replace.setOnClickListener {
            Log.e(tag, "press action3")
        }
    }

    fun action4() {
        both.setOnClickListener {
            Log.e(tag, "press action4")
        }
    }

    companion object {
        const val tag = "LambdaOriginActivity"
    }
}