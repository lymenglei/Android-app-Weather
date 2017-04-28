package com.example.menglei.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import com.example.menglei.myapplication.data.GlobalData;
import com.example.menglei.myapplication.utils.SystemInfo;
import com.show.api.ShowapiRequest;

import java.io.UnsupportedEncodingException;

/**
 * Created by menglei on 17/04/11.
 */

public class DownloadService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        MainActivity.myLog("service.onCreate");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        MainActivity.myLog("service.onDestroy");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MainActivity.myLog("service.onStartCommand");
        if (intent != null){
            this.getJsonString(intent);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void getJsonString(Intent intent)
    {
        String ip = intent.getStringExtra(GlobalData.INTENT_NAME_IP);
        MainActivity.myLog("service.getJsonString");
        final String deviceHost = ip; //GlobalData.API_TEST_IP;
        MainActivity.myLog(deviceHost);
        Runnable run = new Runnable() {
            @Override
            public void run() {
                ShowapiRequest req=new ShowapiRequest("https://ali-weather.showapi.com/ip-to-weather", GlobalData.API_APP_CODE)
                        .addTextPara("needMoreDay", GlobalData.DAYS > 3 ? "1" : "0")
                        .addTextPara("ip", deviceHost);
                byte b[] = req.getAsByte();
                String str = null;
                try {
                    str = new String(b, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                Message msg = new Message();
                msg.what = MainActivity.MSG_RECEIVE_MSG;
                msg.obj = str;
                MainActivity.mainHandler.sendMessage(msg);
            }
        };
        new Thread(run).start();
    }


}
