package com.qihoo360.replugin.plugin

import org.gradle.testfixtures.ProjectBuilder
import kotlin.test.Test
import kotlin.test.assertNotNull

/**
 * author:gaoguanling
 * date:2021/7/4
 * time:16:45
 * email:gaoguanling@360.cn
 * link:
 */
class HostPluginTest {
    @Test
    fun `plugin registers task`() {

        val project = ProjectBuilder.builder().build()
        project.plugins.apply("replugin-host-gradle")

        assertNotNull(project.tasks.findByName("replugin-host-gradle"))
    }
}