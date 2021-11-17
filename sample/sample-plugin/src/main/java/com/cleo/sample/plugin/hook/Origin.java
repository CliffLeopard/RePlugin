package com.cleo.sample.plugin.hook;

/**
 * author:gaoguanling
 * date:2021/11/17
 * time:14:47
 * email:gaoguanling@360.cn
 * link:
 */
public class Origin {

    // 方法调用处替换
    public void callVoidF(A a, B b) {
        System.out.println("CallOrigin:callVoidF");
    }

    public Res callF(A a, B b) {
        return new Res("CallOrigin:callF");
    }

    public static Res callStaticF(A a, B b) {
        return new Res("CallOrigin:callStaticF");
    }

    public static void callVoidStaticF(A a, B b) {
        new Res("CallOrigin:callVoidStaticF").println();
    }


    // 方法定义处替换
    public void definedVoidF(A a, B b) {
        System.out.println("CallOrigin:defineVoidF");
    }

    public Res definedF(A a, B b) {
        return new Res("CallOrigin:definedF");
    }

    public static Res definedStaticF(A a, B b) {
        return new Res("CallOrigin:definedStaticF");
    }

}
