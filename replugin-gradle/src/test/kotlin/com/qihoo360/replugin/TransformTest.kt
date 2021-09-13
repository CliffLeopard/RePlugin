package com.qihoo360.replugin

import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue
import org.gradle.testkit.runner.GradleRunner

/**
 * author:gaoguanling
 * date:2021/8/3
 * time:16:17
 * email:gaoguanling@360.cn
 * link:
 */
class TransformTest {
    @Test
    fun testFunctional() {
        val projectDir = File("../sample/sample-plugin")
        // Run the build
        val runner = GradleRunner.create()
        runner.forwardOutput()
        runner.withPluginClasspath()
        runner.withArguments("assembleDebug")
        runner.withProjectDir(projectDir)
        val result = runner.build()
        println("Hello Fucking World")
        assertTrue(true)
    }
}