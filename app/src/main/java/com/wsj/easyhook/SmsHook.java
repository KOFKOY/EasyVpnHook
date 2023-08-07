package com.wsj.easyhook;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.telephony.VisualVoicemailSms;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * 小米短信hook  复制验证码到剪切板
 */
public class SmsHook {

    public static void smsHook(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedBridge.log("短信hook 成功1");
        XposedHelpers.findAndHookMethod("f.g.b.s.e.f$a",lpparam.classLoader,"onReceive", Context.class, Intent.class, new XC_MethodHook(){
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("短信hook 成功2");
                Intent intent = (Intent) param.args[1];
                Bundle extras = intent.getExtras();
                if (extras != null) {
                    Object[] pdus = (Object[]) extras.get("pdus");
                    if (pdus != null) {
                        for (Object pdu : pdus) {
                            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);

                            String senderPhoneNumber = smsMessage.getOriginatingAddress();
                            String messageBody = smsMessage.getMessageBody();
                            XposedBridge.log("hook 结果1:" + senderPhoneNumber);
                            XposedBridge.log("hook 结果2:" + messageBody);
                            // 在这里可以处理短信内容，例如保存到数据库、显示通知等
                        }
                    }
                }
                Notification notification = (Notification)intent.getParcelableExtra("notification");
                XposedBridge.log("hook 结果:" + notification.toString());
                Intent intent2 = (Intent) intent.getParcelableExtra("clickIntent");
                XposedBridge.log("hook 结果:" + intent2.toString());
            }
        });

    }

    public static void phone(XC_LoadPackage.LoadPackageParam lpparam) {

        XposedHelpers.findAndHookMethod("com.android.phone.vvm.RemoteVvmTaskManager",lpparam.classLoader,"startSmsReceived", Context.class, VisualVoicemailSms.class,String.class, new XC_MethodHook(){
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("短信hook 成功3");
            }
        });
    }
}
