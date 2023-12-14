package com.wsj.easyhook.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wsj.easyhook.IXposedHookAbstract;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
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

    private Map<String,String> map;

    public EvHook(){
        packageName = "com.sangfor.vpn.client.phone";
        TAG = "公司VPN";
        debug = true;
        version = 2;
    }


    public  void hook(XC_LoadPackage.LoadPackageParam lpparam) {

        Class<?> clazz = XposedHelpers.findClass(new String(Base64.getDecoder().decode("Y29tLnNhbmdmb3IudnBuLmNsaWVudC5waG9uZS5BdXRoQWN0aXZpdHk=")), lpparam.classLoader);
        XposedHelpers.findAndHookMethod(clazz, "a", String.class, String.class, String.class, new XC_MethodHook() {
            protected void beforeHookedMethod(final MethodHookParam param) throws InterruptedException {
                final CountDownLatch latch = new CountDownLatch(1);
                final CountDownLatch mapLatch = new CountDownLatch(1);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String userInfo = getUserInfo();
                        if (userInfo == null) {
                            log("获取vpn用户信息出错，请查看gitee");
                        }else {
                            Gson gson = new Gson();
                            Map content = gson.fromJson(userInfo, Map.class);
                            map = gson.fromJson(new String(Base64.getDecoder().decode((String) content.get("content"))), Map.class);
                        }
                        mapLatch.countDown();
                    }
                }).start();
                mapLatch.await();

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


    public String getUserInfo() {
        String url = new String(Base64.getDecoder().decode("aHR0cHM6Ly9naXRlZS5jb20vYXBpL3Y1L3JlcG9zL3dzamtvZi9maWxlL2NvbnRlbnRzL3ZwblVzZXJJbmZvLmpzb24/YWNjZXNzX3Rva2VuPTg0ZTgyNTNlNDRkYmI0NGJiZjEzZWJlNTZhODVhZjQ1"));
        try {
            URL requestUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
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
            log("请求数据失败" + e.toString());
            return null;
        }
    }

}
