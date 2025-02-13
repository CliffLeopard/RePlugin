package com.cleo.sample.plugin.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cleo.sample.plugin.Data
import com.cleo.sample.plugin.R

/**
 * author:gaoguanling
 * date:2021/9/22
 * time:16:07
 * email:gaoguanling@360.cn
 * link:
 */
class RecyclerViewAdapter(
    private val cases: List<Pair<String, String>>,
    private val context: Context
) :
    RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewViewHolder>() {

    class RecyclerViewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.main_item, parent, false)
        return RecyclerViewViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewViewHolder, position: Int) {
        val case = cases[position]
        holder.textView.text = case.first
        holder.textView.setOnClickListener {
            val intent = Intent()
            intent.setClassName(Data.pluginId, case.second)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return cases.size
    }
}