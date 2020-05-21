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

package com.qihoo360.replugin.gradle.plugin.injector.zhushou

import com.qihoo360.replugin.gradle.plugin.injector.BaseInjector
import javassist.ClassPool
import javassist.CtField

import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes

/**
 * @author RePlugin Team
 */
public class ConstantValueInjector extends BaseInjector {

    @Override
    def injectClass(ClassPool pool, String dir, Map config) {

        Files.walkFileTree(Paths.get(dir), new SimpleFileVisitor<Path>() {
            @Override
            FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

                String filePath = file.toString()
                if (filePath =~ /com[\\\/]qihoo[\\\/]appstore[\\\/]export[\\\/]proxy[\\\/]Constant\.class/) {
                    def ctCls
                    try {
                        ctCls = pool.makeClass("com.qihoo.appstore.export.proxy.Constant");
                        ctCls.addField(CtField.make(
                                String.format(
                                        "public static final String PLUGIN_NAME = \"%s\";",
                                        project.android.defaultConfig.applicationId
                                ),
                                ctCls
                        ))
                        ctCls.writeFile(dir)
                    } catch (Throwable t) {
                        println "    [Warning] $filePath --> ${t.toString()} : ${t.getCause().toString()}"
                    } finally {
                        if (ctCls != null) {
                            ctCls.detach()
                        }
                    }
                    return FileVisitResult.TERMINATE
                }
                return super.visitFile(file, attrs)
            }
        })
    }
}
