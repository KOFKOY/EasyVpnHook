package com.wsj.easyhook.app;

import android.annotation.SuppressLint;

import com.wsj.easyhook.IXposedHookAbstract;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class XmlaHook extends IXposedHookAbstract {

    public XmlaHook() {
        packageName = "com.ximalaya.ting.lite";
        TAG = "喜马拉雅";
        debug = true;
        version = 1;
    }

    @Override
    public void hook(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedHelpers.findAndHookMethod("com.ximalaya.ting.android.host.model.album.AlbumM", lpparam.classLoader, "isSkipHeadTail", new XC_MethodHook() {
            @SuppressLint("ResourceType")
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                log("wsj 喜马拉雅hook");
                param.setResult(true);
            }
        });
    }
}
