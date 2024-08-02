package com.wsj.easyhook.service;

import android.content.Intent;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.widget.Toast;

import java.io.File;
import java.io.PrintWriter;
import java.util.Random;

import de.robv.android.xposed.XC_MethodHook;

public class AdbService extends TileService {

    @Override
    public void onClick() {
        Tile qsTile = getQsTile();
        //设置tile的状态为活跃
        qsTile.setState(2);
        //更新状态
        qsTile.updateTile();
        adbStart();
        //关闭系统对话框
        Intent intent = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        sendBroadcast(intent);
        //设置非活跃
        qsTile.setState(1);
        qsTile.updateTile();
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

}
