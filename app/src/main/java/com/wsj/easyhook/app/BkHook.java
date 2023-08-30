package com.wsj.easyhook.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wsj.easyhook.IXposedHookAbstract;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class BkHook extends IXposedHookAbstract {
    int page = 0;
    public BkHook(){
        packageName = "com.picacomic.fregata";
        TAG = "哔咔";
    }

    @Override
    public void hook(XC_LoadPackage.LoadPackageParam lpparam) {
        page = 0;
        hookVip(lpparam.classLoader);
    }

    private void hookVip(ClassLoader classLoader) {
        //去除右下角商品推荐
        XposedHelpers.findAndHookConstructor(CountDownTimer.class, Long.TYPE, Long.TYPE, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                param.args[0] = 1000L;
                param.args[1] = 1500L;
            }
        });

        XposedHelpers.findAndHookMethod("com.picacomic.fregata.fragments.HomeFragment", classLoader, "onCreateView",
                LayoutInflater.class, ViewGroup.class, Bundle.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        // 获取传入的 View 参数
                        View rootView = (View) param.getResult();
                        // 获取指定 ID 的 View
                        @SuppressLint("ResourceType")
                        View specificView = rootView.findViewById(2131297054);
                        if (specificView != null) {
                            // 在这里对获取到的 View 进行操作
                            specificView.setVisibility(View.GONE);
                        }
                    }
        });

        XposedHelpers.findAndHookMethod("com.picacomic.fregata.activities.MainActivity",classLoader,"u",Integer.TYPE,new XC_MethodHook(){
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Object arg = param.args[0];
                int i = Integer.parseInt(arg.toString());
                if (i == 2) {
                    param.args[0] = 1;
                }
            }
        });
        XposedHelpers.findAndHookMethod("com.picacomic.fregata.activities.MainActivity", classLoader, "G",String.class, new XC_MethodReplacement() {
            @Override
            protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                return null;
            }
        });
        XposedHelpers.findAndHookMethod("com.picacomic.fregata.activities.MainActivity", classLoader, "bW",String.class, new XC_MethodReplacement() {
            @Override
            protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                return null;
            }
        });
        XposedHelpers.findAndHookMethod("com.picacomic.fregata.activities.MainActivity", classLoader, "F",String.class, new XC_MethodReplacement() {
            @Override
            protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                return null;
            }
        });

        XposedHelpers.findAndHookMethod(
                "com.picacomic.fregata.utils.views.AlertDialogCenter",
                classLoader,
                "showAnnouncementAlertDialog",
                Context.class,
                String.class,
                String.class,
                String.class,
                String.class,
                View.OnClickListener.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        param.setResult(null);
                    }
                }
        );

        XposedHelpers.findAndHookMethod(
                "com.picacomic.fregata.adapters.ComicPageRecyclerViewAdapter",
                classLoader,
                "onCreateViewHolder",
                ViewGroup.class,
                Integer.TYPE,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        Integer i = (Integer)param.args[1];
                        if (i == 2) {
                            param.args[1] = page;
                        }
                    }
                }
        );

        XposedHelpers.findAndHookMethod(
                "com.picacomic.fregata.adapters.a",
                classLoader,
                "getItemViewType",
                Integer.TYPE,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        param.setResult(0);
                    }
                }
        );
    }
}
