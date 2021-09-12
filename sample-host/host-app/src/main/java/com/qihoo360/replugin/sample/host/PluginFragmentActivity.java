package com.qihoo360.replugin.sample.host;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.qihoo360.replugin.RePlugin;

/**
 * 打开插件中的Fragment
 * <p>
 * 作者 coder
 * 创建时间 2017/7/6
 */

public class PluginFragmentActivity extends FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean isBuiltIn = true;
        String pluginName = isBuiltIn ? "demo1" : "com.qihoo360.replugin.sample.demo1";
        RePlugin.registerHookingClass("com.qihoo360.replugin.sample.demo1.fragment.DemoFragment", RePlugin.createComponentName(pluginName, "com.qihoo360.replugin.sample.demo1.fragment.DemoFragment"), null);
        ClassLoader d1ClassLoader = RePlugin.fetchClassLoader(pluginName);
        setContentView(R.layout.activity_plugin_fragment2);
        try {
            Class<?> demoFragmentClass = d1ClassLoader.loadClass("com.qihoo360.replugin.sample.demo1.fragment.DemoFragment");
            Fragment fragment = demoFragmentClass.asSubclass(Fragment.class).newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.container2, fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
