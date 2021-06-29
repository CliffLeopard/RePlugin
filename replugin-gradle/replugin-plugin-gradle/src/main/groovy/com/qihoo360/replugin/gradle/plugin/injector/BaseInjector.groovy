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

package com.qihoo360.replugin.gradle.plugin.injector

import org.gradle.api.Project

/**
 * @author RePlugin Team
 */
abstract class BaseInjector implements IClassInjector {

    protected Project project

    protected String variantName

    @Override
    Object name() {
        return getClass().getSimpleName()
    }

    void setProject(Project project) {
        this.project = project;
    }

    void setVariantDir(String variantName) {
        this.variantName = variantName;
    }
}
