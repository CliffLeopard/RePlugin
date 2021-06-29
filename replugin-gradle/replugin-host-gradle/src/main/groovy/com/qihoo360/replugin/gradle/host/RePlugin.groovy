/*
 * Copyright (C) 2005-2017 Qihoo 360 Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed To in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.qihoo360.replugin.gradle.host

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.internal.api.ApplicationVariantImpl
import com.qihoo360.replugin.gradle.host.creator.FileCreators
import com.qihoo360.replugin.gradle.host.handlemanifest.ComponentsGenerator
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.LogLevel

/**
 * @author RePlugin Team
 */
class Replugin implements Plugin<Project> {

    def static TAG = AppConstant.TAG
    Project project
    RePluginConfig config

    @Override
    void apply(Project project) {
        println "${TAG} Welcome to replugin world ! "
        this.project = project
        project.extensions.create(AppConstant.USER_CONFIG, RePluginConfig)
        if (project.plugins.hasPlugin(AppPlugin)) {
            AppExtension android = project.extensions.getByType(AppExtension)
            android.applicationVariants.all { ApplicationVariantImpl variant ->
                if (config == null) {
                    config = project.extensions.getByName(AppConstant.USER_CONFIG)
                    checkUserConfig(config)
                }

                def generateBuildConfigTask = variant.generateBuildConfigProvider.get()
                def appID = generateBuildConfigTask.appPackageName.get()
                def newManifest = ComponentsGenerator.generateComponent(appID, config)
                println "${TAG} countTask=${config.countTask}"

                def generateHostConfigTaskName = AppConstant.TASK_GENERATE + variant.name.capitalize() + "HostConfig"
                def generateHostConfigTask = project.task(generateHostConfigTaskName)

                generateHostConfigTask.doLast {
                    FileCreators.createHostConfig(project, variant, config)
                }
                generateHostConfigTask.group = AppConstant.TASKS_GROUP

                //depends on build config task
                if (generateBuildConfigTask) {
                    generateHostConfigTask.dependsOn generateBuildConfigTask
                    generateBuildConfigTask.finalizedBy generateHostConfigTask
                }
                addBuildInJsonTask(variant)
                variant.outputs.each { output ->
                    output.processManifestProvider.get().doLast {
                        println "${AppConstant.TAG} processManifest: ${it.outputs.files}"
                        it.outputs.files.each { File file ->
                            updateManifest(file, newManifest)
                        }
                    }
                }
            }
        }
    }
    /**
     * 在 mergeAssetsTask 执行完之后，继续执行生成保存内置插件信息的 plugins-builtin.json
     * @param variant
     */
    private void addBuildInJsonTask(ApplicationVariantImpl variant) {
        variant.mergeAssetsProvider.get().doLast {
            FileCreators.createBuiltinJson(variant, config)
        }
    }

    /**
     * 旧版生成plugins-builtin.json的方案
     * 以debug为例：
     * 在mergeDebugAssets 和 compressDebugAssets之间插入自定义的task:rpGenerateDebugBuiltinJson
     * 在rpGenerateDebugBuiltinJson中生成plugins-builtin.json
     * 但是这种方案在gradle-4.2.1中无效。
     * 但是 gradle-4.2.1的构建序列确实是 mergeDebugAssets，compressDebugAssets 顺序执行。所以问题比较诡异
     * 所以使用了 `addBuildInJsonTask` 新方式，直接在 `mergeDebugAssets`task内加入构建逻辑
     * @param variant
     */
    private void oldVersionBuildInJsonTask(ApplicationVariantImpl variant) {
        def generateBuiltinJsonTaskName = AppConstant.TASK_GENERATE + variant.name.capitalize() + "BuiltinJson"
        def generateBuiltinJsonTask = project.task(generateBuiltinJsonTaskName)
        generateBuiltinJsonTask.doFirst {
            FileCreators.createBuiltinJson(variant, config)
        }
        generateBuiltinJsonTask.group = AppConstant.TASKS_GROUP
        if (mergeAssetsTask) {
            generateBuiltinJsonTask.dependsOn(mergeAssetsTask)
            mergeAssetsTask.finalizedBy(generateBuiltinJsonTask)
            project.getTasksByName("compress${variant.name.capitalize()}Assets", false).find().dependsOn(generateBuiltinJsonTask)
        }
    }

    /**
     *
     * @hyongbai
     * 
     * 在gradle plugin 3.0.0之前，file是文件，且文件名为AndroidManifest.xml
     * 在gradle plugin 3.0.0之后，file是目录，(特别是3.3.2)在这里改成递归的方式替换内部所有的 manifest 文件
     *
     * @param file manifest文件
     * @param newManifest 需要添加的 manifest 信息
     */
    def updateManifest(def file, def newManifest) {
        // 除了目录和AndroidManifest.xml之外，还可能会包含manifest-merger-debug-report.txt等不相干的文件，过滤它
        if (file == null || !file.exists() || newManifest == null) return
        if (file.isDirectory()) {
            println "${AppConstant.TAG} updateManifest: ${file}"
            file.listFiles().each {
                updateManifest(it, newManifest)
            }
        } else if (file.name.equalsIgnoreCase("AndroidManifest.xml")) {
            appendManifest(file, newManifest)
        }
    }

    static def appendManifest(def file, def content) {
        if (file == null || !file.exists()) return
        println "${AppConstant.TAG} appendManifest: ${file}"
        def updatedContent = file.getText("UTF-8").replaceAll("</application>", content + "</application>")
        file.write(updatedContent, 'UTF-8')
    }

    private static String getTaskName(String prefix, String variantName, String tail) {
        return prefix + variantName.capitalize() + tail
    }

    /**
     * 检查用户配置项
     */
    def checkUserConfig(config) {
/*
        def persistentName = config.persistentName

        if (persistentName == null || persistentName.trim().equals("")) {
            project.logger.log(LogLevel.ERROR, "\n---------------------------------------------------------------------------------")
            project.logger.log(LogLevel.ERROR, " ERROR: persistentName can'te be empty, please set persistentName in replugin. ")
            project.logger.log(LogLevel.ERROR, "---------------------------------------------------------------------------------\n")
            System.exit(0)
            return
        }
*/
        doCheckConfig("countProcess", config.countProcess)
        doCheckConfig("countTranslucentStandard", config.countTranslucentStandard)
        doCheckConfig("countTranslucentSingleTop", config.countTranslucentSingleTop)
        doCheckConfig("countTranslucentSingleTask", config.countTranslucentSingleTask)
        doCheckConfig("countTranslucentSingleInstance", config.countTranslucentSingleInstance)
        doCheckConfig("countNotTranslucentStandard", config.countNotTranslucentStandard)
        doCheckConfig("countNotTranslucentSingleTop", config.countNotTranslucentSingleTop)
        doCheckConfig("countNotTranslucentSingleTask", config.countNotTranslucentSingleTask)
        doCheckConfig("countNotTranslucentSingleInstance", config.countNotTranslucentSingleInstance)
        doCheckConfig("countTask", config.countTask)

        println '--------------------------------------------------------------------------'
        println "${TAG} useAppCompat=${config.useAppCompat}"
        println "${TAG} countProcess=${config.countProcess}"

        println "${TAG} countTranslucentStandard=${config.countTranslucentStandard}"
        println "${TAG} countTranslucentSingleTop=${config.countTranslucentSingleTop}"
        println "${TAG} countTranslucentSingleTask=${config.countTranslucentSingleTask}"
        println "${TAG} countTranslucentSingleInstance=${config.countTranslucentSingleInstance}"
        println "${TAG} countNotTranslucentStandard=${config.countNotTranslucentStandard}"
        println "${TAG} countNotTranslucentSingleTop=${config.countNotTranslucentSingleTop}"
        println "${TAG} countNotTranslucentSingleTask=${config.countNotTranslucentSingleTask}"
        println "${TAG} countNotTranslucentSingleInstance=${config.countNotTranslucentSingleInstance}"
        println "${TAG} countTask=${config.countTask}"
        println '--------------------------------------------------------------------------'
    }

    /**
     * 检查配置项是否正确
     * @param name 配置项
     * @param count 配置值
     */
    def doCheckConfig(def name, def count) {
        if (!(count instanceof Integer) || count < 0) {
            this.project.logger.log(LogLevel.ERROR, "\n--------------------------------------------------------")
            this.project.logger.log(LogLevel.ERROR, " ${TAG} ERROR: ${name} must be an positive integer. ")
            this.project.logger.log(LogLevel.ERROR, "--------------------------------------------------------\n")
            System.exit(0)
        }
    }
}
