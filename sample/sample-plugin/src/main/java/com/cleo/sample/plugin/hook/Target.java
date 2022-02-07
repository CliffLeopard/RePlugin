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

    public static void callBeforeVoidF(MethodInfo info, Object o, A a, B b) {
        new Res("Target:callBeforeVoidF").println();
    }

    public static void callBeforeStaticF(MethodInfo info, A a, B b) {
        new Res("Target:callBeforeStaticF").println();
    }

    public static void callBeforeStaticVoidF(MethodInfo info, A a, B b) {
        new Res("Target:callBeforeStaticVoidF").println();
    }

    public static Res callAfterF(MethodInfo info, Object o, A a, B b, Res res) {
        res.println();
        return new Res("Target:callAfterF");
    }

    public static void callAfterVoidF(MethodInfo info, Object o, A a, B b) {
        new Res("Target:callAfterVoidF").println();
    }

    public static Res callAfterStaticF(MethodInfo info, A a, B b, Res res) {
        res.println();
        return new Res("Target:callAfterStaticF");
    }

    public static void callAfterStaticVoidF(MethodInfo info, A a, B b) {
        new Res("Target:callAfterStaticVoidF").println();
    }

    public static Res callReplaceF(MethodInfo info, Object o, A a, B b) {
        return new Res("Target:callReplaceF");
    }

    public static void callReplaceVoidF(MethodInfo info, Object o, A a, B b) {
        new Res("Target:callReplaceVoidF").println();
    }

    public static Res callReplaceStaticF(MethodInfo info, A a, B b) {
        return new Res("Target:callReplaceStaticF");
    }

    public static void callReplaceStaticVoidF(MethodInfo info, A a, B b) {
        new Res("Target:callReplaceStaticVoidF").println();
    }


    public static void defineBeforeF(MethodInfo info, Object o, A a, B b) {
        new Res("Target:defineBeforeF").println();
    }

    public static void defineBeforeVoidF(MethodInfo info, Object o, A a, B b) {
        new Res("Target:defineBeforeVoidF").println();
    }

    public static void defineBeforeStaticF(MethodInfo info, A a, B b) {
        new Res("Target:defineBeforeStaticF").println();
    }

    public static void defineBeforeStaticVoidF(MethodInfo info, A a, B b) {
        new Res("Target:defineBeforeStaticVoidF").println();
    }

    public static Res defineAfterF(MethodInfo info, Object o, A a, B b, Res res) {
        res.println();
        return new Res("Target:defineAfterF");
    }

    public static void defineAfterVoidF(MethodInfo info, Object o, A a, B b) {
        new Res("Target:defineAfterVoidF").println();
    }

    public static Res defineAfterStaticF(MethodInfo info, A a, B b, Res res) {
        res.println();
        return new Res("Target:defineAfterStaticF");
    }

    public static void defineAfterStaticVoidF(MethodInfo info, A a, B b) {
        new Res("Target:defineAfterStaticVoidF").println();
    }

    public static Res defineReplaceF(MethodInfo info, Object o, A a, B b) {
        return new Res("Target:defineReplaceF");
    }

    public static void defineReplaceVoidF(MethodInfo info, Object o, A a, B b) {
        new Res("Target:defineReplaceVoidF").println();
    }

    public static Res defineReplaceStaticF(MethodInfo info, A a, B b) {
        return new Res("Target:defineReplaceStaticF");
    }

    public static void defineReplaceStaticVoidF(MethodInfo info, A a, B b) {
        new Res("Target:defineReplaceStaticVoidF").println();
    }


}
