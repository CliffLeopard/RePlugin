package com.qihoo360.replugin.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.qihoo360.loader2.PMF;

import java.util.concurrent.Callable;

/**
 * author:CliffLeopard
 * date:4/29/21
 * time:5:19 PM
 * email:precipiceleopard@gmail.com
 * link:
 */
public class LocalBroadcastHelper {

    /**
     * 和LocalBroadcastManager.sendBroadcastSync类似，唯一的区别是执行所在线程：前者只在调用所在线程，本方法则在UI线程。
     * <p>
     * 可防止onReceiver在其它线程中被调用到。特别适用于AIDL、没有Looper的线程中调用此方法。
     *
     * @param intent 要发送的Intent信息
     * @see LocalBroadcastManager#sendBroadcastSync(Intent)
     */
    public static void sendBroadcastSyncUi(final Context context, final Intent intent) {
        try {
            ThreadUtils.syncToMainThread(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    LocalBroadcastManager.getInstance(context).sendBroadcastSync(intent);
                    return null;
                }
            }, 10000);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public static void registerReceiver(BroadcastReceiver receiver, IntentFilter intentFilter) {
        LocalBroadcastManager.getInstance(PMF.getApplicationContext()).registerReceiver(receiver, intentFilter);
    }

    public static void registerReceiver(Context context, BroadcastReceiver receiver, IntentFilter intentFilter) {
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, intentFilter);
    }

    public static void sendBroadcast(Context context, Intent intent) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
