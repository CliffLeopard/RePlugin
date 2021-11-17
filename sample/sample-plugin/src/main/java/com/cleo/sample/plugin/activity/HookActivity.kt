package com.cleo.sample.plugin.activity

import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.cleo.sample.plugin.R
import com.cleo.sample.plugin.hook.Scene

class HookActivity : AppCompatActivity() {
    private lateinit var container: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hook)
        container = findViewById(R.id.container)
        hookCallBeforeCases()
        hookCallAfterCases()
        hookCallReplaceCases()
    }

    private fun hookCallBeforeCases() {
        addButton("Hook-Call-Before").setOnClickListener {
            Scene.callScene()
        }

        addButton("Hook-Call-Before-Void").setOnClickListener {
            Scene.callVoidScene()
        }

        addButton("Hook-Call-Static-Before").setOnClickListener {
            Scene.callStaticScene()
        }

        addButton("Hook-Call-Static-Before-Void").setOnClickListener {
            Scene.callStaticVoidScene()
        }
    }

    private fun hookCallAfterCases() {
        addButton("Hook-Call-After").setOnClickListener {

        }

        addButton("Hook-Call-After-Void").setOnClickListener {

        }

        addButton("Hook-Call-Static-After").setOnClickListener {

        }

        addButton("Hook-Call-Static-After-Void").setOnClickListener {

        }
    }

    private fun hookCallReplaceCases() {
        addButton("Hook-Call-Replace").setOnClickListener {

        }

        addButton("Hook-Call-Replace-Void").setOnClickListener {

        }

        addButton("Hook-Call-Static-Replace").setOnClickListener {

        }

        addButton("Hook-Call-Static-Replace-Void").setOnClickListener {

        }
    }

    private fun addButton(text: String): Button {
        val button = Button(this)
        button.text = text
        val lt = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        button.layoutParams = lt
        container.addView(button, lt)
        return button
    }
}