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
 *
 */

package com.qihoo360.replugin.gradle.plugin.manifest

import com.android.build.gradle.tasks.ProcessMultiApkApplicationManifest
import org.gradle.api.Project

/**
 * @author RePlugin Team
 */
class ManifestAPI {

    IManifest sManifestAPIImpl

    def getActivities(Project project, String variantName) {
        if (sManifestAPIImpl == null) {
            sManifestAPIImpl = new ManifestReader(manifestPath(project, variantName))
        }
        sManifestAPIImpl.activities
    }

    /**
     * 获取 AndroidManifest.xml 路径
     */
    def static manifestPath(Project project, String variantName) {
        println ">>> variantName:${variantName.capitalize()}"
        def processManifestTask = project.tasks.getByName("process${variantName.capitalize()}Manifest")
        println ">>> ManifestTaskName:${processManifestTask.class.name}"
        File manifestOutputFile = null
        if (processManifestTask instanceof ProcessMultiApkApplicationManifest) {
            processManifestTask = (ProcessMultiApkApplicationManifest) processManifestTask
            manifestOutputFile = new File(processManifestTask.getMultiApkManifestOutputDirectory().getAsFile().get(), "AndroidManifest.xml")
        }
        println ">>> ManifestPath:${manifestOutputFile.getAbsolutePath()}"
        return manifestOutputFile.getAbsolutePath()
    }
}
