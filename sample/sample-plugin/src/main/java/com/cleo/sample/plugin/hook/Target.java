package com.cleo.sample.plugin.hook;

import com.qihoo360.replugin.base.hook.MethodInfo;

/**
 * author:gaoguanling
 * date:2021/11/17
 * time:14:47
 * email:gaoguanling@360.cn
 * link:
 */
public class Target {
    public static void callBeforeF(MethodInfo info, Object o, A a, B b) {
        new Res("Target:callBeforeF").println();
    }
    public static void callBeforeStaticF(MethodInfo info, A a, B b) {
        new Res("Target:callBeforeStaticF").println();
    }

    public static Res callAfterF(MethodInfo info, Object o, A a, B b, Res res) {
        res.println();
        return new Res("Target:callAfterF");
    }

    public static Res callAfterStaticF(MethodInfo info, A a, B b, Res res) {
        res.println();
        return new Res("Target:callAfterStaticF");
    }

    public static Res callReplaceF(MethodInfo info, Object o, A a, B b) {
        return new Res("Target:callReplaceF");
    }
    public static Res callReplaceStaticF(MethodInfo info, Object o, A a, B b) {
        return new Res("Target:callReplaceStaticF");
    }
}
