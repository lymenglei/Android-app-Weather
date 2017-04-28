package com.example.menglei.myapplication.data;

/**
 * Created by menglei on 17/04/11.
 */

public class CurWeather {
    public String quality;
    public int aqi;
    public int pm10;
    public int pm2_5;
    public String weather_code;
    public String wind_direction;
    public String wind_power;
    public String shidu;
    public String weather_pic;
    public String weather;
    public String temperature;

    public CurWeather(String quality, int aqi, int pm10, int pm2_5, String weather_code, String wind_direction, String wind_power,
                      String shidu, String weather_pic, String weather, String temperature)
    {
        this.quality = quality;
        this.aqi = aqi;
        this.pm10 = pm10;
        this.pm2_5 = pm2_5;
        this.weather_code = weather_code;
        this.wind_direction = wind_direction;
        this.wind_power = wind_power;
        this.shidu = shidu;
        this.weather_pic = weather_pic;
        this.weather = weather;
        this.temperature = temperature;
    }
}
