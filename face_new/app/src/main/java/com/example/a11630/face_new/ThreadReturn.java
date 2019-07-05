package com.example.a11630.face_new;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

public class ThreadReturn implements Callable<String> {
    String uu = "http://sumtudou.cn/Database/operate?opt={\"opt\":\"query_face\"}";
    StringBuffer response = new StringBuffer();

    public String call() throws Exception {

        HttpURLConnection conn = null;
        BufferedReader reader = null;
        try {
            Log.i("string_is", uu);
            URL url = new URL(uu);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            InputStream in = conn.getInputStream();
            //下面操作获取到的输入流
            reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            Log.i("GGG", response.toString());
            // BACK = response.toString();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null)
                conn.disconnect();

            return response.toString();
        }
    }
}
