package com.wsj.easyhook.app;

import com.wsj.easyhook.IXposedHookAbstract;
import com.wsj.easyhook.util.HookUitl;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * 服务器验证  搞不定
 */
public class Dy91Hook extends IXposedHookAbstract {
    public Dy91Hook(){
        packageName = "com.qnmd.a91xj.lk04tt";
        TAG = "香蕉";
        version = 17;
    }

    @Override
    public void hook(XC_LoadPackage.LoadPackageParam lpparam) {
        hookUserInfoBean(lpparam.classLoader);
        hookRecommendResultBean(lpparam.classLoader);
    }

    private void hookRecommendResultBean(ClassLoader classLoader) {
        Class<?> blockBean = XposedHelpers.findClass("com.aiqiyi.youtube.play.bean.response.RecommendResultBean$BlockBean", classLoader);
        Class<?> videoBean = XposedHelpers.findClass("com.aiqiyi.youtube.play.bean.response.VideoBean", classLoader);
        Class<?> detailBean = XposedHelpers.findClass("com.aiqiyi.youtube.play.bean.response.VideoDetailBean", classLoader);
        Class<?> test2 = XposedHelpers.findClass("com.aiqiyi.youtube.play.ui.av.detail.VideoDetailViewModel$i", classLoader);
        Class<?> linkBean = XposedHelpers.findClass("com.aiqiyi.youtube.play.bean.response.LinkBean", classLoader);

        XposedHelpers.findAndHookMethod(test2, "invoke", Object.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Object arg = param.args[0];
                Field[] declaredFields = detailBean.getDeclaredFields();
                for (Field field : declaredFields) {
                    field.setAccessible(true);
                    String name = field.getName();
                    if (name.equals("can_play")) {
                        field.set(arg, "n");
                    } else if (name.equals("is_vip")) {
                        field.set(arg, "n");
                    }else if (name.equals("money")) {
                        field.set(arg, "0");
                    }else if (name.equals("play_error_tips")) {
                        field.set(arg, null);
                    }else if (name.equals("vip_function_tips")) {
                        field.set(arg, null);
                    }else if (name.equals("is_user_vip")) {
                        field.set(arg, "n");
                    }else if (name.equals("link")) {
                        Object o = field.get(arg);
                        if (o instanceof List) {
                            List<?> list = (List<?>) o;
                            for (Object obj : list) {
                                Field[] declaredFields1 = linkBean.getDeclaredFields();
                                for (Field field1 : declaredFields1) {
                                    field1.setAccessible(true);
                                    log("linkBean:" + field1.get(obj));
                                }
                            }
                        }
                    }
                }
                HookUitl.printBeanAllValue(detailBean,arg);
//                HookUitl.printStack();
            }
        });

        HookUitl.printBeanAllValue("com.aiqiyi.youtube.play.bean.response.RecommendResultBean$BlockBean", classLoader, "setItems",List.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Object result = param.args[0];
                if (result instanceof List) {
                    List<?> list = (List<?>) result;
                    for (Object item : list) {
                        Field[] declaredFields = videoBean.getDeclaredFields();
                        for (Field field : declaredFields) {
                            field.setAccessible(true);
                            //A.isAssignableFrom(B)
                            //A和B均为Class对象，判断B是否等于/继承/实现A，是返回true，否返回false
                            if (field.getType().isAssignableFrom(detailBean)) {
                                continue;
                            }
                            String name = field.getName();
                            if (name.equals("is_vip")) {
                                field.set(item, "n");
                            } else if (name.equals("is_money")) {
                                field.set(item, "n");
                            }else if (name.equals("money")) {
                                field.set(item, "0");
                            }
                        }
                    }
                }
            }
        });

        HookUitl.printBeanAllValue("com.aiqiyi.youtube.play.bean.response.RecommendResultBean", classLoader, "setBlock",List.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Object result = param.args[0];
                if (result instanceof List) {
                    List<?> list = (List<?>) result;
                    for (Object item : list) {
                        Field[] declaredFields = blockBean.getDeclaredFields();
                        for (Field field : declaredFields) {
                            field.setAccessible(true);
                            String name = field.getName();
                            switch (name) {
                                case "buy_tips":
                                    field.set(item, "wsj test");
                                    break;
                                case "has_buy":
                                case "is_money":
                                    field.set(item, "n");
                                    break;
                                case "money":
                                    field.set(item, "0");
                                    break;
                                case "money_str":
                                    field.set(item, "¥0");
                                    break;
                            }
                        }
                    }
                }
            }
        });
    }

    private void hookVideoDetail(ClassLoader classLoader) {
        HookUitl.printGetValue( "com.aiqiyi.youtube.play.bean.response.VideoDetailBean",classLoader,null);
    }

    private void hookGroup(ClassLoader classLoader) {
        HookUitl.printGetValue( "com.aiqiyi.youtube.play.bean.response.VipInfoBean$GroupsBean",classLoader,null);
    }

    private void hookUserInfoBean(ClassLoader classLoader) {
//        HookUitl.printGetValue( "com.aiqiyi.youtube.play.bean.response.UserInfoBean",classLoader,null);
        Class<?> userBean = XposedHelpers.findClass("com.aiqiyi.youtube.play.bean.response.UserInfoBean", classLoader);
        HookUitl.printBeanAllValue("com.aiqiyi.youtube.play.MyApp",classLoader,"g","com.aiqiyi.youtube.play.bean.response.UserInfoBean");
        XposedHelpers.findAndHookMethod("com.aiqiyi.youtube.play.MyApp", classLoader, "g","com.aiqiyi.youtube.play.bean.response.UserInfoBean",
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        Object user = param.args[0];
                        XposedHelpers.setObjectField(user,"is_vip","y");
                        XposedHelpers.setObjectField(user,"level","6");
                        XposedHelpers.setObjectField(user,"is_unlimit","y");
                        XposedHelpers.setObjectField(user,"group_id","21");
                    }
        });
        XposedHelpers.findAndHookMethod("b.b.a.a.a.b.a", classLoader, "b","com.aiqiyi.youtube.play.bean.response.UserInfoBean",
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        Object user = param.args[0];
                        XposedHelpers.setObjectField(user,"is_vip","y");
                        XposedHelpers.setObjectField(user,"level","6");
                        XposedHelpers.setObjectField(user,"is_unlimit","y");
                        XposedHelpers.setObjectField(user,"group_id","21");
                    }
       });
    }
}
