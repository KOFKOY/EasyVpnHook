package com.wsj.easyhook;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainHook implements IXposedHookLoadPackage {

    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedBridge.log("包名:"+lpparam.packageName);
        switch (lpparam.packageName) {
            case "com.sangfor.vpn.client.phone":
                EvHook.hook(lpparam);
                break;
            case "com.tools.frp":
                FrpHook.hook(lpparam);
                break;
            case "cn.ktidata.redappm.sd":
                SdHook.hook(lpparam);
                break;
            case "com.dragon.read":
                FanQieHook.hook(lpparam);
                break;
        }
    }
}