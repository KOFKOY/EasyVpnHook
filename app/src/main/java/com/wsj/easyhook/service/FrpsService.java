package com.wsj.easyhook.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicBoolean;

public class FrpsService extends Service {

    private Process frpsProcess;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            Log.d("wsj", "onStartCommand: 启动frps服务");
            String[] cmd = {
                    "/system/bin/sh",
                    "-c",
                    "cd /data/local/tmp/frps/ && frpc -c frpc.toml"
            };
            frpsProcess = Runtime.getRuntime().exec(cmd);
//            frpsProcess.waitFor();
        } catch (Exception e) {
            Log.d("wsj FrpsService", "onStartCommand: "+e.toString());
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (frpsProcess != null) {
            frpsProcess.destroy();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
