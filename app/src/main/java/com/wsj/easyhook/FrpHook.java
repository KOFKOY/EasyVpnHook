package com.wsj.easyhook;

import android.os.Bundle;

import java.io.IOException;

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
                super.afterHookedMethod(param);
                adbStart();
            }
        });
    }

    private static void adbStart(){
        try {
            Runtime.getRuntime().exec("su -c adb kill-server");
            Runtime.getRuntime().exec("su -c adb start-server");
            Process exec = Runtime.getRuntime().exec("su -c adb tcpip 5555");
            log("开启ADB"+(exec.isAlive()?"失败":"成功"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void log(String str) {
        XposedBridge.log(TAG + str);
    }
}
