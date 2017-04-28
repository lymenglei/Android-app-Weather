package com.example.menglei.myapplication.data;

/**
 * Created by menglei on 17/04/11.
 */

public class Weather {

    public String weather; // 天气
    public String weather_code;
    public String wind_power;
    public String wind_direction;
    public String air_temperature;
    public String weather_pic;

    public Weather(String weather, String weather_code, String wind_power, String wind_direction, String air_temperature, String weather_pic)
    {
        this.weather = weather;
        this.weather_code = weather_code;
        this.wind_power = wind_power;
        this.wind_direction = wind_direction;
        this.air_temperature = air_temperature;
        this.weather_pic = weather_pic;
    }
}
