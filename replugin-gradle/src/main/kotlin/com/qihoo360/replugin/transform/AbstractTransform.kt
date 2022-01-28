package com.qihoo360.replugin.transform

import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.AppExtension
import com.android.build.gradle.internal.pipeline.TransformManager
import com.qihoo360.replugin.Log
import com.qihoo360.replugin.config.BaseExtension
import com.qihoo360.replugin.transform.bean.InstrumentationContext
import com.qihoo360.replugin.transform.bean.TransformClassInfo
import com.qihoo360.replugin.transform.pool.TransformTaskCreator
import com.qihoo360.replugin.transform.pool.TransformThreadPool
import com.qihoo360.replugin.transform.visitor.FilterClassVisitor
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
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
abstract class AbstractTransform(appExtension: AppExtension, open val extension: BaseExtension) :
    Transform() {

    private var bootClassPaths = mutableSetOf<URL>()

    // 遇到复杂操作时，在子类Transform会使用到。目前业务不需要
    private var globalClassLoader: URLClassLoader? = null

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
//        initClassLoader(transformInvocation)
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

    abstract fun isSkipClass(classInfo: TransformClassInfo): Boolean
    abstract fun isExcludeClass(classInfo: TransformClassInfo): Boolean
    abstract fun transformVisitor(visitor: ClassVisitor, context: InstrumentationContext): ClassVisitor?

    /**
     * 单个Class文件的转化,
     * 1. 成功转化：返回转化后的ByteArray
     * 2. 在编译时剔除此类: excluded 返回空的ByteArray
     * 3. 此类不参与转化，  skip 使用原来类继续编译：返回NULL
     */
    fun transformClass(classInfo: TransformClassInfo, inputBytes: ByteArray): ByteArray? {
        if (isSkipClass(classInfo)) {
            return null
        } else if (isExcludeClass(classInfo)) {
            return ByteArray(0)
        }
        return rewriteClass(classInfo, inputBytes)
    }

    private fun rewriteClass(classInfo: TransformClassInfo, inputBytes: ByteArray): ByteArray? {
        val context = InstrumentationContext(classInfo, extension)
        val classReader = ClassReader(inputBytes)
        val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
        val visitor = transformVisitor(classWriter, context) ?: return null
        classReader.accept(
            visitor,
            ClassReader.SKIP_FRAMES or ClassReader.EXPAND_FRAMES
        )
        return if (context.classModified) {
            printChangedLog(context.classInfo)
            classWriter.toByteArray()
        } else
            null
    }

    private fun printChangedLog(classInfo: TransformClassInfo) {
        Log.i(
            tag,
            "ChangedClass:\n class:${classInfo.name}\n fromJar:${classInfo.fromJar}\n toPath:${classInfo.toPath}"
        )
    }

    companion object {
        private const val tag = "AbstractTransform"
    }

    /**
     * 由于我们业务比较简单，不需要使用这种方式过滤，打出接口以后使用
     */
    private fun filerClassByReader(classReader: ClassReader, context: InstrumentationContext) {
        classReader.accept(
            FilterClassVisitor(context),
            ClassReader.SKIP_DEBUG or ClassReader.SKIP_CODE or ClassReader.SKIP_FRAMES
        )
    }
}