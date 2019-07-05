package com.example.a11630.face_new;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

public class BackTool {

    public static void SendHttpInsert(final String ID, final String name_or_date, final int type) {
        String uu = null;
        if (type == 1) {             //insert
            uu = "http://sumtudou.cn/Database/operate?opt={\"opt\":\"insert\",\"id\":\"" + ID
                    + "\",\"name\":\"" + name_or_date + "\"}";
        } else if (type == 2) {      //in_clock
            uu = "http://sumtudou.cn/Database/operate?opt={\"opt\":\"in_clock\",\"id\":\"" + ID
                    + "\",\"date\":\"" + name_or_date + "\"}";
        }

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
            StringBuffer response = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            Log.i("GGG", response.toString());
            //  showResponse(response.toString());
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
        }
    }

    public static String SendHttpDelete_orQueryAllface(final String ID, final int type) {  //懒得改了，直接重写了
        String uu = null;
        StringBuffer response = new StringBuffer();
        if (type == 1) {
            uu = "http://sumtudou.cn/Database/operate?opt={\"opt\":\"delete\",\"id\":\"" + ID
                    + "\"}";
        } else if (type == 2) {
            uu = "http://sumtudou.cn/Database/operate?opt={\"opt\":\"query_face\"}";
        }

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
            //  showResponse(response.toString());
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
        }
        return response.toString();
    }

    public static String unicodeToString(String str) {

        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(str);
        char ch;
        while (matcher.find()) {
            //group 6728
            String group = matcher.group(2);
            //ch:'木' 26408
            ch = (char) Integer.parseInt(group, 16);
            //group1 \u6728
            String group1 = matcher.group(1);
            str = str.replace(group1, ch + "");
        }
        return str;
    }


}