package com.wsj.easyhook.app;

import android.app.Activity;
import android.content.Context;
import android.os.Message;
import com.wsj.easyhook.IXposedHookAbstract;
import java.util.ArrayList;
import java.util.List;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class YydqHook extends IXposedHookAbstract {
    public YydqHook(){
        packageName = "com.le123.ysdq";
        TAG = "影视大全";
        debug = false;
        version = 4;
    }

    @Override
    public void hook(XC_LoadPackage.LoadPackageParam lpparam) {
        List<Integer> adIdList = new ArrayList<>();
        adIdList.add(2131362947);
        adIdList.add(2131365980);
        adIdList.add(2131361963);
        adIdList.add(2131363179);
        adIdList.add(2131366735);
        adIdList.add(2131364497);
        adIdList.add(2131362731);
        adIdList.add(2131365005);

        adIdList.add(2131366627);
        adIdList.add(2131366626);
        adIdList.add(2131366628);
        adIdList.add(2131364523);
        adIdList.add(2131363244);
        adIdList.add(2131365186);
        adIdList.add(2131364514);//ll_attach_ad


        //360加固
        XposedHelpers.findAndHookMethod("com.stub.StubApp",lpparam.classLoader, "attachBaseContext", Context.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                ClassLoader loader = ((Context)param.args[0]).getClassLoader();
                hookVip(loader);
                initHideAd("android.widget.ImageView",loader,adIdList);
                initHideAd("android.widget.FrameLayout",loader,adIdList);
                initHideAd("android.widget.RelativeLayout", loader, adIdList);
                initHideAd("android.widget.TextView", loader, adIdList);
                initHideAd("android.widget.LinearLayout", loader, adIdList);
            }
        });
    }

    private void hookVip(ClassLoader loader) {
        log("调试开始");
        // PreferencesManager
        Class<?> pm = XposedHelpers.findClass("com.elinkway.infinitemovies.dao.k", loader);
        XposedHelpers.findAndHookMethod(pm, "k", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(true);
            }
        });
        XposedHelpers.findAndHookMethod(pm, "M", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(false);
            }
        });
        XposedHelpers.findAndHookMethod(pm, "v", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                log("step 5 修改返回成功:" + param.getResult());
                param.setResult(1);
            }
        });
        XposedHelpers.findAndHookMethod(pm, "s", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                log("step 6 过期时间："+param.getResult());
                param.setResult(System.currentTimeMillis() + 96400000);
            }
        });
        XposedHelpers.findAndHookMethod(pm, "B", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                log("step 7 new_user_free_ad_expire_tip："+param.getResult());
                param.setResult(true);
            }
        });
        XposedHelpers.findAndHookMethod(pm, "h", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                log("step 8 home_free_ad_login_close_time："+param.getResult());
            }
        });

        XposedHelpers.findAndHookMethod(pm, "t", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                log("step 9 可能值0 1 2   0不显示广告："+param.getResult());
            }
        });

        XposedHelpers.findAndHookMethod("com.elinkway.infinitemovies.ad.manager.InPlayAdManager",loader, "isLoadAd",String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                log("isLoadAd："+param.getResult());
                param.setResult(false);
            }
        });


        //UserSpManager
        Class<?> usm = XposedHelpers.findClass("com.elinkway.infinitemovies.utils.w2", loader);
        XposedHelpers.findAndHookMethod(usm, "A", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(true);
            }
        });

        //跳过播放视频广告
        //2131366628  TextView
        Class<?> attachAdManager = XposedHelpers.findClass("com.elinkway.infinitemovies.adManager.d", loader);
        Class<?> playMedia = XposedHelpers.findClass("com.elinkway.infinitemovies.play.core.PlayMediaController", loader);
        final Object[] attachAdManagerObject = {null};
        XposedHelpers.findAndHookConstructor(attachAdManager,playMedia, Activity.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                attachAdManagerObject[0] = param.thisObject;
            }
        });

        XposedHelpers.findAndHookMethod("com.elinkway.infinitemovies.adManager.d$c",loader, "handleMessage", Message.class,
        new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                if (attachAdManagerObject[0] != null) {
                    Object object = attachAdManagerObject[0];
                    XposedHelpers.setIntField(object, "n", 0);
                    XposedHelpers.setIntField(object, "s", 0);
                }
            }
        });
    }
}
