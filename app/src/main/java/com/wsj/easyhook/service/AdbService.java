package com.wsj.easyhook.service;

import android.content.Intent;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import de.robv.android.xposed.XC_MethodHook;

public class AdbService extends TileService {

    private final AtomicBoolean isOpen = new AtomicBoolean(false);

    @Override
    public void onClick() {
        Tile qsTile = getQsTile();
        if (isOpen.get()) {
            //设置非活跃
            qsTile.setState(1);
            qsTile.updateTile();
            adbStop();
            isOpen.set(false);
        }else{
            //没有打开的状态
            //设置tile的状态为活跃
            qsTile.setState(2);
            //更新状态
            qsTile.updateTile();
            adbStart();
            isOpen.set(true);
        }
        //关闭系统对话框
        Intent intent = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        sendBroadcast(intent);

    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        Tile qsTile = getQsTile();
        // 检查ADB是否已经在9697端口上开启
        if (isAdbRunningOnPort9697()) {
            qsTile.setState(Tile.STATE_ACTIVE);
            isOpen.set(true);
        } else {
            qsTile.setState(Tile.STATE_INACTIVE);
            isOpen.set(false);
        }
        qsTile.updateTile();
//        Log.d("wsj", "onStartListening状态更新: "+isOpen.get());
    }

    private boolean isAdbRunningOnPort9697() {
        try {
            // 执行netstat命令
            Process process = Runtime.getRuntime().exec(new String[]{"sh", "-c", "netstat -tuln | grep :9697"});
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
//                Log.d("wsj", "line value: "+line);
                if (line.contains("LISTEN")) {
                    // 如果找到状态为LISTEN的行，说明ADB正在9697端口上运行
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private  void adbStart(){
        try {
            // 执行adb tcpip命令
            Process exec = Runtime.getRuntime().exec("su -c setprop service.adb.tcp.port 9697");
            exec.waitFor();
            Process stop_adbd = Runtime.getRuntime().exec("su -c stop adbd");
            stop_adbd.waitFor();
            Process start_adbd = Runtime.getRuntime().exec("su -c start adbd");
            start_adbd.waitFor();
            Toast.makeText(this, "开启ADB监听 端口：9697", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private  void adbStop(){
        try {
            // 执行adb tcpip命令
            Process exec = Runtime.getRuntime().exec("su -c setprop service.adb.tcp.port 9697");
            exec.waitFor();
            Process stop_adbd = Runtime.getRuntime().exec("su -c stop adbd");
            stop_adbd.waitFor();
            Toast.makeText(this, "关闭ADB监听", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
