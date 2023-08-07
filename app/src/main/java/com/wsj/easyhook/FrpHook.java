package com.wsj.easyhook;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * 打开FRP后自动打开adb
 */
public class FrpHook {

    private static final String TAG = "FRP软件: ";
    public static void hook(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedHelpers.findAndHookMethod("com.tools.frp.activities.MainActivity",lpparam.classLoader,"onCreate", Bundle.class,new XC_MethodHook(){

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                new Thread(()->{
                    adbStart(param);
                }).start();
            }
        });
    }

    private static void adbStart(XC_MethodHook.MethodHookParam param){
        try {
            // 执行adb tcpip命令
            Process exec = Runtime.getRuntime().exec("su -c setprop service.adb.tcp.port 5555");
            exec.waitFor();
            Process stop_adbd = Runtime.getRuntime().exec("su -c stop adbd");
            stop_adbd.waitFor();
            Process start_adbd = Runtime.getRuntime().exec("su -c start adbd");
            start_adbd.waitFor();
            log( "adb 开启成功 端口 5555");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void log(String str) {
        XposedBridge.log(TAG + str);
    }
}
