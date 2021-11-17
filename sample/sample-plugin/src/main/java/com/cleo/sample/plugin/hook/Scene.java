package com.cleo.sample.plugin.hook;

/**
 * author:gaoguanling
 * date:2021/11/17
 * time:14:48
 * email:gaoguanling@360.cn
 * link:
 */
public class Scene {
    public static void callScene() {
        Origin origin = new Origin();
        origin.callF(new A(), new B()).println();
    }

    public static void callVoidScene() {
        Origin origin = new Origin();
        origin.callVoidF(new A(), new B());
    }

    public static void callStaticScene() {
        Origin.callStaticF(new A(), new B()).println();
    }

    public static void callStaticVoidScene() {
        Origin.callVoidStaticF(new A(), new B());
    }

    public static void defineScene() {
        Origin origin = new Origin();
        origin.definedF(new A(), new B()).println();
    }

    public static void defineVoidScene() {
        Origin origin = new Origin();
        origin.definedVoidF(new A(), new B());
    }

    public static void defineStaticScene() {
        Origin.definedStaticF(new A(), new B()).println();
    }
}
