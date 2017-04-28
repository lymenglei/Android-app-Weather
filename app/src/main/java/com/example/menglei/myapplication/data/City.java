package com.example.menglei.myapplication.data;

import java.util.ArrayList;

/**
 * Created by menglei on 17/04/11.
 */

public class City {

    private String name;
    private CurWeather todayWeather;
    private String ipAddress;
    private ArrayList<Day> nextDays = new ArrayList<>();

    public City(String name, String ip, CurWeather today, ArrayList<Day> nextDays)
    {
        this.name = name;
        this.ipAddress = ip;
        this.todayWeather = today;
        this.nextDays = nextDays;
    }

    public String getIpAddress()
    {
        return this.ipAddress;
    }
    public String getName()
    {
        return this.name;
    }

    public CurWeather getCurWeather()
    {
        return this.todayWeather;
    }

    public ArrayList<Day> getNextDaysWeather()
    {
        return this.nextDays;
    }
}
