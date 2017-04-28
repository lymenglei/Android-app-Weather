package com.example.menglei.myapplication.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.widget.ImageView;

import android.os.Handler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by menglei on 17/04/25.
 */

public class NormalLoadPicture {
    private static final int MSG_DOWNLOAD_SUCCESS = 1;

    private String uri;
    private ImageView imageView;
    private byte[] picByte;


    public void getPicture(String uri,ImageView imageView){
        this.uri = uri;
        this.imageView = imageView;
        new Thread(runnable).start();
    }

    @SuppressLint("HandlerLeak")
    private Handler handle = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_DOWNLOAD_SUCCESS) {
                if (picByte != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(picByte, 0, picByte.length);
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    };

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL(uri);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("GET");
                conn.setReadTimeout(10000);

                if (conn.getResponseCode() == 200) {
                    InputStream fis =  conn.getInputStream();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] bytes = new byte[1024];
                    int length = -1;
                    while ((length = fis.read(bytes)) != -1) {
                        bos.write(bytes, 0, length);
                    }
                    picByte = bos.toByteArray();
                    bos.close();
                    fis.close();

                    Message message = new Message();
                    message.what = MSG_DOWNLOAD_SUCCESS;
                    handle.sendMessage(message);
                }


            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
}
