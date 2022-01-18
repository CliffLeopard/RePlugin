package com.qihoo360.replugin.transform.pool

import java.util.concurrent.ForkJoinPool
import java.util.concurrent.ForkJoinTask

/**
 * author:gaoguanling
 * date:2021/8/30
 * time:16:13
 * email:gaoguanling@360.cn
 * link:
 */
object TransformThreadPool {

    private val transformPool: ForkJoinPool = ForkJoinPool.commonPool()
    fun runTransformTask(transformTask: TransformTask): ForkJoinTask<Boolean> {
        return transformPool.submit(transformTask)
    }

    /**
     *     下面的方案，使用IO线程池处理IO,使用独立线程池处理Class字节变动。 由于会频繁切换线程,降低性能 暂时不采用
    private val ioPool: ExecutorService = Executors.newFixedThreadPool(2)
    fun runIOTask(runnable: Runnable) {
    ioPool.submit(runnable)
    }

    interface TransformProcessor {
    fun readBytes(): ByteArray
    fun changeBytes(bytes: ByteArray): Pair<ByteArray?, ByteArray>
    fun writeBytes(result: ByteArray?, origin: ByteArray): Boolean
    }
    fun transform(classInfo: TransformClassInfo, transform: BaseTransform) {
    val processor = object : TransformProcessor {
    override fun readBytes(): ByteArray {
    return IOUtils.toByteArray(FileInputStream(classInfo.fromPath))
    }

    override fun changeBytes(bytes: ByteArray): Pair<ByteArray?, ByteArray> {
    val result = transform.transformClass(classInfo, bytes)
    return Pair(result, bytes)
    }

    override fun writeBytes(result: ByteArray?, origin: ByteArray): Boolean {
    IOUtils.write(result ?: origin, FileOutputStream(classInfo.toPath))
    return result != null
    }
    }
    transform(processor)
    }

    fun transform(processor: TransformProcessor) {
    ioPool.submit {
    val originBytes = processor.readBytes()
    transformPool.submit {
    val changedBytes = processor.changeBytes(originBytes)
    ioPool.submit {
    processor.writeBytes(changedBytes.first, changedBytes.second)
    }
    }
    }
    }
     **/
}