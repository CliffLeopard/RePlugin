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

package com.qihoo360.replugin.gradle.plugin.inner

import com.android.build.api.transform.*
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.BasePlugin
import com.android.build.gradle.internal.pipeline.TransformManager
import com.qihoo360.replugin.gradle.plugin.injector.IClassInjector
import com.qihoo360.replugin.gradle.plugin.injector.Injectors
import javassist.ClassPool
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.gradle.api.GradleException
import org.gradle.api.Project
import qihoo360.replugin.gradle.utils.VersionHelper

import java.nio.file.Files
import java.nio.file.Paths
import java.security.MessageDigest
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.regex.Pattern

/**
 * @author RePlugin Team
 */
public class ReClass2Transform extends Transform {

    private Project project
    private def globalScope

    /* 需要处理的 jar 包 */
    def inputJars = [:]
    def inputDirs = [] as Set

    //def map = [:]

    public ReClass2Transform(Project p) {
        this.project = p
        def appPlugin = project.plugins.getPlugin(AppPlugin)

        VersionHelper.Version version = VersionHelper.getAndroidBuildGradleVersion(p)
        if (version.major < 4) {
            // taskManager 在 2.1.3 中为 protected 访问类型的，在之后的版本为 private 访问类型的，
            // 使用反射访问
            def taskManager = BasePlugin.metaClass.getProperty(appPlugin, "taskManager")
            this.globalScope = taskManager.globalScope
        } else {
            // 4.0.0开始 BasePlugin is Deprecated
            def extension = project.extensions.getByName("android")
            this.globalScope = extension.globalScope
        }
    }

    @Override
    String getName() {
        return '___ReClass___'
    }

    void transform(TransformInvocation invocation) throws TransformException, InterruptedException, IOException {
        welcome()

        /* 读取用户配置 */
        def config = project.extensions.getByName('repluginPluginConfig')

        File rootLocation = null
        try {
            rootLocation = invocation.outputProvider.rootLocation
        } catch (Throwable e) {
            //android gradle plugin 3.0.0+ 修改了私有变量，将其移动到了IntermediateFolderUtils中去
            rootLocation = invocation.outputProvider.folderUtils.getRootFolder()
        }
        if (rootLocation == null) {
            throw new GradleException("can't get transform root location")
        }
        println ">>> rootLocation: ${rootLocation}"
        // Compatible with path separators for window and Linux, and fit split param based on 'Pattern.quote'
        def variantDir = rootLocation.absolutePath.split(getName() + Pattern.quote(File.separator))[1]
        println ">>> variantDir: ${variantDir}"

        CommonData.appModule = config.appModule
        CommonData.ignoredActivities = config.ignoredActivities

        def injectors = includedInjectors(config, variantDir)
        if (injectors.isEmpty()) {
            copyResult(inputs, invocation.outputProvider) // 跳过 reclass
        } else {
            Util.newSection()
            ClassPool pool = prepare(invocation)

            /* 进行注入操作 */
            Util.newSection()
            injectors.each { String nickName, IClassInjector injector ->
                println ">>> Do: ${nickName}"
                def configPre = Util.lowerCaseAtIndex(nickName, 0)
                inputDirs.each { File dir ->
                    injector.injectClass(pool, dir.absolutePath, config.properties["${configPre}Config"])
                }
                inputJars.each { File jar, File dir ->
                    injector.injectClass(pool, dir.absolutePath, config.properties["${configPre}Config"])
                }
            }

            Util.newSection()
            repackage()
        }
    }

    def prepare(TransformInvocation invocation) {
        ClassPool pool = new ClassPool(true)

        // android.jar
        def androidJarPath = Util.getAndroidJarPath(globalScope)
        pool.appendClassPath(androidJarPath)

        def visitor = new ClassFileVisitor()

        // 原始项目中引用的 classpathList
        invocation.inputs.each { input ->
            input.directoryInputs.each { dirInput ->
                def destDir = invocation.outputProvider.getContentLocation(dirInput.name, dirInput.contentTypes, dirInput.scopes, Format.DIRECTORY)
                println "    [copy] ${dirInput.file.path} -> ${destDir.path}"
                com.android.utils.FileUtils.copyDirectory(dirInput.file, destDir)
                pool.appendClassPath(destDir.path)
                inputDirs.add(destDir)
                visitor.setBaseDir(destDir.path)
                Files.walkFileTree(destDir.toPath(), visitor)
            }
            input.jarInputs.each { jarInput ->
                def jarName = jarInput.name
                def md5Name = MessageDigest.getInstance("MD5").digest(jarInput.file.readBytes()).encodeHex().toString()
                if (jarName.endsWith(".jar")) {
                    jarName = jarName.substring(0, jarName.length() - 4)
                }
                def destJar = invocation.outputProvider.getContentLocation(jarName + md5Name, jarInput.contentTypes, jarInput.scopes, Format.JAR)
                def clsDir = new File(destJar.parentFile, destJar.name.substring(0, destJar.name.lastIndexOf('.')))
                FileUtils.copyFile(jarInput.file, destJar)
                Util.unzip(jarInput.file.path, clsDir.path)
                if (clsDir.exists()) { // 如果jar是空的，目录不存在，直接跳过
                    pool.appendClassPath(clsDir.path)
                    inputJars.put(destJar, clsDir)
                    visitor.setBaseDir(clsDir.path)
                    Files.walkFileTree(clsDir.toPath(), visitor)
                }
            }
        }

        //println ">>> ClassPath: ${pool}"
        return pool
    }

    def repackage() {
        inputJars.each { File jar, File dir ->
            Util.zipDir(dir.path, jar.path)
            FileUtils.deleteDirectory(dir)
        }
    }

    /**
     * 返回用户未忽略的注入器的集合
     */
    def includedInjectors(def cfg, String variantDir) {
        def injectors = new LinkedHashMap<String, IClassInjector>()
        Injectors.values().each {
            //设置project
            it.injector.setProject(project)
            //设置variant关键dir
            it.injector.setVariantDir(variantDir)
            if (!(it.nickName in cfg.ignoredInjectors)) {
                //injectors[it.nickName] = it.injector
                injectors.put(it.nickName, it.injector)
            }
        }
        injectors
    }

    /**
     * 拷贝处理结果
     */
    def copyResult(def inputs, def outputs) {
        // Util.newSection()
        inputs.each { TransformInput input ->
            input.directoryInputs.each { DirectoryInput dirInput ->
                copyDir(outputs, dirInput)
            }
            input.jarInputs.each { JarInput jarInput ->
                copyJar(outputs, jarInput)
            }
        }
    }

    /**
     * 拷贝 Jar
     */
    def copyJar(TransformOutputProvider output, JarInput input) {
        File jar = input.file
        String jarPath = map.get(jar.absolutePath);
        if (jarPath != null) {
            jar = new File(jarPath)
        }

        if(!jar.exists()){
            return
        }

        String destName = input.name
        def hexName = DigestUtils.md5Hex(jar.absolutePath)
        if (destName.endsWith('.jar')) {
            destName = destName.substring(0, destName.length() - 4)
        }
        File dest = output.getContentLocation(destName + '_' + hexName, input.contentTypes, input.scopes, Format.JAR)
        FileUtils.copyFile(jar, dest)

/*
        def path = jar.absolutePath
        if (path in CommonData.includeJars) {
            println ">>> 拷贝Jar ${path} 到 ${dest.absolutePath}"
        }
*/
    }

    /**
     * 拷贝目录
     */
    def copyDir(TransformOutputProvider output, DirectoryInput input) {
        File dest = output.getContentLocation(input.name, input.contentTypes, input.scopes, Format.DIRECTORY)
        FileUtils.copyDirectory(input.file, dest)
//        println ">>> 拷贝目录 ${input.file.absolutePath} 到 ${dest.absolutePath}"
    }

    /**
     * 欢迎
     */
    def welcome() {
        println '\n'
        60.times { print '=' }
        println '\n                    replugin-plugin-gradle'
        60.times { print '=' }
        println("""
//Add repluginPluginConfig to your build.gradle to enable this plugin:
//
//repluginPluginConfig {
//    // Name of 'App Module'，use '' if root dir is 'App Module'. ':app' as default.
//    appModule = ':app'
//
//    // Injectors ignored
//    // LoaderActivityInjector: Replace Activity to LoaderActivity
//    // ProviderInjector: Inject provider method call.
//    ignoredInjectors = ['LoaderActivityInjector']
//}""")
//        println('\n')
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }
}
