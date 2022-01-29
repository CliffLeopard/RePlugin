package com.qihoo360.replugin.hook

import com.android.build.gradle.AppExtension
import com.qihoo360.replugin.Constants
import com.qihoo360.replugin.transform.AbstractTransform
import org.gradle.api.Project

/**
 * author:gaoguanling
 * date:2022/1/27
 * time:17:52
 * email:gaoguanling@360.cn
 * link:
 */
open class HookPlugin : HookAbstractPlugin<HookExtension>() {

    override fun createExtension(project: Project) {
        project.extensions.create(Constants.HOOK_CONFIG, HookExtension::class.java)
    }

    override fun initExtension(project: Project, android: AppExtension) {
        extension = project.extensions.getByName(Constants.HOOK_CONFIG) as HookExtension
        if (extension == null)
            throw Exception("请在build.gradle 文件中配置 hookConfig!!")
    }

    override fun registerProjectTask(project: Project, android: AppExtension, extension: HookExtension) {

    }

    override fun getTransform(project: Project, android: AppExtension): AbstractTransform? {
        val hookExtension = extension as HookExtension
        return if (HookMethodContainer.getInstance(hookExtension).isEmpty()
            && HookLambdaContainer.getInstance(hookExtension).isEmpty()
        ) {
            null
        } else
            HookTransform(android, extension!!)
    }

}