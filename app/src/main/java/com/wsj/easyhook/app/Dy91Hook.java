package com.wsj.easyhook.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wsj.easyhook.IXposedHookAbstract;
import com.wsj.easyhook.util.HookUitl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.util.Base64;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Dy91Hook extends IXposedHookAbstract {
    public Dy91Hook(){
        packageName = "com.qnmd.a91xj.lk04tt";
        TAG = "香蕉";
        version = 6;
    }

    @Override
    public void hook(XC_LoadPackage.LoadPackageParam lpparam) {
//        hookVideoBean(lpparam.classLoader);
//        hookUserInfoBean(lpparam.classLoader);
//        hookGroup(lpparam.classLoader);
//        hookVideoDetail(lpparam.classLoader);
        hookRecommendResultBean(lpparam.classLoader);
    }

    private void hookRecommendResultBean(ClassLoader classLoader) {
        Class<?> blockBean = XposedHelpers.findClass("com.aiqiyi.youtube.play.bean.response.RecommendResultBean$BlockBean", classLoader);
        Class<?> videoBean = XposedHelpers.findClass("com.aiqiyi.youtube.play.bean.response.VideoBean", classLoader);
        Class<?> detailBean = XposedHelpers.findClass("com.aiqiyi.youtube.play.bean.response.VideoDetailBean", classLoader);
        Class<?> test = XposedHelpers.findClass("b.b.a.a.a.n.m", classLoader);
        Class<?> iInvoke = XposedHelpers.findClass("q.s.b.l", classLoader);

        Object callback =  Proxy.newProxyInstance(classLoader, new Class[]{iInvoke}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //如果不需要 没必要实现
                log("如果不需要 没必要实现");
                if(method.getName().equals("invoke")){
                    Object res = args[0];
                    HookUitl.printBeanAllValue(detailBean,res);
                }
                return null;
            }
        });

        XposedHelpers.callMethod(test,"invoke", callback);







//        Method[] declaredMethods = test.getDeclaredMethods();
//        for (Method me : declaredMethods) {
//            String name = me.getName();
//            if (name.equals("invoke")) {
//                log("找到invoke方法");
////                XposedHelpers.findAndHookMethod("b.b.a.a.a.n.m", classLoader,"q.s.b.linvoke", "com.aiqiyi.youtube.play.bean.response.VideoDetailBean", new XC_MethodHook() {
////                @Override
////                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
////                    log("hook1 到 detail参数");
////                    Object arg = param.args[0];
////                    HookUitl.printBeanAllValue(detailBean,arg);
////                }
////                });
//            }
//        }
        Class<?> test2 = XposedHelpers.findClass("b.b.a.a.a.n.a$k", classLoader);

//        XposedHelpers.findAndHookMethod("b.b.a.a.a.n.a$k", classLoader, "invoke", detailBean, new XC_MethodHook() {
//            @Override
//            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                log("hook1 到 detail参数");
//                Object arg = param.args[0];
//                HookUitl.printBeanAllValue(detailBean,arg);
//            }
//        });

//        XposedHelpers.findAndHookMethod("b.b.a.a.a.n.m", classLoader, "invoke", detailBean, new XC_MethodHook() {
//            @Override
//            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                log("hook2 到 detail参数");
//                Object arg = param.args[0];
//                HookUitl.printBeanAllValue(detailBean,arg);
//            }
//        });

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
//                                log("setItems  detailBean 打印:" + field.getName() + ", value:" + field.get(item));
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
//        HookUitl.printBeanAllValue("com.aiqiyi.youtube.play.bean.response.RecommendResultBean", classLoader, "getBlock", new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                log("get方法");
//                Object result = param.getResult();
//                if (result instanceof List) {
//                    List<?> list = (List<?>) result;
//                    for (Object item : list) {
//                        Field[] declaredFields = blockBean.getDeclaredFields();
//                        for (Field field : declaredFields) {
//                            field.setAccessible(true);
//                            try {
//                                log("打印:" + field.getName() + ", value:" + field.get(item));
//                            } catch (Exception e) {
//                                log("异常:" + field.getName() + e);
//                            }
//                        }
//                    }
//                }
//            }
//        });
    }

    private void hookVideoDetail(ClassLoader classLoader) {
        HookUitl.printGetValue( "com.aiqiyi.youtube.play.bean.response.VideoDetailBean",classLoader,null);
    }

    private void hookGroup(ClassLoader classLoader) {
        HookUitl.printGetValue( "com.aiqiyi.youtube.play.bean.response.VipInfoBean$GroupsBean",classLoader,null);
    }

    private void hookUserInfoBean(ClassLoader classLoader) {
        HookUitl.printGetValue( "com.aiqiyi.youtube.play.bean.response.UserInfoBean",classLoader,null);
        Class<?> userBean = XposedHelpers.findClass("com.aiqiyi.youtube.play.bean.response.UserInfoBean", classLoader);
        HookUitl.printBeanAllValue("com.aiqiyi.youtube.play.MyApp",classLoader,"g",userBean);
//        XposedHelpers.findAndHookMethod("com.aiqiyi.youtube.play.MyApp", classLoader, "g",userBean,
//                new XC_MethodHook() {
//                    @Override
//                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                        Object user = param.args[0];
//                        XposedHelpers.setObjectField(user,"is_vip","y");
//                        XposedHelpers.setObjectField(user,"level","6");
//                        XposedHelpers.setObjectField(user,"is_unlimit","y");
//                        XposedHelpers.setObjectField(user,"group_id","21");
//                    }
//        });
//        XposedHelpers.findAndHookMethod("b.b.a.a.a.b.a", classLoader, "b",userBean,
//                new XC_MethodHook() {
//                    @Override
//                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                        Object user = param.args[0];
//                        XposedHelpers.setObjectField(user,"is_vip","y");
//                        XposedHelpers.setObjectField(user,"level","6");
//                        XposedHelpers.setObjectField(user,"is_unlimit","y");
//                        XposedHelpers.setObjectField(user,"group_id","21");
//                    }
//       });
    }

    private void hookVideoBean(ClassLoader classLoader) {
        HookUitl.printGetValue( "com.aiqiyi.youtube.play.bean.response.VideoBean",classLoader,null);
//        Class<?> targetClass = XposedHelpers.findClass("com.aiqiyi.youtube.play.bean.response.VideoBean", classLoader);
//        Method[] methods = targetClass.getDeclaredMethods();
//        for (Method method : methods) {
//            if (method.getName().startsWith("get")) { // 只hook以"get"开头的方法
//                XposedHelpers.findAndHookMethod(targetClass, method.getName(), new XC_MethodHook() {
//                    @Override
//                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                        log("VideoBean Hooked method: " + method.getName() + ", Result: " + param.getResult());
//                        // 在这里执行你的操作
//                    }
//                });
//            }
//        }
    }
}
