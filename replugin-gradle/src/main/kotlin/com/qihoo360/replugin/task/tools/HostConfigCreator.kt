package com.qihoo360.replugin.task.tools

import com.android.build.gradle.internal.api.ApplicationVariantImpl
import com.qihoo360.replugin.host.HostExtension
import org.gradle.api.Project
import java.io.File

/**
 * @author RePlugin Team
 */
class HostConfigCreator(
    private var project: Project,
    private var variant: ApplicationVariantImpl,
    private var extension: HostExtension
) : IFileCreator {

    private var relativeDir = "/com/qihoo360/replugin/gen/"
    private var fileName = "RePluginHostConfig.java"
    private var fileDir: File

    init {
        val buildConfigGeneratedDir: File =
            this.variant.generateBuildConfigProvider.get().sourceOutputDir.get().asFile
        fileDir = File(buildConfigGeneratedDir, relativeDir)
    }


    override fun getFileName(): String {
        return fileName
    }

    override fun getFileDir(): File {
        return fileDir
    }

    override fun createFileContent(): String {
        return """
            
package com.qihoo360.replugin.gen;

/**
 * 注意：此文件由插件化框架自动生成，请不要手动修改。
 */
public class RePluginHostConfig {

    // 常驻进程名字
    public static String PERSISTENT_NAME = "${extension.persistentName}";

    // 是否使用“常驻进程”（见PERSISTENT_NAME）作为插件的管理进程。若为False，则会使用默认进程
    public static boolean PERSISTENT_ENABLE = ${extension.persistentEnable};

    // 背景透明的坑的数量（每种 launchMode 不同）
    public static int ACTIVITY_PIT_COUNT_TS_STANDARD = ${extension.countTranslucentStandard};
    public static int ACTIVITY_PIT_COUNT_TS_SINGLE_TOP = ${extension.countTranslucentSingleTop};
    public static int ACTIVITY_PIT_COUNT_TS_SINGLE_TASK = ${extension.countTranslucentSingleTask};
    public static int ACTIVITY_PIT_COUNT_TS_SINGLE_INSTANCE = ${
            extension.countTranslucentSingleInstance
        };

    // 背景不透明的坑的数量（每种 launchMode 不同）
    public static int ACTIVITY_PIT_COUNT_NTS_STANDARD = ${extension.countNotTranslucentStandard};
    public static int ACTIVITY_PIT_COUNT_NTS_SINGLE_TOP = ${extension.countNotTranslucentSingleTop};
    public static int ACTIVITY_PIT_COUNT_NTS_SINGLE_TASK = ${extension.countNotTranslucentSingleTask};
    public static int ACTIVITY_PIT_COUNT_NTS_SINGLE_INSTANCE = ${
            extension.countNotTranslucentSingleInstance
        };

    // TaskAffinity 组数
    public static int ACTIVITY_PIT_COUNT_TASK = ${extension.countTask};

    // 是否使用 AppCompat 库
    public static boolean ACTIVITY_PIT_USE_APPCOMPAT = ${extension.useAppCompat};

    //------------------------------------------------------------
    // 主程序支持的插件版本范围
    //------------------------------------------------------------

    // HOST 向下兼容的插件版本
    public static int ADAPTER_COMPATIBLE_VERSION = ${extension.compatibleVersion};

    // HOST 插件版本
    public static int ADAPTER_CURRENT_VERSION = ${extension.currentVersion};
}    
""".trimIndent()
    }

    fun setFileDir(fileDir: File) {
        this.fileDir = fileDir
    }

    fun setFileName(fileName: String) {
        this.fileName = fileName
    }
}