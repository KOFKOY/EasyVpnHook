package com.wsj.easyhook.app;

import android.content.Context;

import com.wsj.easyhook.IXposedHookAbstract;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class F555Hook extends IXposedHookAbstract {
    public F555Hook(){
        packageName = "com.qiqi.hhvideo";
        TAG = "影视555";
    }

    @Override
    public void hook(XC_LoadPackage.LoadPackageParam lpparam) {
        //360加固
        XposedHelpers.findAndHookMethod("com.stub.StubApp",lpparam.classLoader, "attachBaseContext", Context.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                ClassLoader loader = ((Context)param.args[0]).getClassLoader();
                hookVip(loader);
            }
        });
    }

    private void hookVip(ClassLoader loader) {
        XposedHelpers.findAndHookMethod("com.qiqi.hhvideo.ui.SplashActivity2", loader, "z0", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(0);
            }
        });
        XposedHelpers.findAndHookMethod("com.qiqi.hhvideo.ui.home.MoiveListFragment", loader, "G", new XC_MethodReplacement() {
            @Override
            protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                return null;
            }
        });
    }
}
