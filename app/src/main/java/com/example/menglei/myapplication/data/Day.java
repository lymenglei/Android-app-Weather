package com.example.menglei.myapplication.data;

/**
 * Created by menglei on 17/04/11.
 */

public class Day {
    private Weather day;
    private Weather night;
    private String jiangshui;
    private String date;

    public Day(Weather day, Weather night, String jiangshui, String date)
    {
        this.day = day;
        this.night = night;
        this.jiangshui = jiangshui;
        this.date = date;
    }

    public Weather getDay()
    {
        return this.day;
    }

    public Weather getNight()
    {
        return this.night;
    }

    public String getJiangShui()
    {
        return this.jiangshui;
    }

    public String getDate()
    {
        return this.date;
    }
}
