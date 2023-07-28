package com.wsj.easyvpnhook;

import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        String s = sendPostRequest("600279832");
        System.out.println(s);
    }

    public static String sendPostRequest(String value) {
        String url = "https://ekey.zielsmart.com/ekey/feishu-random-code?code="+value;
        String responseString = "";
        try {
            URL requestUrl = new URL(url);
            // Open HttpURLConnection
            HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            connection.setDoOutput(true);
            // Write data to the server
            OutputStream outputStream = connection.getOutputStream();
            outputStream.flush();
            outputStream.close();
            // Get response from the server
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder responseBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                responseBuilder.append(line);
            }
            bufferedReader.close();
            responseString = responseBuilder.toString();
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseString;
    }
}