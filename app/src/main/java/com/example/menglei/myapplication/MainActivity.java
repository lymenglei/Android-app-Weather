package com.example.menglei.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.menglei.myapplication.data.*;
import com.example.menglei.myapplication.utils.*;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * key bind short cut
 *
 *  alt + enter 提示
 *  alt + insert 复写方法
 */


public class MainActivity extends AppCompatActivity {

    // static vars
    public static final String TAG = "ML";
    public static final int MSG_TEST = 0x00ff0001;
    public static final int MSG_RECEIVE_MSG = 0x00ff0002;
    public static final int MSG_REFRESH_UI = 0x00ff0003;
    public static final int MSG_GET_DEVICE_IP = 0x00ff0004;
    public static final int MSG_WRITE_JSON = 0x00ff0005;
    public static final int MSG_GET_JSON_FROM_LOCAL = 0x00ff0006;

    private static String extFileDir = null;
    private static String ip = "";

    private static TextView mtvCityName;
    private static TextView mtvCurTemp;
    private static TextView mtvCurWeather;
    private static ImageView curWeatherImg;

    public static MainHandler mainHandler;

    private SwipeRefreshLayout mSwipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initExtFileDir();

        mainHandler = new MainHandler(this);
        mtvCityName = (TextView)findViewById(R.id.cityName);
        mtvCurTemp = (TextView)findViewById(R.id.curTemp); // 当前温度
        mtvCurWeather = (TextView)findViewById(R.id.curWeather); // 当天温度
        curWeatherImg = (ImageView) findViewById(R.id.imageView);

        updateTextView(mtvCityName, "下拉刷新天气");

        //设置SwipeRefreshLayout
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        initRefreshLayout(mSwipeLayout);
//        startUpdate();
        initWeatherFromLocal();
    }

    private void initExtFileDir()
    {
        File f = this.getExternalFilesDir(null);
        if (f != null)
        {
            try {
                extFileDir = f.getCanonicalPath();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initRefreshLayout(SwipeRefreshLayout mSwipeLayout)
    {
        //设置进度条的颜色主题，最多能设置四种 加载颜色是循环播放的，只要没有完成刷新就会一直循环，holo_blue_bright>holo_green_light>holo_orange_light>holo_red_light
        mSwipeLayout.setColorSchemeColors(Color.BLUE,
                Color.GREEN,
                Color.YELLOW,
                Color.RED);

        // 设置手指在屏幕下拉多少距离会触发下拉刷新
        mSwipeLayout.setDistanceToTriggerSync(300);
        // 设定下拉圆圈的背景
        mSwipeLayout.setProgressBackgroundColorSchemeColor(Color.WHITE);
        // 设置圆圈的大小
        mSwipeLayout.setSize(SwipeRefreshLayout.DEFAULT);
        //设置下拉刷新的监听
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startUpdate();
            }
        });
    }
    public void stopRefreshSwipeLayout()
    {
        mSwipeLayout.setRefreshing(false);
    }

    private void initWeatherFromLocal()
    {
        String json = getJsonString();
        if (json != null)
        {
            MainActivity.myLog("read data from local");
            Message msg = new Message();
            msg.what = MainActivity.MSG_GET_JSON_FROM_LOCAL;
            msg.obj = json;
            MainActivity.mainHandler.sendMessage(msg);
        }
    }


/////////////////////////////// ui ///////////////////////////////////
    public class MainHandler extends Handler {

        private WeakReference<MainActivity> m_wref;

        public MainHandler(MainActivity activity)
        {
            m_wref = new WeakReference<MainActivity>(activity);
        }

        // 此处使用弱引用，两个对象相互引用，释放的时候否则会有问题
        @Override
        public void handleMessage(Message msg) {
            MainActivity hardref = m_wref.get();
            if (hardref != null)
            {
                if (msg.what == MainActivity.MSG_TEST) {
                    Log.i(MainActivity.TAG, "msg = " + msg.what);
                }
                if (msg.what == MainActivity.MSG_RECEIVE_MSG) {
                    String json = (String) msg.obj;
                    hardref.handleReceiveMsg(json);
                    hardref.stopDownloadService();
                    {
                        Message newMsg = new Message();
                        newMsg.what = MainActivity.MSG_WRITE_JSON;
                        newMsg.obj = json;
                        MainActivity.mainHandler.sendMessage(newMsg);
                    }
                }
                if (msg.what == MainActivity.MSG_GET_JSON_FROM_LOCAL) {
                    String json = (String) msg.obj;
                    hardref.handleReceiveMsg(json);
                }
                if (msg.what == MainActivity.MSG_REFRESH_UI) {
                    City city = (City) msg.obj;
                    hardref.setUI(city);
                }
                if (msg.what == MainActivity.MSG_GET_DEVICE_IP) {
                    String ip = (String) msg.obj;
                    hardref.ip = ip;
                    startDownloadService(ip);
                }
                if (msg.what == MainActivity.MSG_WRITE_JSON) {
                    String json = (String) msg.obj;
                    hardref.handleSetJsonString(json);
                }
            }
        }
    }


    /**
     * 查询天气入口函数
     */
    public void startUpdate()
    {
        SystemInfo.getDeviceIP();
    }

    private void startDownloadService(String ip)
    {
        Intent intent = new Intent(this, DownloadService.class);
        intent.putExtra(GlobalData.INTENT_NAME_IP, ip);
        startService(intent);
    }

    private void stopDownloadService()
    {
        Intent intent = new Intent(this, DownloadService.class);
        stopService(intent);
    }

    public static void myLog(String msg) {
        Log.i(MainActivity.TAG, msg);
    }
    public static void myLog(int msg) {
        Log.i(MainActivity.TAG, msg + "");
    }

    private static City parseJson2City(String json,String ip)
    {
        City city = JsonParse.parse(json, ip);
        if (city == null)
            return null;
        MainActivity.myLog(city.getName());
        MainActivity.myLog(city.getCurWeather().aqi);
        MainActivity.myLog(city.getNextDaysWeather().get(1).getJiangShui());
        MainActivity.myLog(city.getNextDaysWeather().get(1).getNight().wind_power);
		/* output:
		 *  北京
			115
			17%
			微风 <5.4m/s
		 */
		return city;
    }

    private void handleReceiveMsg(String json)
    {
        String ip = this.ip;
        City city = MainActivity.parseJson2City(json, ip);
        if (city != null)
        {
            Message msg = new Message();
            msg.what = MainActivity.MSG_REFRESH_UI;
            msg.obj = city;
            MainActivity.mainHandler.sendMessage(msg);
        }else
        {
            MainActivity.myLog("Error. get city a null value");
        }
    }

    public void updateTextView(TextView t, String text)
    {
        t.setText(text);
    }

    public void setUI(City city)
    {
        String tomorrowDayWeather = city.getNextDaysWeather().get(0).getDay().weather;
        String tomorrowNightWeather = city.getNextDaysWeather().get(0).getNight().weather;
        String longText = "城市：" + city.getName() + "\nIP:" + city.getIpAddress() + "\n当前天气：" + city.getCurWeather().weather + "\n当前温度：" + city.getCurWeather().temperature
            + "\n空气状况：" + city.getCurWeather().quality + "\nAPI：" + city.getCurWeather().aqi + "\n明天白天：" + tomorrowDayWeather + "\n明天夜间：" + tomorrowNightWeather
            ;
        updateTextView(mtvCityName, city.getName());
        updateTextView(mtvCurTemp, city.getCurWeather().temperature + "°");
        String todayDayTemp = city.getNextDaysWeather().get(0).getDay().air_temperature;
        String todayNightTemp = city.getNextDaysWeather().get(0).getNight().air_temperature;
        String curWeather = city.getCurWeather().weather + " " + todayDayTemp + "/" + todayNightTemp + "°C    " +
                city.getCurWeather().quality + " " + city.getCurWeather().aqi;
        updateTextView(mtvCurWeather, curWeather);
        String picUrl = GlobalData.getWeatherPicUrlByCode("day", city.getCurWeather().weather_pic);
        setImgByURL(picUrl, curWeatherImg);
        setIncludeUI(city);
        stopRefreshSwipeLayout();
    }

    public void setIncludeUI(City city)
    {
        int layoutCount = GlobalData.SHOW_DAYS;
        int[] layoutsID = new int[]{R.id.include1, R.id.include2, R.id.include3, R.id.include4, R.id.include5};
        for (int i = 0; i < layoutCount; ++i)
        {
            LinearLayout layout = (LinearLayout) findViewById(layoutsID[i]);
            TextView day = (TextView) layout.findViewById(R.id.day);
            ImageView itemImg = (ImageView) layout.findViewById(R.id.itemImg);
            TextView high = (TextView) layout.findViewById(R.id.high);
            TextView low = (TextView) layout.findViewById(R.id.low);

            String highText = city.getNextDaysWeather().get(i).getDay().air_temperature;
            String lowText = city.getNextDaysWeather().get(i).getNight().air_temperature;
            String imgID = city.getNextDaysWeather().get(i).getDay().weather_pic;

            String dayText = GlobalData.getDate(city.getNextDaysWeather().get(i).getDate());
            MainActivity.myLog(dayText);
            day.setText(dayText);
            high.setText(highText);
            low.setText(lowText);
            String picUrl = GlobalData.getWeatherPicUrlByCode("day", imgID);
            setImgByURL(picUrl, itemImg);
        }
        getScreenSize();
    }

    private int getImgID(String name)
    {
        return 1;
    }


    ///////////////////// 系统相关////////////////////
    public void getScreenSize()
    {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        MainActivity.myLog(dm.heightPixels + "=======" + dm.widthPixels);
    }
    public static String getExtFileDir()
    {
        return extFileDir;
    }
    private void handleSetJsonString(String json)
    {
        SystemInfo.writeJsonString(json);
    }
    private String getJsonString()
    {
        try {
            return SystemInfo.readJsonString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 网络下载图片
    private void setImgByURL(String url, ImageView img)
    {
        new NormalLoadPicture().getPicture(url, img);
    }
}
