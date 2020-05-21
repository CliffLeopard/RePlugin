package com.qihoo360.replugin;

import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;

import com.qihoo360.loader2.IPlugin;
import com.qihoo360.replugin.model.PluginInfo;
import com.qihoo360.replugin.utils.ReflectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class Plugin extends IPlugin.Stub {

    public Plugin() {
        try {
            List<PluginRuntimeClassLoader.PluginClassMapEntry> entries = getDependencies();
            if (entries != null && entries.size() > 0) {
                // hook classloader
                ClassLoader cl = Entry.class.getClassLoader();
                ClassLoader runtime = new PluginRuntimeClassLoader(cl.getParent(), entries);
                ReflectUtils.setField(ClassLoader.class, cl, "parent", runtime);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public IBinder query(String name) throws RemoteException {
        return RePluginServiceManager.getInstance().getService(name);
    }

    protected abstract Map<String, Integer> getRequiredPlugin();

    protected abstract List<String> getPrefixClasses(String name);

    protected void onRequiredPluginNotInstalled(String name, int ver) {

    }

    protected List<PluginRuntimeClassLoader.PluginClassMapEntry> getDependencies() {
        List<PluginRuntimeClassLoader.PluginClassMapEntry> entries = new ArrayList<>();
        Map<String, Integer> plugins = getRequiredPlugin();
        if (plugins != null) {
            for (Map.Entry<String, Integer> entry: plugins.entrySet()) {
                String name = entry.getKey();
                if (!RePlugin.isPluginInstalled(name) || RePlugin.getPluginVersion(name) < entry.getValue()) {
                    onRequiredPluginNotInstalled(name, entry.getValue());
                }
                ClassLoader cl = RePlugin.fetchClassLoader(name);
                if (cl != null) {
                    entries.add(new PluginRuntimeClassLoader.PluginClassMapEntry(
                            cl,
                            getPrefixClasses(name)
                    ));
                }
            }
        }
        return entries;
    }
}
