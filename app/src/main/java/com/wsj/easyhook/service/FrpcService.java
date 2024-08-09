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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * 使用说明
 * 1。手机获取root权限
 * 2。在手机/data/local/tmp/frp/ 文件夹下 ，必须有frpc  和 frpc.toml 两个文件
 */
public class FrpcService extends TileService {

    private final AtomicBoolean isOpen = new AtomicBoolean(false);

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

    private void stopFrpc(){
        try {
            String[] cmd = {
                    "sh",
                    "-c",
                    "su -c 'ps -A | grep frpc'"
            };
            Process process = Runtime.getRuntime().exec(cmd);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            String result = null;
            while ((line = reader.readLine()) != null) {
//                Log.d("wsj", "line value: " + line);
                if (line.contains("frpc")) {
                    // 如果找到状态为LISTEN的行，说明ADB正在9697端口上运行
                    result = line;
                    break;
                }
            }
            String regex = "root\\s+(\\d+)";
            Pattern compile = Pattern.compile(regex);
            Matcher matcher = compile.matcher(result);
            Integer pid = null;
            while (matcher.find()) {
                pid = Integer.valueOf(matcher.group(1));
            }
//            Log.d("wsj",pid+"");
            Process process2 = Runtime.getRuntime().exec(new String[]{"su", "-c", "kill -9 " + pid});
            process2.waitFor();
            Toast.makeText(this, "已关闭FRPC服务", Toast.LENGTH_LONG).show();
        }catch (Exception e){
            Log.e("FrpcService", "stopFrpc: " + e);
        }
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        Tile qsTile = getQsTile();
        // 检查ADB是否已经在9697端口上开启
        if (frpcRunning()) {
            qsTile.setState(Tile.STATE_ACTIVE);
            isOpen.set(true);
        } else {
            qsTile.setState(Tile.STATE_INACTIVE);
            isOpen.set(false);
        }
        qsTile.updateTile();
//        Log.d("wsj", "onStartListening状态更新: "+isOpen.get());
    }

    private boolean frpcRunning() {
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
            Log.d("wsj", "frpcRunning: error " + e);
        }
        return false;
    }

    private void startFrpc(){

            new Thread(() -> {
                try {
                    ProcessBuilder processBuilder = new ProcessBuilder();
                    // 注意：确保frps可执行文件在/data/local/tmp/frps/目录下，并且具有执行权限
                    String command = "/data/local/tmp/frp/frpc -c /data/local/tmp/frp/frpc.toml";
                    processBuilder.command("su", "-c", command);
                    Process start = processBuilder.start();
                    // 等待进程结束
                    start.waitFor();
                    Log.d("wsj", "frpc 停止");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();
            Toast.makeText(this,"frpc已启动 端口 19016,正在连接服务",Toast.LENGTH_LONG).show();
    }
}
