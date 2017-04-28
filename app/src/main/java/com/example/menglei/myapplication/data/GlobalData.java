package com.example.menglei.myapplication.data;

import com.example.menglei.myapplication.MainActivity;

/**
 * Created by menglei on 17/04/11.
 */

public class GlobalData {

    public static final int DAYS = 7; // 预报显示的天数(3/7可选，7的时候请求时间较长)
    public static final int SHOW_DAYS = 5; // ui显示的天数
    public static final String API_HOST = "https://ali-weather.showapi.com";
    public static final String API_PATH = "/ip-to-weather";
    public static final String API_METHOD = "GET";
    public static final String API_APP_CODE = "yourOwnAppCode"; // 填写申请的appCode
    //api接口地址：https://market.aliyun.com/products/57096001/cmapi010812.html#sku=yuncode481200005

    public static final String API_TEST_IP = "114.242.248.207";

    public static final String INTENT_NAME_IP = "ip";

    public static final String JSON_FILE_PATH = "/json_context.txt";

    private static final String WEATHER_PIC_URL = "http://app1.showapi.com/weather/icon/%s/%s.png";


    ////////////  functions ////////////
    public static String getWeatherPicUrlByCode(String dayOrNight, String weather_pic)
    {
        return String.format(WEATHER_PIC_URL, dayOrNight, weather_pic);
    }

    // 参数 20160629 返回 6/29
    public static String getDate(String day)
    {
        String temp = day.substring(4, 8);
        StringBuilder sb = new StringBuilder(temp);
        sb.insert(2, '/');
        // 从后面删
        if (sb.charAt(3) == '0')
        {
            sb.deleteCharAt(3);
        }
        if (sb.charAt(0) == '0')
        {
            sb.deleteCharAt(0);
        }

        return sb.toString();

    }
}
