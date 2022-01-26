package com.cleo.sample.plugin.hook.lam

import android.util.Log
import android.view.View

/**
 * author:gaoguanling
 * date:2022/1/26
 * time:11:22
 * email:gaoguanling@360.cn
 * link:
 */
object LambdaTarget {
    const val tag = "LambdaTarget"

    @JvmStatic
    fun targetBefore(view: View) {
        Log.e(tag, "targetBefore")
    }

    @JvmStatic
    fun targetAfter(view: View) {
        Log.e(tag, "targetAfter")
    }

    @JvmStatic
    fun targetReplace(view: View) {
        Log.e(tag, "targetReplace")
    }

}