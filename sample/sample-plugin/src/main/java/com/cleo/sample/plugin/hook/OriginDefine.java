package com.cleo.sample.plugin.hook;

/**
 * author:gaoguanling
 * date:2021/11/18
 * time:14:18
 * email:gaoguanling@360.cn
 * link:
 * 方法定义处Hook
 */
class OriginDefine {
    // before
    public Res beforeF(A a, B b) {
        return new Res("OriginDefine:beforeF");
    }

    public void beforeVoidF(A a, B b) {
        System.out.println("OriginDefine:beforeVoidF");
    }

    public static Res beforeStaticF(A a, B b) {
        return new Res("OriginDefine:beforeStaticF");
    }

    public static void beforeStaticVoidF(A a, B b) {
        new Res("OriginDefine:beforeStaticVoidF").println();
    }


    // after
    public Res afterF(A a, B b) {
        return new Res("OriginDefine:afterF");
    }

    public void afterVoidF(A a, B b) {
        System.out.println("OriginDefine:afterVoidF");
    }

    public static Res afterStaticF(A a, B b) {
        return new Res("OriginDefine:afterStaticF");
    }

    public static void afterStaticVoidF(A a, B b) {
        new Res("OriginDefine:afterStaticVoidF").println();
    }

    // replace

    public Res replaceF(A a, B b) {
        return new Res("OriginDefine:replaceF");
    }

    public void replaceVoidF(A a, B b) {
        System.out.println("OriginDefine:replaceVoidF");
    }

    public static Res replaceStaticF(A a, B b) {
        return new Res("OriginDefine:replaceStaticF");
    }

    public static void replaceStaticVoidF(A a, B b) {
        new Res("OriginDefine:replaceStaticVoidF").println();
    }
}
