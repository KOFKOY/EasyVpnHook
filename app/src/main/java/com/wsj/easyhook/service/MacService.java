package com.wsj.easyhook.service;

import android.content.Intent;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.widget.Toast;

import java.io.File;
import java.io.PrintWriter;
import java.util.Random;

public class MacService extends TileService {

    @Override
    public void onClick() {
        Tile qsTile = getQsTile();
        qsTile.setState(2);
        qsTile.updateTile();
        changeMac();
        Intent intent = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        sendBroadcast(intent);
        qsTile.setState(1);
        qsTile.updateTile();
    }

    private void changeMac() {
        try {
            String mac = generateRandomMacAddress();
            // 创建一个临时shell脚本
            File shellScript = File.createTempFile("script", null);
            PrintWriter writer = new PrintWriter(shellScript);

            // 写入命令
            writer.println("adb shell su");
            writer.println("su -c ifconfig wlan0 down");
            writer.println("su -c ifconfig wlan0 hw ether " + mac);
            writer.println("su -c ifconfig wlan0 up");

            writer.close();

            // 执行shell脚本
            ProcessBuilder processBuilder = new ProcessBuilder("sh", shellScript.toString());
            Process process = processBuilder.start();
            process.waitFor();

            // 删除临时shell脚本
            shellScript.delete();

            Toast.makeText(this, "geigei~~,MAC地址修改成功：" + mac, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String generateRandomMacAddress() {
        Random random = new Random();
        byte[] macAddressBytes = new byte[6];

        // 设置MAC地址的第一个字节为02，表示本地分配的MAC地址
        macAddressBytes[0] = (byte) 0x02;

        // 生成随机的剩余5个字节
        random.nextBytes(macAddressBytes);

        // 确保第一个字节的最低有效位为0（单播地址）
        //转成二进制 最后一位是0  就是单播地址   最后一位是1  多播地址
        macAddressBytes[0] = (byte) (macAddressBytes[0] & (byte) 254);  // 254的二进制表示是11111110

        // 将生成的字节数组转换为十六进制字符串
        StringBuilder macAddressBuilder = new StringBuilder(17);
        for (int i = 0; i < 6; i++) {
            if (i != 0) {
                macAddressBuilder.append(':');
            }
            macAddressBuilder.append(String.format("%02x", macAddressBytes[i]));
        }

        return macAddressBuilder.toString();
    }

}
