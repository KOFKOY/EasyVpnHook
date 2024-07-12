package com.wsj.easyhook.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wsj.easyhook.IXposedHookAbstract;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Objects;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class BiuBiuHook extends IXposedHookAbstract {
    public BiuBiuHook(){
        packageName = "com.njh.biubiu";
        TAG = "biubiu加速器";
        version = 3;
        debug = true;
    }

    @Override
    public void hook(XC_LoadPackage.LoadPackageParam lpparam) {
        log("开始加速");
        hookVip(lpparam.classLoader);
    }

    private void hookVip(ClassLoader classLoader) {
//        Class<?> clzss = XposedHelpers.findClassIfExists("sd.a$a", classLoader);
//        Object a = XposedHelpers.getStaticObjectField(clzss, "a");
//
//        Class<?> clzssA = XposedHelpers.findClassIfExists("sd.a", classLoader);
//
//        Method c1 = XposedHelpers.findMethodBestMatch(clzssA, "c");
//        log("测试");
//        try {
//            Object invoke = c1.invoke(a);
//            log("打印userTag" + invoke);
//        } catch (IllegalAccessException e) {
//            log("报错1");
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            log("报错2");
//            e.printStackTrace();
//        }
        XposedHelpers.findAndHookMethod("sd.a", classLoader, "c", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        log("返回参数:" + param.getResult());
                    }
        });
    }
}
