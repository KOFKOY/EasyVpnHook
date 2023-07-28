package com.wsj.easyvpnhook;

import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class SdHook implements IXposedHookLoadPackage {
    private String packageName = "cn.ktidata.redappm.sd";
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (!packageName.equals(lpparam.packageName)) {
            return;
        }
        Log.d("数独:", "开始Hook");
        hook(lpparam);
    }

    private void hook(XC_LoadPackage.LoadPackageParam lpparam) {
        skipAd(lpparam);
    }

    private void skipAd(XC_LoadPackage.LoadPackageParam lpparam) {

    }
}
