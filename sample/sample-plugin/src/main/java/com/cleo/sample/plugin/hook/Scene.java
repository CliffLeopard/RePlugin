package com.cleo.sample.plugin.hook;

/**
 * author:gaoguanling
 * date:2021/11/17
 * time:14:48
 * email:gaoguanling@360.cn
 * link:
 */
public class Scene {
    public static void callBefore() {
        Origin origin = new Origin();
        origin.beforeF(new A(), new B()).println();
    }

    public static void callBeforeVoid() {
        Origin origin = new Origin();
        origin.beforeVoidF(new A(), new B());
    }

    public static void callBeforeStatic() {
        Origin.beforeStaticF(new A(), new B()).println();
    }

    public static void callBeforeStaticVoid() {
        Origin.beforeStaticVoidF(new A(), new B());
    }

    public static void callAfter() {
        Origin origin = new Origin();
        origin.afterF(new A(), new B()).println();
    }

    public static void callAfterVoid() {
        Origin origin = new Origin();
        origin.afterVoidF(new A(), new B());
    }

    public static void callAfterStatic() {
        Origin.afterStaticF(new A(), new B()).println();
    }

    public static void callAfterStaticVoid() {
        Origin.afterStaticVoidF(new A(), new B());
    }

    public static void callReplace() {
        Origin origin = new Origin();
        origin.replaceF(new A(), new B()).println();
    }

    public static void callReplaceVoid() {
        Origin origin = new Origin();
        origin.replaceVoidF(new A(), new B());
    }

    public static void callReplaceStatic() {
        Origin.replaceStaticF(new A(), new B()).println();
    }

    public static void callReplaceStaticVoid() {
        Origin.replaceStaticVoidF(new A(), new B());
    }


    public static void defineBefore() {
        OriginDefine origin = new OriginDefine();
        origin.beforeF(new A(), new B()).println();
    }

    public static void defineBeforeVoid() {
        OriginDefine origin = new OriginDefine();
        origin.beforeVoidF(new A(), new B());
    }

    public static void defineBeforeStatic() {
        OriginDefine.beforeStaticF(new A(), new B()).println();
    }

    public static void defineBeforeStaticVoid() {
        OriginDefine.beforeStaticVoidF(new A(), new B());
    }


    public static void defineAfter() {
        OriginDefine origin = new OriginDefine();
        origin.afterF(new A(), new B()).println();
    }

    public static void defineAfterVoid() {
        OriginDefine origin = new OriginDefine();
        origin.afterVoidF(new A(), new B());
    }

    public static void defineAfterStatic() {
        OriginDefine.afterStaticF(new A(), new B()).println();
    }

    public static void defineAfterStaticVoid() {
        OriginDefine.afterStaticVoidF(new A(), new B());
    }

    public static void defineReplace() {
        OriginDefine origin = new OriginDefine();
        origin.replaceF(new A(), new B()).println();
    }

    public static void defineReplaceVoid() {
        OriginDefine origin = new OriginDefine();
        origin.replaceVoidF(new A(), new B());
    }

    public static void defineReplaceStatic() {
        OriginDefine.replaceStaticF(new A(), new B()).println();
    }

    public static void defineReplaceStaticVoid() {
        OriginDefine.replaceStaticVoidF(new A(), new B());
    }
}
