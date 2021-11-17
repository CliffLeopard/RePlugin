package com.cleo.sample.plugin.hook;

/**
 * author:gaoguanling
 * date:2021/11/17
 * time:14:48
 * email:gaoguanling@360.cn
 * link:
 */
public class Res {
    private String tag;

    public Res(String tag) {
        this.tag = tag;
    }

    public void println() {
        System.out.println(tag);
    }

    public String toString() {
        return tag;
    }
}
