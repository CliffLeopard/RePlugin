package com.qihoo360.replugin

import com.qihoo360.replugin.config.HostExtension
import com.qihoo360.replugin.task.tools.ComponentCreator
import java.io.File
import kotlin.test.Test

/**
 * author:gaoguanling
 * date:2021/7/4
 * time:16:47
 * email:gaoguanling@360.cn
 * link:
 */
class ManifestsTest {
    @Test
    fun testManifestsUpdater() {
        val newManifest: String = ComponentCreator.generateComponent("com.fuck", HostExtension())
        val filePath =
            "/Users/cliffleopard/Desktop/Reconsitution/RePlugin/sample-host/build/intermediates/merged_manifests/debug"
        val file = File(filePath)
        file.walk()
            .filter {
                it.exists()
                        && !it.isDirectory
                        && it.name.equals("AndroidManifest.xml", ignoreCase = true)
            }
            .forEach { manifest: File ->
                val updateContent = manifest
                    .readText()
                    .replace("</application>", "$newManifest</application>")
                file.writeText(updateContent)
            }

    }
}