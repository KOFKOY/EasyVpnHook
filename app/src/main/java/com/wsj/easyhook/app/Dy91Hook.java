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

public class Dy91Hook extends IXposedHookAbstract {
    public Dy91Hook(){
        packageName = "com.qnmd.a91xj.lk04tt";
        TAG = "香蕉";
    }

    @Override
    public void hook(XC_LoadPackage.LoadPackageParam lpparam) {
        hookVip(lpparam.classLoader);
    }

    private void hookVip(ClassLoader classLoader) {
        log("修改 08");
        Class < ?> userBean = XposedHelpers.findClass("com.aiqiyi.youtube.play.bean.response.UserInfoBean", classLoader);


        XposedHelpers.findAndHookMethod("com.aiqiyi.youtube.play.bean.response.VipInfoBean$GroupsBean", classLoader, "getId", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        log("group id: " + param.getResult());
                    }
        });
        XposedHelpers.findAndHookMethod("com.aiqiyi.youtube.play.MyApp", classLoader, "g",userBean,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        Object user = param.args[0];
                        XposedHelpers.setObjectField(user,"is_vip","y");
                        XposedHelpers.setObjectField(user,"level","6");
                        XposedHelpers.setObjectField(user,"is_unlimit","y");
                        XposedHelpers.setObjectField(user,"group_id","21");

        }});
        XposedHelpers.findAndHookMethod("b.b.a.a.a.b.a", classLoader, "b",userBean,
                 new XC_MethodHook() {
                     @Override
                     protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                         Object user = param.args[0];
                         Object is_vip = XposedHelpers.getObjectField(user, "is_vip");
                         log("反射is vip: " + is_vip);
                         Object account_img = XposedHelpers.getObjectField(user, "account_img");
                         log("反射is account_img: " + account_img);
                         Object balance = XposedHelpers.getObjectField(user, "balance");
                         log("反射is balance: " + balance);
                         Object can_cache_tips = XposedHelpers.getObjectField(user, "can_cache_tips");
                         log("反射is can_cache_tips: " + can_cache_tips);
                         Object can_play_tips = XposedHelpers.getObjectField(user, "can_play_tips");
                         log("反射is can_play_tips: " + can_play_tips);
                         Object channel_name = XposedHelpers.getObjectField(user, "channel_name");
                         log("反射is channel_name: " + channel_name);
                         Object device_type = XposedHelpers.getObjectField(user, "device_type");
                         log("反射is device_type: " + device_type);
                         Object follow = XposedHelpers.getObjectField(user, "follow");
                         log("反射is follow: " + follow);
                         Object game_balance = XposedHelpers.getObjectField(user, "game_balance");
                         log("反射is game_balance: " + game_balance);
                         Object group_end_time = XposedHelpers.getObjectField(user, "group_end_time");
                         log("反射is group_end_time: " + group_end_time);
                         Object group_id = XposedHelpers.getObjectField(user, "group_id");
                         log("反射is group_id: " + group_id);
                         Object group_name = XposedHelpers.getObjectField(user, "group_name");
                         log("反射is group_name: " + group_name);
                         Object group_start_time = XposedHelpers.getObjectField(user, "group_start_time");
                         log("反射is group_start_time: " + group_start_time);
                         Object is_disabled = XposedHelpers.getObjectField(user, "is_disabled");
                         log("反射is is_disabled: " + is_disabled);
                         Object is_unlimit = XposedHelpers.getObjectField(user, "is_unlimit");
                         log("反射is is_unlimit: " + is_unlimit);
                         Object level = XposedHelpers.getObjectField(user, "level");
                         log("反射is level: " + level);
                         Object level_name = XposedHelpers.getObjectField(user, "level_name");
                         log("反射is level_name: " + level_name);
                         Object location = XposedHelpers.getObjectField(user, "location");
                         log("反射is location: " + location);
                         Object nickname = XposedHelpers.getObjectField(user, "nickname");
                         log("反射is nickname: " + nickname);
                         Object parent_name = XposedHelpers.getObjectField(user, "parent_name");
                         log("反射is parent_name: " + parent_name);
                         Object province = XposedHelpers.getObjectField(user, "province");
                         log("反射is province: " + province);
                         Object register_at = XposedHelpers.getObjectField(user, "register_at");
                         log("反射is register_at: " + register_at);
                         Object user_tags = XposedHelpers.getObjectField(user, "user_tags");
                         log("反射is user_tags: " + user_tags);





                         XposedHelpers.setObjectField(user,"is_vip","y");
                         XposedHelpers.setObjectField(user,"level","6");
                         XposedHelpers.setObjectField(user,"is_unlimit","y");
                         XposedHelpers.setObjectField(user,"group_id","21");
                     }
                 });
        XposedHelpers.findAndHookMethod("com.aiqiyi.youtube.play.bean.response.UserInfoBean", classLoader, "getIs_vip",
                 new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        param.setResult("y");
                    }
        });
        XposedHelpers.findAndHookMethod("com.aiqiyi.youtube.play.bean.response.VideoBean", classLoader, "getIs_vip",
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        // 获取传入的 View 参数
                        Object result = param.getResult();
                        log("VideoBean is vip" + result.toString());
                        param.setResult("n");
                    }
        });
        XposedHelpers.findAndHookMethod("com.aiqiyi.youtube.play.bean.response.VideoBean", classLoader, "getItem_type",
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        log("VideoBean 视频类型" + param.getResult().toString());
                        param.setResult("movie");
                    }
                });
        XposedHelpers.findAndHookMethod("com.aiqiyi.youtube.play.bean.response.VideoBean", classLoader, "getDuration",
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        log("VideoBean 视频时长" + param.getResult().toString());

                    }
                });
        XposedHelpers.findAndHookMethod("com.aiqiyi.youtube.play.bean.response.VideoDetailBean", classLoader, "getIs_vip",
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        // 获取传入的 View 参数
                        Object result = param.getResult();
                        log("VideoDetailBean is vip" + result.toString());
                        param.setResult("n");
                    }
                });
        XposedHelpers.findAndHookMethod("com.aiqiyi.youtube.play.bean.response.CommentMsgBean", classLoader, "getIs_vip",
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        // 获取传入的 View 参数
                        Object result = param.getResult();
                        log("CommentMsgBean is vip" + result.toString());
                    }
                });
        XposedHelpers.findAndHookMethod("com.aiqiyi.youtube.play.bean.response.LinkBean", classLoader, "getIs_vip",
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        // 获取传入的 View 参数
                        Object result = param.getResult();
                        log("LinkBean is vip" + result.toString());
                        param.setResult("n");
                    }
                });
        XposedHelpers.findAndHookMethod("com.aiqiyi.youtube.play.bean.response.LoveMsgBean", classLoader, "getIs_vip",
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        // 获取传入的 View 参数
                        Object result = param.getResult();
                        log("LoveMsgBean is vip" + result.toString());
                    }
                });
        XposedHelpers.findAndHookMethod("com.aiqiyi.youtube.play.bean.response.PersonBean", classLoader, "getIs_vip",
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        // 获取传入的 View 参数
                        Object result = param.getResult();
                        log("PersonBean is vip" + result.toString());
                    }
                });

    }
}
