package com.example.menglei.myapplication.utils;

import com.example.menglei.myapplication.data.City;
import com.example.menglei.myapplication.data.CurWeather;
import com.example.menglei.myapplication.data.Day;
import com.example.menglei.myapplication.data.GlobalData;
import com.example.menglei.myapplication.data.Weather;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by menglei on 17/04/11.
 */

public class JsonParse {
    public static City parse(String json,String ip)
    {
//		System.out.println(json);
        try{
            JSONObject obj = new JSONObject(json);
            int resCode = obj.getInt("showapi_res_code");
            if (resCode == 0)
            {
                return toCity(obj.getJSONObject("showapi_res_body"), ip);
            }else
            {
                return null;
            }
        }catch(JSONException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static City toCity(JSONObject obj, String ip)
    {
        try{
            if (obj == null)
                return null;
            JSONObject objNow = obj.getJSONObject("now");
            if (objNow == null)
            {
                return null;
            }else
            {
                // 获取当前天气信息
                JSONObject aqiDetail = objNow.getJSONObject("aqiDetail");
                String name = aqiDetail.getString("area");

                String quality = aqiDetail.getString("quality");
                int pm10 = aqiDetail.getInt("pm10");
                int pm2_5 = aqiDetail.getInt("pm2_5");
                String weather_code = objNow.getString("weather_code");
                String wind_direction = objNow.getString("wind_direction");//风向
                String shidu = objNow.getString("sd"); // 湿度
                String wind_power = objNow.getString("wind_power");//风力
                int aqi = objNow.getInt("aqi");//空气质量指数，越小越好
                String weather_pic = objNow.getString("weather_pic"); // url
                String weather = objNow.getString("weather");//天气
                String temperature = objNow.getString("temperature");//气温

                CurWeather cur = new CurWeather(quality, aqi, pm10, pm2_5, weather_code, wind_direction, wind_power, shidu, weather_pic, weather, temperature);

                // 获取未来几天的天气
                ArrayList<Day> array = new ArrayList<>();
                for(int i = 1; i <= GlobalData.DAYS; ++i)
                {
                    Day day = getDayWeather(obj, "f" + i);
                    array.add(day);
                }
                return new City(name, ip, cur, array);
            }

        }catch(JSONException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static Day getDayWeather(JSONObject obj, String name) throws JSONException {
        JSONObject info = obj.getJSONObject(name);
        if (info == null)
        {
            return null;
        }else
        {
            String jiangshui = info.getString("jiangshui"); // 降水概率
            String day_weather = info.getString("day_weather");
            String night_weather = info.getString("night_weather");//晚上天气
            String day_weather_code = info.getString("day_weather_code");
            String night_weather_code = info.getString("night_weather_code");//晚上的天气编码
            String day_wind_power = info.getString("day_wind_power");
            String night_wind_power = info.getString("night_wind_power");//晚上风力编号
            String day_wind_direction = info.getString("day_wind_direction");
            String night_wind_direction = info.getString("night_wind_direction");//晚上风向编号
            String day_air_temperature = info.getString("day_air_temperature");
            String night_air_temperature = info.getString("night_air_temperature");//晚上天气温度(摄氏度)
            String day_weather_pic = info.getString("day_weather_pic");
            String night_weather_pic = info.getString("night_weather_pic");//晚上天气图标
            String date = info.getString("day"); // 当前天，日期

            Weather day = new Weather(day_weather, day_weather_code, day_wind_power, day_wind_direction, day_air_temperature, day_weather_pic);
            Weather night = new Weather(night_weather, night_weather_code, night_wind_power, night_wind_direction, night_air_temperature, night_weather_pic);
            return new Day(day, night, jiangshui, date);
        }
    }

}
