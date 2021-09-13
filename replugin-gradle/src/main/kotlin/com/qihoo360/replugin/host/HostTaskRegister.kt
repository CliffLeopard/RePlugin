package com.qihoo360.replugin.host

import com.android.build.gradle.AppExtension
import com.android.build.gradle.internal.api.ApplicationVariantImpl
import com.qihoo360.replugin.Constants
import com.qihoo360.replugin.Log
import com.qihoo360.replugin.config.HostExtension
import com.qihoo360.replugin.task.TaskRegister
import com.qihoo360.replugin.task.tools.ComponentCreator
import com.qihoo360.replugin.task.tools.FileCreators
import org.gradle.api.Project
import java.io.File
import java.util.*

/**
 * author:gaoguanling
 * date:2021/9/6
 * time:17:35
 * email:gaoguanling@360.cn
 * link:
 */
@ExperimentalStdlibApi
object HostTaskRegister : TaskRegister<HostExtension> {
    override fun registerProjectTask(
        project: Project,
        android: AppExtension,
        extension: HostExtension
    ) {
        android.applicationVariants.all { variant ->
            when (variant) {
                is ApplicationVariantImpl -> {
                    updateManifest(variant, extension)
                    createHostConfig(project, variant, extension)
                    createBuiltInPluginJson(variant, extension)
                }
            }
        }
    }

    private fun updateManifest(variant: ApplicationVariantImpl, extension: HostExtension) {
        val taskConfig = variant.generateBuildConfigProvider.get()
        val appId = taskConfig.appPackageName.get()
        val newManifest: String = ComponentCreator.generateComponent(appId, extension)
        variant.outputs.forEach { output ->
            output.processManifestProvider.get().doLast { task ->
                task.outputs.files.forEach { file ->
                    if (file.exists()) {
                        Log.i("updateManifest", file.absolutePath)
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
                                manifest.writeText(updateContent)
                            }
                    } else {
                        Log.i("updateManifest", "fileNotExists")
                    }

                }
            }
        }
    }

    private fun createHostConfig(
        project: Project,
        variant: ApplicationVariantImpl,
        extension: HostExtension
    ) {
        val generateHostConfigTaskName =
            Constants.TASK_GENERATE + variant.name.capitalize(Locale.getDefault()) + "HostConfig"
        val generateHostConfigTask = project.task(generateHostConfigTaskName)

        generateHostConfigTask.doLast {
            FileCreators.createHostConfig(project, variant, extension)
        }
        generateHostConfigTask.group = Constants.HOST_TASKS_GROUP

        val taskConfig = variant.generateBuildConfigProvider.get()
        generateHostConfigTask.dependsOn(taskConfig)
        taskConfig.finalizedBy(generateHostConfigTask)
    }

    private fun createBuiltInPluginJson(variant: ApplicationVariantImpl, extension: HostExtension) {
        variant.mergeAssetsProvider.get().doLast {
            FileCreators.createBuiltInJson(variant, extension)
        }
    }
}