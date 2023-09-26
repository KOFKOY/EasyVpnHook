package com.wsj.easyhook.app;
import com.wsj.easyhook.IXposedHookAbstract;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class GsHook extends IXposedHookAbstract {
    public GsHook() {
        packageName = "com.GSHY.xiaolei.zishuo";
        TAG = "怪兽画质";
        debug = true;
    }

    @Override
    public void hook(XC_LoadPackage.LoadPackageParam lpparam) {
        hookVip(lpparam.classLoader);
    }

    private void hookVip(ClassLoader loader) {
        XposedHelpers.findAndHookMethod("com.GSHY.webApi.WebOpen", loader, "getIsVip", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(true);
            }
        });
        XposedHelpers.findAndHookMethod("com.GSHY.webApi.WebOpen$Info", loader, "isVip", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(true);
            }
        });
        XposedHelpers.findAndHookMethod("com.GSHY.pay.PayUtils", loader, "isPay", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(true);
            }
        });
    }
}
