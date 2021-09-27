package com.qihoo360.replugin;

import android.util.Log;

import com.qihoo360.replugin.utils.ReflectUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;

public class PluginRuntimeClassLoader extends ClassLoader {

    private List<PluginClassMapEntry> mEntries;

    static class PluginClassMapEntry {
        private ClassLoader mClassLoader;
        private Method mFindClass;
        private List<String> mClasses;

        public PluginClassMapEntry(ClassLoader loader, List<String> classes) {
            mClassLoader = loader;
            mClasses = classes;
            try {
                mFindClass = ReflectUtils.getMethod(loader, ClassLoader.class.getName(), "findClass", new Class[]{String.class});
            } catch (Throwable throwable) {
                // should no happen
            }
        }
    }

    public PluginRuntimeClassLoader(ClassLoader parent, List<PluginClassMapEntry> entries) throws NullPointerException, ClassNotFoundException, NoSuchMethodException {
        super(parent);
        mEntries = entries;
    }

    @Override
    public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class clazz = null;
        for (PluginClassMapEntry entry : mEntries) {
            for (String prefix : entry.mClasses) {
                if (name.startsWith(prefix)) {
                    Log.e("sheng", "PluginRuntimeClassLoader loadClass " + name + ", prefix " + prefix);
                    try {
                        clazz = (Class<?>) entry.mFindClass.invoke(entry.mClassLoader, name);
                    } catch (Throwable ignored) {
                        Log.e("sheng", ignored.getMessage(), ignored);
                    }
                    if (clazz != null) {
                        return clazz;
                    }
                }
            }
        }
        //Log.e("sheng", "PluginRuntimeClassLoader loadClass " + name);
        return super.loadClass(name, resolve);
    }

    @Override
    protected URL findResource(String name) {
        URL url;
        for (PluginClassMapEntry entry : mEntries) {
            url = entry.mClassLoader.getResource(name);
            if (url != null) {
                return url;
            }
        }
        return super.findResource(name);
    }

    @Override
    protected Enumeration<URL> findResources(String name) throws IOException {
        return super.findResources(name);
    }
}
