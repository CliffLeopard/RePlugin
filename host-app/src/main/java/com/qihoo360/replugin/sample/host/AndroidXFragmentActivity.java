package com.qihoo360.replugin.sample.host;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

/**
 * author:CliffLeopard
 * date:4/26/21
 * time:2:35 PM
 * email:precipiceleopard@gmail.com
 * link:
 */
public class AndroidXFragmentActivity extends FragmentActivity {
    Fragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin_fragment2);
        try {
            fragment = getClassLoader().loadClass("com.qihoo360.replugin.sample.host.DemoFragment").asSubclass(Fragment.class).newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment, fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
