package com.qihoo360.replugin

import com.qihoo360.replugin.host.HostExtension
import com.qihoo360.replugin.task.tools.PluginInfoParser
import org.junit.Test
import java.io.File

/**
 * author:gaoguanling
 * date:2021/8/2
 * time:14:25
 * email:gaoguanling@360.cn
 * link:
 */
class BuiltInJsonTest {
    @Test
    fun builtInJson() {
        val filePath =
            "/Users/cliffleopard/Desktop/Reconsitution/RePlugin/sample/sample-host/build/intermediates/merged_assets/debug/out/plugins/sample-plugin.jar"
        val jarFile = File(filePath)
        val parser = PluginInfoParser(jarFile, HostExtension())
        val pluginInfo = parser.getPluginInfo()
        print(pluginInfo.pkg)
    }

    @Test
    fun testSequence() {
        val list = mutableListOf("four", "three", "two", "one")
        list.asSequence()
            .map { str: String ->
                str.elementAt(0).toUpperCase()
            }
            .chunked(3)
            .forEach { sublist ->
                print("Hello:")
                sublist.forEach { print("  $it") }
                println()
            }
    }
}