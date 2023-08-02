package com.wsj.easyhook;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainHook implements IXposedHookLoadPackage {

    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if (lpparam.packageName.equals("com.sangfor.vpn.client.phone")) {
            EvHook.hook(lpparam);
        } else if (lpparam.packageName.equals("com.tools.frp")) {
            FrpHook.hook(lpparam);
        } else if (lpparam.packageName.equals("cn.ktidata.redappm.sd")) {
            SdHook.hook(lpparam);
        } else if (lpparam.packageName.equals("com.dragon.read")) {
            FanQieHook.hook(lpparam);
        }
    }
}