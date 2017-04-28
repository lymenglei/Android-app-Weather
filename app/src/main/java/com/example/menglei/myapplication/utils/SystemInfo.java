package com.example.menglei.myapplication.utils;

import android.os.Environment;
import android.os.Message;
import android.util.Log;

import com.example.menglei.myapplication.MainActivity;
import com.example.menglei.myapplication.data.GlobalData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by menglei on 17/04/20.
 */

public class SystemInfo {


    ////////////////// impl functions below //////////////////////////
    // get device ip
    public static void getDeviceIP()
    {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                String result = "";
                URL infoUrl = null;
                InputStream inStream = null;
                try {
                    infoUrl = new URL("http://city.ip138.com/ip2city.asp");

                    URLConnection connection = infoUrl.openConnection();
                    HttpURLConnection httpConnection = (HttpURLConnection) connection;
                    int responseCode = httpConnection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        inStream = httpConnection.getInputStream();
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(inStream, "utf-8"));
                        StringBuilder strber = new StringBuilder();
                        String line = null;
                        while ((line = reader.readLine()) != null)
                            strber.append(line + "\n");
                        inStream.close();
                        // 从反馈的结果中提取出IP地址
                        int start = strber.indexOf("[");
                        Log.d("zph", "" + start);
                        int end = strber.indexOf("]", start + 1);
                        Log.d("zph", "" + end);
                        line = strber.substring(start + 1, end);
                        result = line;
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Message msg = new Message();
                msg.what = MainActivity.MSG_GET_DEVICE_IP;
                msg.obj = result;
                MainActivity.mainHandler.sendMessage(msg);
            }
        };
        new Thread(run).start();
    }

    public static String getAppSdcardPath()
    {
        File dir = new File("/", MainActivity.getExtFileDir());
        if( !dir.exists() )
        {
            if ( dir.mkdirs() )
            {
                return dir.getAbsolutePath();
            }else
            {
                return null;
            }
        }else
        {
            if ( !dir.isDirectory() )
            {
                return null;
            }
        }
        return dir.getAbsolutePath();
    }

    public static void writeJsonString(String msg)
    {
        String sdcard = SystemInfo.getAppSdcardPath();
        if (sdcard == null || msg == null)
            return;
        String filePath = sdcard + GlobalData.JSON_FILE_PATH;
        MainActivity.myLog("write file path: " + filePath);
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                File dir = new File(file.getParent());
                dir.mkdirs();
                file.createNewFile();
            }
            FileOutputStream outStream = new FileOutputStream(file);
            outStream.write(msg.getBytes());
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String readJsonString() throws IOException {
        String sdcard = SystemInfo.getAppSdcardPath();
        if (sdcard == null)
            return null;
        String filePath = sdcard + GlobalData.JSON_FILE_PATH;
        File f = new File(filePath);
        String str ;
        String result = "";
        if (f.exists() && f.isFile())
        {
            try {
                FileInputStream fis = new FileInputStream(f);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);
                while((str = br.readLine()) != null)
                {
                    result += str;
                }
                br.close();
                isr.close();
                fis.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        if(result.isEmpty())
            return null;
        return result;
    }
}
