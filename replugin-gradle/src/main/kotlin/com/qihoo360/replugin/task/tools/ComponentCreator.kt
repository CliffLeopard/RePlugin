package com.qihoo360.replugin.task.tools

import com.qihoo360.replugin.host.HostExtension
import org.redundent.kotlin.xml.xml

/**
 * author:gaoguanling
 * date:2021/7/6
 * time:11:30
 * email:gaoguanling@360.cn
 * link:
 */
object ComponentCreator {

    private const val infix = "loader.a.Activity"
    private const val name = "android:name"
    private const val process = "android:process"
    private const val task = "android:taskAffinity"
    private const val launchMode = "android:launchMode"
    private const val authorities = "android:authorities"
    private const val multiProcess = "android:multiprocess"
    private const val cfg = "android:configChanges"
    private const val cfgV = "keyboard|keyboardHidden|orientation|screenSize"
    private const val exp = "android:exported"
    private const val expV = "false"
    private const val ori = "android:screenOrientation"
    private const val oriV = "portrait"
    private const val theme = "android:theme"
    private const val themeTS = "@android:style/Theme.Translucent.NoTitleBar"
    private const val THEME_NTS_USE_APP_COMPAT = "@style/Theme.AppCompat"
    private const val THEME_NTS_NOT_USE_APP_COMPAT = "@android:style/Theme.NoTitleBar"
    private var themeNTS = THEME_NTS_NOT_USE_APP_COMPAT

    /**
     * 动态生成插件化框架中需要的组件
     *
     * @param applicationID 宿主的 applicationID
     * @param extension        用户配置
     * @return String       插件化框架中需要的组件
     */
    fun generateComponent(applicationID: String, extension: HostExtension): String {
        // 是否使用 AppCompat 库（涉及到默认主题）
        themeNTS = if (extension.useAppCompat) {
            THEME_NTS_USE_APP_COMPAT
        } else {
            THEME_NTS_NOT_USE_APP_COMPAT
        }

        /* UI 进程 */
        val pluginMgrProcessName: String =
            if (extension.persistentEnable) extension.persistentName else applicationID
        val xml = xml("application") {
            // 常驻进程Provider
            "provider" {
                attributes(
                    Pair(name, "com.qihoo360.replugin.component.process.ProcessPitProviderPersist"),
                    Pair(authorities, "${applicationID}.loader.p.main"),
                    Pair(exp, false),
                    Pair(process, pluginMgrProcessName)
                )
            }
            "provider" {
                attributes(
                    Pair(name, "com.qihoo360.replugin.component.provider.PluginPitProviderPersist"),
                    Pair(authorities, "${applicationID}.Plugin.NP.PSP"),
                    Pair(exp, false),
                    Pair(process, pluginMgrProcessName)
                )
            }

            // ServiceManager 服务框架
            "provider" {
                attributes(
                    Pair(name, "com.qihoo360.mobilesafe.svcmanager.ServiceProvider"),
                    Pair(authorities, "${applicationID}.svcmanager"),
                    Pair(exp, false),
                    Pair(multiProcess, false),
                    Pair(process, pluginMgrProcessName)
                )
            }

            "service" {
                attributes(
                    Pair(
                        name,
                        "com.qihoo360.replugin.component.service.server.PluginPitServiceGuard"
                    ),
                    Pair(process, pluginMgrProcessName)
                )
            }

            /* 透明坑 */
            for (i in 0 until extension.countTranslucentStandard)
                "activity" {
                    attributes(
                        Pair(name, "$applicationID.${infix}N1NRTS$i"),
                        Pair(cfg, cfgV),
                        Pair(exp, expV),
                        Pair(ori, oriV),
                        Pair(theme, themeTS)
                    )
                }

            for (i in 0 until extension.countTranslucentSingleTop)
                "activity" {
                    attributes(
                        Pair(name, "$applicationID.${infix}N1STPTS$i"),
                        Pair(cfg, cfgV),
                        Pair(exp, expV),
                        Pair(ori, oriV),
                        Pair(theme, themeTS),
                        Pair(launchMode, "singleTop")
                    )
                }

            for (i in 0 until extension.countTranslucentSingleTask)
                "activity" {
                    attributes(
                        Pair(name, "$applicationID.${infix}N1STTS$i"),
                        Pair(cfg, cfgV),
                        Pair(exp, expV),
                        Pair(ori, oriV),
                        Pair(theme, themeTS),
                        Pair(launchMode, "singleTask")
                    )
                }

            for (i in 0 until extension.countTranslucentSingleInstance)
                "activity" {
                    attributes(
                        Pair(name, "$applicationID.${infix}N1SITS$i"),
                        Pair(cfg, cfgV),
                        Pair(exp, expV),
                        Pair(ori, oriV),
                        Pair(theme, themeTS),
                        Pair(launchMode, "singleInstance")
                    )
                }


            /* 不透明坑 */

            for (i in 0 until extension.countNotTranslucentStandard)
                "activity" {
                    attributes(
                        Pair(name, "$applicationID.${infix}N1NRNTS$i"),
                        Pair(cfg, cfgV),
                        Pair(exp, expV),
                        Pair(ori, oriV),
                        Pair(theme, themeNTS)
                    )
                }

            for (i in 0 until extension.countNotTranslucentSingleTop)
                "activity" {
                    attributes(
                        Pair(name, "$applicationID.${infix}N1STPNTS$i"),
                        Pair(cfg, cfgV),
                        Pair(exp, expV),
                        Pair(ori, oriV),
                        Pair(theme, themeNTS),
                        Pair(launchMode, "singleTop")
                    )
                }

            for (i in 0 until extension.countNotTranslucentSingleTask)
                "activity" {
                    attributes(
                        Pair(name, "$applicationID.${infix}N1STNTS$i"),
                        Pair(cfg, cfgV),
                        Pair(exp, expV),
                        Pair(ori, oriV),
                        Pair(theme, themeNTS),
                        Pair(launchMode, "singleTask")
                    )
                }

            for (i in 0 until extension.countNotTranslucentSingleInstance)
                "activity" {
                    attributes(
                        Pair(name, "$applicationID.${infix}N1SINTS$i"),
                        Pair(cfg, cfgV),
                        Pair(exp, expV),
                        Pair(ori, oriV),
                        Pair(theme, themeNTS),
                        Pair(launchMode, "singleInstance")
                    )
                }

            /* TaskAffinity */
            for (i in 0 until extension.countTask) {
                for (j in 0 until extension.countTranslucentStandard)
                    "activity" {
                        attributes(
                            Pair(name, "${applicationID}.${infix}N1TA${i}NRTS${j}"),
                            Pair(cfg, cfgV),
                            Pair(exp, expV),
                            Pair(ori, oriV),
                            Pair(theme, themeTS),
                            Pair(task, ":t$i")
                        )
                    }
                for (j in 0 until extension.countTranslucentSingleTop)
                    "activity" {
                        attributes(
                            Pair(name, "${applicationID}.${infix}N1TA${i}STPTS${j}"),
                            Pair(cfg, cfgV),
                            Pair(exp, expV),
                            Pair(ori, oriV),
                            Pair(theme, themeTS),
                            Pair(task, ":t$i"),
                            Pair(launchMode, "singleTop")
                        )
                    }

                for (j in 0 until extension.countTranslucentSingleTask)
                    "activity" {
                        attributes(
                            Pair(name, "${applicationID}.${infix}N1TA${i}STTS${j}"),
                            Pair(cfg, cfgV),
                            Pair(exp, expV),
                            Pair(ori, oriV),
                            Pair(theme, themeTS),
                            Pair(task, ":t$i"),
                            Pair(launchMode, "singleTask")
                        )
                    }

                for (j in 0 until extension.countNotTranslucentStandard)
                    "activity" {
                        attributes(
                            Pair(name, "${applicationID}.${infix}N1TA${i}NRNTS${j}"),
                            Pair(cfg, cfgV),
                            Pair(exp, expV),
                            Pair(ori, oriV),
                            Pair(theme, themeNTS),
                            Pair(task, ":t$i")
                        )
                    }
                for (j in 0 until extension.countNotTranslucentSingleTop)
                    "activity" {
                        attributes(
                            Pair(name, "${applicationID}.${infix}N1TA${i}STPNTS${j}"),
                            Pair(cfg, cfgV),
                            Pair(exp, expV),
                            Pair(ori, oriV),
                            Pair(theme, themeNTS),
                            Pair(task, ":t$i"),
                            Pair(launchMode, "singleTop")
                        )
                    }

                for (j in 0 until extension.countNotTranslucentSingleTask)
                    "activity" {
                        attributes(
                            Pair(name, "${applicationID}.${infix}N1TA${i}STNTS${j}"),
                            Pair(cfg, cfgV),
                            Pair(exp, expV),
                            Pair(ori, oriV),
                            Pair(theme, themeNTS),
                            Pair(task, ":t$i"),
                            Pair(launchMode, "singleTask")
                        )
                    }
            }
        }

        // 删除 application 标签
        val normalStr = xml.toString().replace("<application>", "").replace("</application>", "")

        // 将单进程和多进程的组件相加
        return normalStr + generateMultiProcessComponent(applicationID, extension)
    }

    /**
     * 生成多进程坑位配置
     */
    private fun generateMultiProcessComponent(applicationID: String, extension: HostExtension): String {
        if (extension.countProcess == 0) {
            return ""
        }

        /* 自定义进程 */
        val xml = xml("application") {
            for (p in 0 until extension.countProcess) {

                // 透明
                for (j in 0 until extension.countTranslucentStandard)
                    "activity" {
                        attributes(
                            Pair(name, "$applicationID.${infix}P${p}NRTS$j"),
                            Pair(cfg, cfgV),
                            Pair(exp, expV),
                            Pair(ori, oriV),
                            Pair(theme, themeTS),
                            Pair(process, ":p${p}")
                        )
                    }
                for (j in 0 until extension.countTranslucentSingleTop)
                    "activity" {
                        attributes(
                            Pair(name, "$applicationID.${infix}P${p}STPTS$j"),
                            Pair(cfg, cfgV),
                            Pair(exp, expV),
                            Pair(ori, oriV),
                            Pair(theme, themeTS),
                            Pair(process, ":p${p}"),
                            Pair(launchMode, "singleTop")
                        )
                    }

                for (j in 0 until extension.countTranslucentSingleTask)
                    "activity" {
                        attributes(
                            Pair(name, "$applicationID.${infix}P${p}STTS$j"),
                            Pair(cfg, cfgV),
                            Pair(exp, expV),
                            Pair(ori, oriV),
                            Pair(theme, themeTS),
                            Pair(process, ":p${p}"),
                            Pair(launchMode, "singleTask")
                        )
                    }

                for (j in 0 until extension.countTranslucentSingleInstance)
                    "activity" {
                        attributes(
                            Pair(name, "$applicationID.${infix}P${p}SITS$j"),
                            Pair(cfg, cfgV),
                            Pair(exp, expV),
                            Pair(ori, oriV),
                            Pair(theme, themeTS),
                            Pair(process, ":p${p}"),
                            Pair(launchMode, "singleInstance")
                        )
                    }

                // 不透明

                for (j in 0 until extension.countNotTranslucentStandard)
                    "activity" {
                        attributes(
                            Pair(name, "$applicationID.${infix}P${p}NRNTS$j"),
                            Pair(cfg, cfgV),
                            Pair(exp, expV),
                            Pair(ori, oriV),
                            Pair(theme, themeNTS),
                            Pair(process, ":p${p}")
                        )
                    }
                for (j in 0 until extension.countNotTranslucentSingleTop)
                    "activity" {
                        attributes(
                            Pair(name, "$applicationID.${infix}P${p}STPNTS$j"),
                            Pair(cfg, cfgV),
                            Pair(exp, expV),
                            Pair(ori, oriV),
                            Pair(theme, themeNTS),
                            Pair(process, ":p${p}"),
                            Pair(launchMode, "singleTop")
                        )
                    }

                for (j in 0 until extension.countNotTranslucentSingleTask)
                    "activity" {
                        attributes(
                            Pair(name, "$applicationID.${infix}P${p}STNTS$j"),
                            Pair(cfg, cfgV),
                            Pair(exp, expV),
                            Pair(ori, oriV),
                            Pair(theme, themeNTS),
                            Pair(process, ":p${p}"),
                            Pair(launchMode, "singleTask")
                        )
                    }

                for (j in 0 until extension.countNotTranslucentSingleInstance)
                    "activity" {
                        attributes(
                            Pair(name, "$applicationID.${infix}P${p}SINTS$j"),
                            Pair(cfg, cfgV),
                            Pair(exp, expV),
                            Pair(ori, oriV),
                            Pair(theme, themeNTS),
                            Pair(process, ":p${p}"),
                            Pair(launchMode, "singleInstance")
                        )
                    }


                // TaskAffinity
                for (i in 0 until extension.countTask) {
                    for (j in 0 until extension.countTranslucentStandard)
                        "activity" {
                            attributes(
                                Pair(name, "${applicationID}.${infix}P${p}TA${i}NRTS${j}"),
                                Pair(cfg, cfgV),
                                Pair(exp, expV),
                                Pair(ori, oriV),
                                Pair(theme, themeTS),
                                Pair(process, ":p${p}"),
                                Pair(task, ":t$i")
                            )
                        }

                    for (j in 0 until extension.countTranslucentSingleTop)
                        "activity" {
                            attributes(
                                Pair(name, "${applicationID}.${infix}P${p}TA${i}STPTS${j}"),
                                Pair(cfg, cfgV),
                                Pair(exp, expV),
                                Pair(ori, oriV),
                                Pair(theme, themeTS),
                                Pair(process, ":p${p}"),
                                Pair(task, ":t$i"),
                                Pair(launchMode, "singleTop")
                            )
                        }

                    for (j in 0 until extension.countTranslucentSingleTask)
                        "activity" {
                            attributes(
                                Pair(name, "${applicationID}.${infix}P${p}TA${i}STTS${j}"),
                                Pair(cfg, cfgV),
                                Pair(exp, expV),
                                Pair(ori, oriV),
                                Pair(theme, themeTS),
                                Pair(process, ":p${p}"),
                                Pair(task, ":t$i"),
                                Pair(launchMode, "singleTask")
                            )
                        }

                    for (j in 0 until extension.countNotTranslucentStandard)
                        "activity" {
                            attributes(
                                Pair(name, "${applicationID}.${infix}P${p}TA${i}NRNTS${j}"),
                                Pair(cfg, cfgV),
                                Pair(exp, expV),
                                Pair(ori, oriV),
                                Pair(theme, themeNTS),
                                Pair(process, ":p${p}"),
                                Pair(task, ":t$i")
                            )
                        }
                    for (j in 0 until extension.countNotTranslucentSingleTop)
                        "activity" {
                            attributes(
                                Pair(name, "${applicationID}.${infix}P${p}TA${i}STPNTS${j}"),
                                Pair(cfg, cfgV),
                                Pair(exp, expV),
                                Pair(ori, oriV),
                                Pair(theme, themeNTS),
                                Pair(process, ":p${p}"),
                                Pair(task, ":t$i"),
                                Pair(launchMode, "singleTop")
                            )
                        }

                    for (j in 0 until extension.countNotTranslucentSingleTask)
                        "activity" {
                            attributes(
                                Pair(name, "${applicationID}.${infix}P${p}TA${i}STNTS${j}"),
                                Pair(cfg, cfgV),
                                Pair(exp, expV),
                                Pair(ori, oriV),
                                Pair(theme, themeNTS),
                                Pair(process, ":p${p}"),
                                Pair(task, ":t$i"),
                                Pair(launchMode, "singleTask")
                            )
                        }
                }

                "provider" {
                    attributes(
                        Pair(
                            name,
                            "com.qihoo360.replugin.component.provider.PluginPitProviderP${p}"
                        ),
                        Pair(authorities, "${applicationID}.Plugin.NP.${p}"),
                        Pair(process, ":p$p"),
                        Pair(exp, expV)
                    )
                }

                "provider" {
                    attributes(
                        Pair(
                            name,
                            "com.qihoo360.replugin.component.process.ProcessPitProviderP${p}"
                        ),
                        Pair(authorities, "${applicationID}.loader.p.mainN${100 - p}"),
                        Pair(process, ":p$p"),
                        Pair(exp, expV)
                    )
                }

                "service" {
                    attributes(
                        Pair(
                            name,
                            "com.qihoo360.replugin.component.service.server.PluginPitServiceP${p}"
                        ),
                        Pair(process, ":p$p"),
                        Pair(exp, expV)
                    )
                }

            }

        }
        // 删除 application 标签
        return xml.toString().replace("<application>", "").replace("</application>", "")
    }
}