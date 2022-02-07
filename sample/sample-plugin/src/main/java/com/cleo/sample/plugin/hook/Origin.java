package com.cleo.sample.plugin.hook;

/**
 * author:gaoguanling
 * date:2021/11/17
 * time:14:47
 * email:gaoguanling@360.cn
 * link:
 * 方法调用处Hook
 */
public class Origin {
    // before
    public Res beforeF(A a, B b) {
        return new Res("OriginCall:beforeF");
    }

    public void beforeVoidF(A a, B b) {
        System.out.println("OriginCall:beforeVoidF");
    }

    public static Res beforeStaticF(A a, B b) {
        return new Res("OriginCall:beforeStaticF");
    }

    public static void beforeStaticVoidF(A a, B b) {
        new Res("OriginCall:beforeStaticVoidF").println();
    }


    // after
    public Res afterF(A a, B b) {
        return new Res("OriginCall:afterF");
    }

    public void afterVoidF(A a, B b) {
        System.out.println("OriginCall:afterVoidF");
    }

    public static Res afterStaticF(A a, B b) {
        return new Res("OriginCall:afterStaticF");
    }

    public static void afterStaticVoidF(A a, B b) {
        new Res("OriginCall:afterStaticVoidF").println();
    }

    // replace

    public Res replaceF(A a, B b) {
        return new Res("OriginCall:replaceF");
    }

    public void replaceVoidF(A a, B b) {
        System.out.println("OriginCall:replaceVoidF");
    }

    public static Res replaceStaticF(A a, B b) {
        return new Res("OriginCall:replaceStaticF");
    }

    public static void replaceStaticVoidF(A a, B b) {
        new Res("OriginCall:replaceStaticVoidF").println();
    }
}
