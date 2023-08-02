package com.wsj.easyhook;

import android.content.Context;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class FanQieHook {
    public static void hook(XC_LoadPackage.LoadPackageParam lpparam){
        hookVip(lpparam);
        hookUpdate(lpparam);
        hookKillAd(lpparam);
        hookPoplive(lpparam);
        hookLuckyDog(lpparam);
    }

    private static void hookVip(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedBridge.log("番茄hook vip");
        XposedHelpers.findAndHookConstructor("com.dragon.read.user.model.VipInfoModel",lpparam.classLoader,String.class,String.class,String.class,boolean.class,boolean.class,int.class,new XC_MethodHook(){
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Object[] args = param.args;
                String expireTime = (String) args[0];
                String isVip = (String) args[1];
                String leftTime = (String) args[2];
                boolean isAutoCharge = (boolean) args[3];
                boolean isUnionVip = (boolean) args[4];
                int union_source = (int) args[5];
                XposedBridge.log("expireTime:" +expireTime);
                XposedBridge.log("isVip:" +isVip);
                XposedBridge.log("leftTime:" +leftTime);
                XposedBridge.log("isAutoCharge:" +isAutoCharge);
                XposedBridge.log("isUnionVip:" +isUnionVip);
                XposedBridge.log("union_source:" +union_source);
                args[0] = "4102415999";
                args[1] = "1";
                args[2] = "10000";
                args[3] = true;
                args[4] = true;
                args[5] = 1;
            }
        });
    }

    private static void hookUpdate(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedBridge.log("番茄hook update");
        XposedHelpers.findAndHookMethod("com.ss.android.update.ad", lpparam.classLoader, "k", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(false);
            }
        });
    }

    private static void hookKillAd(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedBridge.log("番茄hook kill ad");
        XposedHelpers.findAndHookMethod("com.dragon.read.user.h", lpparam.classLoader, "e", String.class,new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(true);
            }
        });
        XposedHelpers.findAndHookMethod("com.dragon.read.base.ad.a", lpparam.classLoader, "a", java.lang.String.class, java.lang.String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                if (param.args[0].toString().contains("_ad")) {
                    param.setResult(false);
                }
            }
        });
    }

    private  static void hookPoplive(XC_LoadPackage.LoadPackageParam lpparam) {
//        XposedHelpers.findAndHookMethod("com.dragon.read.component.audio.impl.ui.b.a", lpparam.classLoader, "a",
//                Context.class, String.class, String.class, new XC_MethodHook() {
//                    @Override
//                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                        param.setResult(null);
//                    }
//        });
    }
    private static void hookLuckyDog(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedBridge.log("番茄hook dog");
        XposedHelpers.findAndHookMethod("com.dragon.read.polaris.d", lpparam.classLoader, "b", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(false);
            }
        });
    }

}
