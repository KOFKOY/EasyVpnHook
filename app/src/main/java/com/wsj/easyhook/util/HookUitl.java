package com.wsj.easyhook.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;


public class HookUitl {
    /**
     * 打印要hook方法传参的对象所有值
     *
     * @param classLoader
     * @param targetClassName
     * @param methodName
     * @param parameterTypes
     */
    public static void printBeanAllValue(String targetClassName, ClassLoader classLoader, String methodName, Object... parameterTypes) {
        if (parameterTypes != null && parameterTypes.length != 0 && (parameterTypes[parameterTypes.length - 1] instanceof XC_MethodHook)) {
            XposedHelpers.findAndHookMethod(targetClassName, classLoader, methodName, parameterTypes);
        } else {
            Object[] parame;
            if (parameterTypes == null) {
                parame = new Object[1];
                parame[0] = new PrintXcHookMethod();
            } else {
                parame = new Object[parameterTypes.length + 1];
                for (int i = 0; i < parame.length - 1; i++) {
                    parame[i] = parameterTypes[i];
                }
                parame[parame.length - 1] = new PrintXcHookMethod();
            }

            XposedHelpers.findAndHookMethod(targetClassName, classLoader, methodName, parame);
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

    private static class PrintXcHookMethod extends XC_MethodHook {
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
     *
     * @param classLoader
     * @param className
     */
    public static void printGetValue(String className, ClassLoader classLoader, Object callBack) {
        Class<?> targetClass = XposedHelpers.findClass(className, classLoader);
        Method[] methods = targetClass.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("get")) {
                hookGetMethod(targetClass, method, callBack);
            }
        }
    }

    private static void hookGetMethod(Class<?> targetClass, Method method, Object callBack) {
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

    public static void printStack() {
        // 获取当前线程的堆栈跟踪元素
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        // 遍历堆栈跟踪元素并打印
        for (StackTraceElement element : stackTraceElements) {
            XposedBridge.log("方法调用: " + element.toString());
        }
    }

    /**
     * 复制文本到剪切板
     * @param context
     * @param textToCopy
     */
    public static void copyToClipboard(Context context, String textToCopy) {
        // 获取剪切板管理器
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);

        // 创建一个ClipData对象，将要复制的文本放入其中
        ClipData clipData = ClipData.newPlainText("text", textToCopy);

        // 将ClipData对象放入剪切板
        clipboardManager.setPrimaryClip(clipData);

        // 提示用户已成功复制到剪切板
        Toast.makeText(context, "已复制到剪切板", Toast.LENGTH_SHORT).show();
    }

}
