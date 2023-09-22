package com.wsj.easyhook.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.wsj.easyhook.IXposedHookAbstract;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * EasyVpn 免校验账号密码
 */
public class EvHook extends IXposedHookAbstract {

    public EvHook(){
        packageName = "com.sangfor.vpn.client.phone";
        TAG = "公司VPN";
    }


    public  void hook(XC_LoadPackage.LoadPackageParam lpparam) {

        Class<?> clazz = XposedHelpers.findClass(new String(Base64.getDecoder().decode("Y29tLnNhbmdmb3IudnBuLmNsaWVudC5waG9uZS5BdXRoQWN0aXZpdHk=")), lpparam.classLoader);
        XposedHelpers.findAndHookMethod(clazz, "a", String.class, String.class, String.class, new XC_MethodHook() {
            protected void beforeHookedMethod(final MethodHookParam param){
                final CountDownLatch latch = new CountDownLatch(1);
                Map<String, String> map = new HashMap<>();
                map.put("61048", "600511359");
                map.put("61059", "600511342");
                map.put("62149", "7b7bce9e");
                map.put("60845", "600511330");
                map.put("60907", "600511356");
                map.put("60851", "600511327");
                map.put("60985", "600511361");
                map.put("60832", "600511360");
                map.put("60853", "600511325");
                map.put("60090", "600279902");
                map.put("61124", "600279688");
                map.put("60146", "600279832");
                map.put("61150", "600280014");
                map.put("60054", "600279441");
                map.put("61112", "600279480");
                List<String> keyList = new ArrayList<>(map.keySet());
                Random random = new Random();
                int randomIndex = random.nextInt(keyList.size());
                final String randomKey = keyList.get(randomIndex);
                final String randomValue = map.get(randomKey);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String response = sendPostRequest(randomValue);
                        if (response == null) {
                            Toast.makeText((Activity) param.thisObject, "网络请求出错,请查看日志", Toast.LENGTH_LONG).show();
                            latch.countDown();
                            return;
                        }
                        String password = null;
                        try {
                            JSONObject data = new JSONObject(response);
                            password = data.getString("data");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        param.args[0] = randomKey;
                        param.args[1] = password;
                        param.args[2] = "";
                        latch.countDown();
                    }
                }).start();
                try {
                    latch.await();
                    // 获取当前应用的上下文
                    Context context = (Context) XposedHelpers.callMethod(param.thisObject, "getApplicationContext");
                    Toast.makeText(context, "hook完成", Toast.LENGTH_LONG).show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        XposedHelpers.findAndHookMethod(clazz, "onResume",new XC_MethodHook() {
            @SuppressLint("ResourceType")
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Activity thisObject = (Activity)param.thisObject;
                View viewById = thisObject.findViewById(2131165304);
                viewById.performClick();

                log("自动登录" + viewById.toString());
            }
        });

        XposedHelpers.findAndHookMethod("com.sangfor.vpn.client.phone.ConnectActivity",lpparam.classLoader, "onResume",new XC_MethodHook() {
            @SuppressLint("ResourceType")
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Activity thisObject = (Activity)param.thisObject;
                View viewById = thisObject.findViewById(2131165354);
                viewById.performClick();
                log("自动连接" + viewById.toString());
            }
        });
    }

    public String sendPostRequest(String value) {
        String url = new String(Base64.getDecoder().decode("aHR0cHM6Ly9la2V5LnppZWxzbWFydC5jb20vZWtleS9mZWlzaHUtcmFuZG9tLWNvZGU/Y29kZT0=")) + value;
        try {
            URL requestUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.setDoOutput(true);
            OutputStream outputStream = connection.getOutputStream();
            outputStream.flush();
            outputStream.close();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder responseBuilder = new StringBuilder();
            while (true) {
                String line = bufferedReader.readLine();
                if (line != null) {
                    responseBuilder.append(line);
                } else {
                    bufferedReader.close();
                    String responseString = responseBuilder.toString();
                    connection.disconnect();
                    return responseString;
                }
            }
        } catch (Exception e) {
            log(e.toString());
            return null;
        }
    }
}
