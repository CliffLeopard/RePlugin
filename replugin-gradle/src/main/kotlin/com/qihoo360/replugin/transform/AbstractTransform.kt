package com.qihoo360.replugin.transform

import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.AppExtension
import com.android.build.gradle.internal.pipeline.TransformManager
import com.qihoo360.replugin.config.BaseExtension
import com.qihoo360.replugin.transform.bean.TransformClassInfo
import com.qihoo360.replugin.transform.pool.TransformTaskCreator
import com.qihoo360.replugin.transform.pool.TransformThreadPool
import java.io.File
import java.io.IOException
import java.net.URL
import java.net.URLClassLoader
import java.util.concurrent.ForkJoinTask

/**
 * author:CliffLeopard
 * date:5/2/21
 * time:2:23 AM
 * email:precipiceleopard@gmail.com
 * link:
 */
abstract class AbstractTransform(appExtension: AppExtension,val extension:BaseExtension) :
    Transform() {

    private var bootClassPaths = mutableSetOf<URL>()

    // 遇到复杂操作时，在子类Transform会使用到。目前业务不需要
    private lateinit var globalClassLoader: URLClassLoader

    init {
        appExtension.bootClasspath.forEach { file: File ->
            bootClassPaths.add(file.toURI().toURL())
        }
    }

    override fun getName(): String {
        return this.javaClass.simpleName
    }

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        return TransformManager.CONTENT_CLASS
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Throws(TransformException::class, InterruptedException::class, IOException::class)
    override fun transform(transformInvocation: TransformInvocation) {
        initClassLoader(transformInvocation)
        val taskList = mutableListOf<ForkJoinTask<Boolean>>()
        transformInvocation.inputs
            .asSequence()
            .forEach { transformInput ->
                TransformTaskCreator.createTask(
                    isIncremental,
                    transformInput,
                    transformInvocation.outputProvider,
                    this
                ) { task ->
                    taskList.add(TransformThreadPool.runTransformTask(task))
                }
            }
        taskList.forEach { task ->
            if (!task.isDone)
                task.join()
        }
        taskList.clear()
    }

    /**
     * 使用classLoader构建类图，方便后续类转变时的判断
     * ClassLoader的加载机制决定并不会消耗大量内存，这里只多了一次类循环，记录其路径
     */
    private fun initClassLoader(transformInvocation: TransformInvocation) {
        val urlList = mutableListOf<URL>()
        urlList.addAll(bootClassPaths)
        for (input in transformInvocation.inputs) {
            for (directoryInput in input.directoryInputs) {
                urlList.add(directoryInput.file.toURI().toURL())
            }
            for (jarInput in input.jarInputs) {
                urlList.add(jarInput.file.toURI().toURL())
            }
        }
        globalClassLoader = URLClassLoader(urlList.toTypedArray())
    }

    /**
     * 单个Class文件的转化,如果不参与转化则直接返回null
     */
    abstract fun transformClass(classInfo: TransformClassInfo, inputBytes: ByteArray): ByteArray?
}