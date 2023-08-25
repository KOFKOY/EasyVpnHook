package com.wsj.easyhook.app;

import android.content.Context;

import com.wsj.easyhook.IXposedHookAbstract;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class ZqHook extends IXposedHookAbstract {

    public ZqHook(){
        packageName = "com.zhengnengliang.precepts";
        TAG = "正气";
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

    private void hookVip(ClassLoader classLoader) {
        XposedHelpers.findAndHookMethod("com.zhengnengliang.precepts.advert.ActivityAdvert", classLoader, "loadTreeAd2Show", Boolean.TYPE,new XC_MethodReplacement() {
            @Override
            protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                return null;
            }
        });
        XposedHelpers.findAndHookMethod("com.zhengnengliang.precepts.advert.ActivityAdvert", classLoader, "loadAd2Show", Boolean.TYPE,new XC_MethodReplacement() {
            @Override
            protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                return null;
            }
        });
        XposedHelpers.findAndHookMethod("com.zhengnengliang.precepts.advert.ActivityAdvert", classLoader, "requestGoodsAd",new XC_MethodReplacement() {
            @Override
            protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                return null;
            }
        });
        //去除右下角商品推荐
        XposedHelpers.findAndHookMethod("com.zhengnengliang.precepts.ecommerce.FloatingMall", classLoader, "refresh",new XC_MethodReplacement() {
            @Override
            protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                return null;
            }
        });

        XposedHelpers.findAndHookMethod("com.zhengnengliang.precepts.manager.user.UserShowInfo",classLoader,"isVIP",new XC_MethodHook(){
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(true);
            }
        });
        XposedHelpers.findAndHookMethod("com.zhengnengliang.precepts.manager.user.UserShowInfo",classLoader,"isAdBlock",new XC_MethodHook(){
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(true);
            }
        });
        XposedHelpers.findAndHookMethod("com.zhengnengliang.precepts.manager.user.UserShowInfo",classLoader,"getUserIpLocation",new XC_MethodHook(){
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult("来自 无间地狱");
            }
        });

        XposedHelpers.findAndHookMethod("com.zhengnengliang.precepts.advert.AdvertManager",classLoader,"isShowAd",new XC_MethodHook(){
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(false);
            }
        });

        Class<?> zqVipInfo = XposedHelpers.findClass("com.zhengnengliang.precepts.bean.ZqVipInfo", classLoader);
        XposedHelpers.findAndHookMethod("com.zhengnengliang.precepts.vip.ActivityVIP",classLoader,"updateVipUI",zqVipInfo,new XC_MethodHook(){
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Object arg = param.args[0];
                if (arg == null) {
                    arg = zqVipInfo.newInstance();
                }
                XposedHelpers.setObjectField(arg, "isVip", "on");
                XposedHelpers.setIntField(arg, "days", 97);
                XposedHelpers.setObjectField(arg, "expire", "永远不会");
                param.args[0] = arg;
            }
        });

        XposedHelpers.findAndHookMethod("com.zhengnengliang.precepts.manager.login.LoginManager", classLoader,"isVIP",  new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(true);
            }
        });
        XposedHelpers.findAndHookMethod("com.zhengnengliang.precepts.manager.login.LoginManager", classLoader,"isAdBlock",  new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(true);
            }
        });

        XposedHelpers.findAndHookMethod("com.zhengnengliang.precepts.manager.login.LoginInfo", classLoader,"isVIP",  new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(true);
            }
        });
        XposedHelpers.findAndHookMethod("com.zhengnengliang.precepts.manager.login.LoginInfo", classLoader,"isAdBlock",  new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(true);
            }
        });
    }
}
