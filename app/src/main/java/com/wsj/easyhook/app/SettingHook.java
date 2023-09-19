package com.wsj.easyhook.app;

import android.content.Context;

import com.wsj.easyhook.IXposedHookAbstract;
import com.wsj.easyhook.util.HookUitl;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class SettingHook extends IXposedHookAbstract {
    public SettingHook() {
        packageName = "com.android.settings";
        TAG = "小米设置";
        version = 5;
    }

    @Override
    public void hook(XC_LoadPackage.LoadPackageParam lpparam) {
        hookPassword(lpparam.classLoader);
    }

    /**
     * 在小米设置里，管理已保存网络，单击网络，复制此网络密码到剪切板
     *
     * @param classLoader
     */
    private void hookPassword(ClassLoader classLoader) {
        Class<?> preferenceScreen = XposedHelpers.findClass("androidx.preference.PreferenceScreen", classLoader);
        Class<?> preference = XposedHelpers.findClass("androidx.preference.Preference", classLoader);
        XposedHelpers.findAndHookMethod("com.android.settings.wifi.MiuiSavedAccessPointsWifiSettings", classLoader, "onPreferenceTreeClick", preferenceScreen, preference, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                Object arg = param.args[1];
                if (arg.getClass().getSimpleName().equals("SavedAccessPointPreference")) {
                    Object accessPoint = XposedHelpers.callMethod(arg, "getAccessPoint");
                    Object getConfig = XposedHelpers.callMethod(accessPoint, "getConfig");
                    Object getWifiManager = XposedHelpers.callMethod(accessPoint, "getWifiManager");
                    Class<?> WifiDppUtilsClass = XposedHelpers.findClass("com.android.settings.wifi.dpp.WifiDppUtils", classLoader);
                    String sharedKey = (String) XposedHelpers.callStaticMethod(WifiDppUtilsClass, "getPresharedKey", getWifiManager, getConfig);
                    if (sharedKey.length() > 2) {
                        //去除引号
                        sharedKey = sharedKey.substring(1, sharedKey.length() - 1);
                    }
                    Context context = (Context) XposedHelpers.callMethod(param.thisObject, "getPrefContext");
                    HookUitl.copyToClipboard(context, sharedKey);
                }

            }
        });
    }
}
