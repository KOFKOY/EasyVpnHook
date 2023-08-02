package com.wsj.easyhook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.lang.reflect.Method;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * 数独去广告
 */
public class SdHook{
    public static void hook(XC_LoadPackage.LoadPackageParam lpparam) {
        skipAd(lpparam);
    }
    private static void skipAd(XC_LoadPackage.LoadPackageParam lpparam) {
        Class<?> splashActivity = XposedHelpers.findClassIfExists("com.study.learningsudoku.feiniuad.SplashActivity", lpparam.classLoader);
        XposedHelpers.findAndHookMethod(splashActivity,"onCreate", Bundle.class, new XC_MethodReplacement(){
            @Override
            protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.invokeOriginalMethod(param.method, param.thisObject, param.args);
                Method finish = XposedHelpers.findMethodBestMatch(Activity.class, "finish");
                finish.invoke(param.thisObject);
                return null;
            }
        });

        XposedHelpers.findAndHookMethod(Activity.class, "startActivity", Intent.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param){
                Intent originalIntent = (Intent) param.args[0];
                String originalClassName = originalIntent.getComponent().getClassName();
                if (originalClassName.equals("com.study.learningsudoku.feiniuad.RewardVideoActivity")) {
                    Intent newIntent = new Intent();
                    newIntent.setClassName(originalIntent.getComponent().getPackageName(), "com.study.learningsudoku.studyhelper.StudyHelperMainActivity");
                    param.args[0] = newIntent;
                }
            }
        });
    }
}
