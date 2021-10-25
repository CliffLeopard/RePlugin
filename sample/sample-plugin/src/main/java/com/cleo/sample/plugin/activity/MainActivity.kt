package com.cleo.sample.plugin.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cleo.sample.plugin.Data
import com.cleo.sample.plugin.R
import com.cleo.sample.plugin.adapter.RecyclerViewAdapter


class MainActivity : AppCompatActivity() {
    private lateinit var list: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        list = findViewById(R.id.case_list)
        list.adapter = RecyclerViewAdapter(Data.cases, this)
        list.layoutManager = LinearLayoutManager(this)
        list.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }
}

