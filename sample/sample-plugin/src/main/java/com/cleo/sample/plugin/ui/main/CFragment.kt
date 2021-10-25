package com.cleo.sample.plugin.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.cleo.sample.plugin.Data
import com.cleo.sample.plugin.R
import com.cleo.sample.plugin.activity.DealActivity

class CFragment : Fragment() {

    companion object {
        fun newInstance() = CFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.main_fragment, container, false)
        view.findViewById<Button>(R.id.btn_start_fragment).setOnClickListener {
            val intent = Intent()
            intent.setClassName(Data.pluginId, DealActivity::class.java.name)
            startActivityForResult(intent, 10)

        }

        view.findViewById<Button>(R.id.btn_start_activity).setOnClickListener {
            val intent = Intent()
            intent.setClassName(Data.pluginId, DealActivity::class.java.name)
            activity?.startActivityForResult(intent, 11, null)
        }

        view.findViewById<Button>(R.id.btn_start_host_fragment).setOnClickListener {
            val intent = Intent()
            intent.setClassName(Data.hostId, "com.cleo.sample_host.DealActivity")
            startActivityForResult(intent, 10)

        }

        view.findViewById<Button>(R.id.btn_start_host_activity).setOnClickListener {
            val intent = Intent()
            intent.setClassName(Data.hostId, "com.cleo.sample_host.DealActivity")
            activity?.startActivityForResult(intent, 11, null)
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Toast.makeText(
            context,
            "CFragment.onActivityResult rqCode:$requestCode  rtCode:$resultCode",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }


}