package com.wsj.easyhook.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;


public class HookUitl {
    /**
     * 打印要hook方法传参的对象所有值
     * @param classLoader
     * @param targetClassName
     * @param methodName
     * @param parameterTypes
     */
    public static void printBeanAllValue(String targetClassName,ClassLoader classLoader,  String methodName, Object... parameterTypes){
        if (parameterTypes!=null && parameterTypes.length!=0 && (parameterTypes[parameterTypes.length - 1] instanceof XC_MethodHook)) {
            XposedHelpers.findAndHookMethod(targetClassName, classLoader, methodName, parameterTypes);
        }else {
            XposedHelpers.findAndHookMethod(targetClassName, classLoader, methodName, parameterTypes, new PrintXcHookMethod());
        }
    }

    public static void printBeanAllValue(Class<?> detailBean, Object arg) {
        Field[] fields = detailBean.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(arg);
                String fieldName = field.getName();
                XposedBridge.log(detailBean.getName() + " 对象, 字段: " + fieldName + ", Value值: " + value);
            } catch (IllegalAccessException e) {
                XposedBridge.log("打印出错 Error accessing field: " + e.getMessage());
            }
        }
    }

    private static class PrintXcHookMethod extends XC_MethodHook{
        @Override
        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            Object item = param.args[0];
            if (item != null) {
                printFieldsAndValues(item);
            }
        }
    }

    private static void printFieldsAndValues(Object object) {
        if (object == null) {
            XposedBridge.log("要打印的对象 Object is null");
            return;
        }

        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(object);
                String fieldName = field.getName();
                XposedBridge.log(clazz.getName() + " 对象, 字段: " + fieldName + ", Value值: " + value);
            } catch (IllegalAccessException e) {
                XposedBridge.log("打印出错 Error accessing field: " + e.getMessage());
            }
        }
    }


    /**
     * 打印方法get返回值
     * @param classLoader
     * @param className
     */
    public static void printGetValue( String className,ClassLoader classLoader,Object callBack) {
        Class<?> targetClass = XposedHelpers.findClass(className, classLoader);
        Method[] methods = targetClass.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("get")) {
                hookGetMethod(targetClass, method,callBack);
            }
        }
    }

    private static void hookGetMethod(Class<?> targetClass, Method method,Object callBack) {
        if (callBack == null) {
            callBack = new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    XposedBridge.log(targetClass.getName() + " Hooked method: " + method.getName() + " 方法, Result: " + param.getResult());
                    // 在这里执行你的操作
                }
            };
        }
        XposedHelpers.findAndHookMethod(targetClass, method.getName(), callBack);
    }
}
