package com.cleo.sample.plugin.activity

import android.os.Bundle
import android.view.Gravity
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
        addTittle("调用时Hook:Before")
        hookCallBeforeCases()
        addTittle("调用时Hook:After")
        hookCallAfterCases()
        addTittle("调用时Hook:Replace")
        hookCallReplaceCases()
        addTittle("定义时Hook:Before")
        hookDefineBeforeCases()
        addTittle("定义时Hook:After")
        hookDefineAfterCases()
        addTittle("定义时Hook:Replace")
        hookDefineReplaceCases()
    }

    private fun hookCallBeforeCases() {
        addButton("Hook-Call-Before").setOnClickListener {
            Scene.callBefore()
        }

        addButton("Hook-Call-Before-Void").setOnClickListener {
            Scene.callBeforeVoid()
        }

        addButton("Hook-Call-Static-Before").setOnClickListener {
            Scene.callBeforeStatic()
        }

        addButton("Hook-Call-Static-Before-Void").setOnClickListener {
            Scene.callBeforeStaticVoid()
        }
    }

    private fun hookCallAfterCases() {
        addButton("Hook-Call-After").setOnClickListener {
            Scene.callAfter()
        }

        addButton("Hook-Call-After-Void").setOnClickListener {
            Scene.callAfterVoid()
        }

        addButton("Hook-Call-Static-After").setOnClickListener {
            Scene.callAfterStatic()
        }

        addButton("Hook-Call-Static-After-Void").setOnClickListener {
            Scene.callAfterStaticVoid()
        }
    }

    private fun hookCallReplaceCases() {
        addButton("Hook-Call-Replace").setOnClickListener {
            Scene.callReplace()
        }

        addButton("Hook-Call-Replace-Void").setOnClickListener {
            Scene.callReplaceVoid()
        }

        addButton("Hook-Call-Static-Replace").setOnClickListener {
            Scene.callReplaceStatic()
        }

        addButton("Hook-Call-Static-Replace-Void").setOnClickListener {
            Scene.callReplaceStaticVoid()
        }
    }

    private fun hookDefineBeforeCases() {
        addButton("Hook-Define-Before").setOnClickListener {
            Scene.defineBefore()
        }

        addButton("Hook-Define-Before-Void").setOnClickListener {
            Scene.defineBeforeVoid()
        }

        addButton("Hook-Define-Static-Before").setOnClickListener {
            Scene.defineBeforeStatic()
        }

        addButton("Hook-Define-Static-Before-Void").setOnClickListener {
            Scene.defineBeforeStaticVoid()
        }
    }

    private fun hookDefineAfterCases() {
        addButton("Hook-Define-After").setOnClickListener {
            Scene.defineAfter()
        }

        addButton("Hook-Define-After-Void").setOnClickListener {
            Scene.defineAfterVoid()
        }

        addButton("Hook-Define-Static-After").setOnClickListener {
            Scene.defineAfterStatic()
        }

        addButton("Hook-Define-Static-After-Void").setOnClickListener {
            Scene.defineAfterStaticVoid()
        }
    }

    private fun hookDefineReplaceCases() {
        addButton("Hook-Define-Replace").setOnClickListener {
            Scene.defineReplace()
        }

        addButton("Hook-Define-Replace-Void").setOnClickListener {
            Scene.defineReplaceVoid()
        }

        addButton("Hook-Define-Static-Replace").setOnClickListener {
            Scene.defineReplaceStatic()
        }

        addButton("Hook-Define-Static-Replace-Void").setOnClickListener {
            Scene.defineReplaceStaticVoid()
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

    private fun addTittle(text: String): Button {
        val button = Button(this)
        button.text = text
        button.gravity = Gravity.START
        button.textSize = 16f

        val lt = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        button.layoutParams = lt
        container.addView(button, lt)
        return button
    }
}