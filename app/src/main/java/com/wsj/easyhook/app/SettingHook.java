package com.wsj.easyhook.app;

import android.content.Context;
import android.os.Bundle;

import com.wsj.easyhook.IXposedHookAbstract;
import com.wsj.easyhook.util.HookUitl;

import java.io.File;
import java.io.PrintWriter;
import java.util.Random;

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
        hookMac(lpparam.classLoader);
        hookPassword(lpparam.classLoader);
    }

    private void hookMac(ClassLoader classLoader) {
        XposedHelpers.findAndHookMethod("com.android.settings.MiuiSettings", classLoader, "onCreate", Bundle.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                new Thread(()->{
                    changeMac();
                }).start();
            }
        });
    }
    private void changeMac() {
        try {
            String mac = generateRandomMacAddress();
            // 创建一个临时shell脚本
            File shellScript = File.createTempFile("script", null);
            PrintWriter writer = new PrintWriter(shellScript);

            // 写入命令
            writer.println("adb shell su");
            writer.println("su -c ifconfig wlan0 down");
            writer.println("su -c ifconfig wlan0 hw ether " + mac);
            writer.println("su -c ifconfig wlan0 up");

            writer.close();

            // 执行shell脚本
            ProcessBuilder processBuilder = new ProcessBuilder("sh", shellScript.toString());
            Process process = processBuilder.start();
            process.waitFor();

            log("修改MAC地址成功 " + mac);

            // 删除临时shell脚本
            shellScript.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String generateRandomMacAddress() {
        Random random = new Random();
        byte[] macAddressBytes = new byte[6];

        // 设置MAC地址的第一个字节为02，表示本地分配的MAC地址
        macAddressBytes[0] = (byte) 0x02;

        // 生成随机的剩余5个字节
        random.nextBytes(macAddressBytes);

        // 确保第一个字节的最低有效位为0（单播地址）
        //转成二进制 最后一位是0  就是单播地址   最后一位是1  多播地址
        macAddressBytes[0] = (byte) (macAddressBytes[0] & (byte) 254);  // 254的二进制表示是11111110

        // 将生成的字节数组转换为十六进制字符串
        StringBuilder macAddressBuilder = new StringBuilder(17);
        for (int i = 0; i < 6; i++) {
            if (i != 0) {
                macAddressBuilder.append(':');
            }
            macAddressBuilder.append(String.format("%02x", macAddressBytes[i]));
        }

        return macAddressBuilder.toString();
    }

    /**
     * 在小米设置里，管理已保存网络，单击网络，复制此网络密码到剪切板
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
