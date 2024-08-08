package com.wsj.easyhook.service;

import android.content.Intent;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicBoolean;

public class FrpsTileService extends TileService {

    private AtomicBoolean isOpen = new AtomicBoolean(false);

    @Override
    public void onClick() {
        Tile qsTile = getQsTile();
        if (isOpen.get()) {
            //设置非活跃
            qsTile.setState(1);
            qsTile.updateTile();
            stopFrpc();
            isOpen.set(false);
        }else{
            //没有打开的状态
            //设置tile的状态为活跃
            qsTile.setState(2);
            //更新状态
            qsTile.updateTile();
            startFrpc();

            isOpen.set(true);
        }
        //关闭系统对话框
        Intent intent = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        sendBroadcast(intent);

    }

    private void stopFrpc() throws IOException {

        String[] cmd = {
                "sh",
                "-c",
                "su -c 'ps -A | grep frpc'"
        };
        Process process = Runtime.getRuntime().exec(cmd);

        // 执行netstat命令
//            Process process = Runtime.getRuntime().exec(new String[]{"sh", "-c", "netstat -tuln | grep :9697"});
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        String result= null;
        while ((line = reader.readLine()) != null) {
            Log.d("wsj", "line value: "+line);
            if (line.contains("frpc")) {
                // 如果找到状态为LISTEN的行，说明ADB正在9697端口上运行
                result = line;
                break;
            }
        }
        //处理result 获取 pid

    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        Tile qsTile = getQsTile();
        // 检查ADB是否已经在9697端口上开启
        if (frpsRunning()) {
            qsTile.setState(Tile.STATE_ACTIVE);
            isOpen.set(true);
        } else {
            qsTile.setState(Tile.STATE_INACTIVE);
            isOpen.set(false);
        }
        qsTile.updateTile();
//        Log.d("wsj", "onStartListening状态更新: "+isOpen.get());
    }

    private boolean frpsRunning() {
        try {
            String[] cmd = {
                    "sh",
                    "-c",
                    "su -c 'ps -A | grep frpc'"
            };
            Process process = Runtime.getRuntime().exec(cmd);

            // 执行netstat命令
//            Process process = Runtime.getRuntime().exec(new String[]{"sh", "-c", "netstat -tuln | grep :9697"});
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                Log.d("wsj", "line value: "+line);
                if (line.contains("frpc")) {
                    // 如果找到状态为LISTEN的行，说明ADB正在9697端口上运行
                    return true;
                }
            }
        } catch (IOException e) {
            Log.d("wsj", "frpsRunning: error " + e.toString());
        }
        return false;
    }

    private void startFrpc(){
        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            // 注意：确保frps可执行文件在/data/local/tmp/frps/目录下，并且具有执行权限
            String command = "/data/local/tmp/frps/frpc -c /data/local/tmp/frps/frpc.toml";
            processBuilder.command("/system/bin/sh", "-c", command);

            Process process = processBuilder.start();

            // 读取输出
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            int read;
            char[] buffer = new char[4096];
            StringBuffer output = new StringBuffer();
            while ((read = reader.read(buffer)) > 0) {
                output.append(buffer, 0, read);
            }
            reader.close();

            // 读取错误输出
            BufferedReader errors = new BufferedReader(
                    new InputStreamReader(process.getErrorStream()));
            while ((read = errors.read(buffer)) > 0) {
                output.append(buffer, 0, read);
            }
            errors.close();

            // 等待进程结束
            int exitVal = process.waitFor();
            if (exitVal == 0) {
                // 命令执行成功
                Log.d("FRPC", "FRPC service started successfully: " + output.toString());
            } else {
                // 命令执行失败
                Log.e("FRPC", "FRPC service failed to start: " + output.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }


}
