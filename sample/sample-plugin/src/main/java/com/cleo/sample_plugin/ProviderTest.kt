package com.cleo.sample_plugin

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import com.qihoo360.replugin.loader.p.PluginProviderClient

/**
 * author:gaoguanling
 * date:2021/9/9
 * time:13:32
 * email:gaoguanling@360.cn
 * link:
 */
object ProviderTest {
    fun testProvider(context: Context, uri: Uri, values: ContentValues) {
        context.contentResolver.insert(uri, values)
    }
}