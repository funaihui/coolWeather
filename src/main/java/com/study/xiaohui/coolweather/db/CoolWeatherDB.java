package com.study.xiaohui.coolweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.study.xiaohui.coolweather.mode.City;
import com.study.xiaohui.coolweather.mode.County;
import com.study.xiaohui.coolweather.mode.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaohui on 2016/8/8.
 */
public class CoolWeatherDB {
    public static final String DB_NAME = "cool_weather";
    public static final int VERSION = 1;
    private static CoolWeatherDB coolWeatherDB;
    private SQLiteDatabase mDatabase;
    private static final String TAG = "WizardFu";
    private CoolWeatherDB(Context context) {
        CoolWeatherOpenHelper coolWeatherOpenHelper = new CoolWeatherOpenHelper(context,
                DB_NAME, null, VERSION);
        mDatabase = coolWeatherOpenHelper.getWritableDatabase();
    }

    public synchronized static CoolWeatherDB getInstance(Context context) {
        if (coolWeatherDB == null) {
            coolWeatherDB = new CoolWeatherDB(context);
        }
        return coolWeatherDB;
    }

    public void saveProvince(Province province) {
        if (province != null) {
            ContentValues values = new ContentValues();
            values.put("province_name", province.getProvinceName());
            values.put("province_code", province.getProvinceCode());
            mDatabase.insert("Province", null, values);
        }
    }

    public List<Province> loadProvince() {
        List<Province> provinceList = new ArrayList<>();
        Cursor cursor = mDatabase.query("Province", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                provinceList.add(province);
            } while (cursor.moveToNext());
        }
        return provinceList;
    }

    public void saveCity(City city) {
        if (city != null) {
            ContentValues values = new ContentValues();
            values.put("city_name", city.getCityName());
            values.put("city_code", city.getCityCode());
            values.put("province_id", city.getProvinceId());
            mDatabase.insert("City", null, values);
        }
    }

    public List<City> loadCity(int provinceId) {
        List<City> cityList = new ArrayList<>();
        Cursor cursor = mDatabase.query("City", null, "province_id = ?",
                new String[]{String.valueOf(provinceId)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setProvinceId(cursor.getInt(cursor.getColumnIndex("province_id")));
                cityList.add(city);
            } while (cursor.moveToNext());
        }
        return cityList;
    }

    public void saveCounty(County county) {
        if (county != null) {
            ContentValues values = new ContentValues();
            values.put("county_name", county.getCountyName());
            values.put("county_code", county.getCountyCode());
            values.put("city_id", county.getCityId());
            mDatabase.insert("county", null, values);
        }
    }

    public List<County> loadCounty(int cityId) {
        List<County> countyList = new ArrayList<>();
        Cursor cursor = mDatabase.query("County", null, "city_id = ?",
                new String[]{String.valueOf(cityId)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                County county = new County();
                county.setId(cursor.getInt(cursor.getColumnIndex("id")));
                county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
                county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
                county.setCityId(cursor.getInt(cursor.getColumnIndex("city_id")));
                countyList.add(county);
            } while (cursor.moveToNext());
        }
        return countyList;
    }

    /*//保存天气现象数据
    public void saveWeather() {
        ContentValues values = new ContentValues();
        values.put("weather_code", "00");
        values.put("weather_name", "晴");
        mDatabase.insert("WeatherData", null, values);

        values.put("weather_code", "01");
        values.put("weather_name", "多云");
        mDatabase.insert("WeatherData", null, values);

        values.put("weather_code", "02");
        values.put("weather_name", "阴");
        mDatabase.insert("WeatherData", null, values);

        values.put("weather_code", "03");
        values.put("weather_name", "阵雨");
        mDatabase.insert("WeatherData", null, values);

        values.put("weather_code", "04");
        values.put("weather_name", "雷阵雨");
        mDatabase.insert("WeatherData", null, values);

        values.put("weather_code", "05");
        values.put("weather_name", "雷阵雨伴有冰雹");
        mDatabase.insert("WeatherData", null, values);

        values.put("weather_code", "06");
        values.put("weather_name", "雨夹雪");
        mDatabase.insert("WeatherData", null, values);

        values.put("weather_code", "07");
        values.put("weather_name", "小雨");
        mDatabase.insert("WeatherData", null, values);

        values.put("weather_code", "08");
        values.put("weather_name", "中雨");
        mDatabase.insert("WeatherData", null, values);

        values.put("weather_code", "09");
        values.put("weather_name", "大雨");
        mDatabase.insert("WeatherData", null, values);

        values.put("weather_code", "10");
        values.put("weather_name", "暴雨");
        mDatabase.insert("WeatherData", null, values);

        values.put("weather_code", "11");
        values.put("weather_name", "大暴雨");
        mDatabase.insert("WeatherData", null, values);

        values.put("weather_code", "12");
        values.put("weather_name", "特大暴雨");
        mDatabase.insert("WeatherData", null, values);

        values.put("weather_code", "13");
        values.put("weather_name", "阵雪");
        mDatabase.insert("WeatherData", null, values);

        values.put("weather_code", "14");
        values.put("weather_name", "小雪");
        mDatabase.insert("WeatherData", null, values);

        values.put("weather_code", "15");
        values.put("weather_name", "中雪");
        mDatabase.insert("WeatherData", null, values);

        values.put("weather_code", "16");
        values.put("weather_name", "大雪");
        mDatabase.insert("WeatherData", null, values);

        values.put("weather_code", "17");
        values.put("weather_name", "暴雪");
        mDatabase.insert("WeatherData", null, values);

        values.put("weather_code", "18");
        values.put("weather_name", "雾");
        mDatabase.insert("WeatherData", null, values);

        values.put("weather_code", "19");
        values.put("weather_name", "冻雨");
        mDatabase.insert("WeatherData", null, values);

        values.put("weather_code", "20");
        values.put("weather_name", "沙尘暴");
        mDatabase.insert("WeatherData", null, values);

        values.put("weather_code", "21");
        values.put("weather_name", "小到中雨");
        mDatabase.insert("WeatherData", null, values);

        values.put("weather_code", "22");
        values.put("weather_name", "中到大雨");
        mDatabase.insert("WeatherData", null, values);

        values.put("weather_code", "23");
        values.put("weather_name", "大到暴雨");
        mDatabase.insert("WeatherData", null, values);

        values.put("weather_code", "24");
        values.put("weather_name", "暴雨到大暴雨");
        mDatabase.insert("WeatherData", null, values);

        values.put("weather_code", "25");
        values.put("weather_name", "大暴雨到特大暴雨");
        mDatabase.insert("WeatherData", null, values);

        values.put("weather_code", "26");
        values.put("weather_name", "小到中雪");
        mDatabase.insert("WeatherData", null, values);

        values.put("weather_code", "27");
        values.put("weather_name", "中雪到大雪");
        mDatabase.insert("WeatherData", null, values);

        values.put("weather_code", "28");
        values.put("weather_name", "大雪到暴雪");
        mDatabase.insert("WeatherData", null, values);

        values.put("weather_code", "29");
        values.put("weather_name", "浮尘");
        mDatabase.insert("WeatherData", null, values);

        values.put("weather_code", "30");
        values.put("weather_name", "扬尘");
        mDatabase.insert("WeatherData", null, values);

        values.put("weather_code", "31");
        values.put("weather_name", "强沙尘暴");
        mDatabase.insert("WeatherData", null, values);

        values.put("weather_code", "53");
        values.put("weather_name", "霾");
        mDatabase.insert("WeatherData", null, values);

        values.put("weather_code", "99");
        values.put("weather_name", "无");
        mDatabase.insert("WeatherData", null, values);

        values.put("weather_code", "32");
        values.put("weather_name", "浓雾");
        mDatabase.insert("WeatherData", null, values);

        values.put("weather_code", "49");
        values.put("weather_name", "强浓雾");
        mDatabase.insert("WeatherData", null, values);

        values.put("weather_code", "54");
        values.put("weather_name", "中度霾");
        mDatabase.insert("WeatherData", null, values);

        values.put("weather_code", "55");
        values.put("weather_name", "重度霾");
        mDatabase.insert("WeatherData", null, values);

        values.put("weather_code", "56");
        values.put("weather_name", "严重霾");
        mDatabase.insert("WeatherData", null, values);

        values.put("weather_code", "57");
        values.put("weather_name", "大雾");
        mDatabase.insert("WeatherData", null, values);

        values.put("weather_code", "58");
        values.put("weather_name", "特强浓雾");
        mDatabase.insert("WeatherData", null, values);

        values.put("weather_code", "301");
        values.put("weather_name", "雨");
        mDatabase.insert("WeatherData", null, values);

        values.put("weather_code", "302");
        values.put("weather_name", "雪");
        mDatabase.insert("WeatherData", null, values);

    }*/

    public String getWeather(String weatherCode) {
        Cursor cursor = mDatabase.query("WeatherData", null, "weather_code = ?",
                new String[]{String.valueOf(weatherCode)}, null, null, null);
        String weather = null;
        if (cursor.moveToFirst()) {
            do {
                weather = cursor.getString(cursor.getColumnIndex("weather_name"));
            } while (cursor.moveToNext());

        }

        return weather;
    }

    /**
     * 判断是不是第一次启动程序，如果是则向WeatherData数据库中加数据，否则则不用添加
     *//*
    public void isFirst() {
        Cursor cursor = mDatabase.query("WeatherData", null, null, null,
                null, null, null);
        if (!cursor.moveToFirst()) {
            do {
                saveWeather();
            } while (cursor.moveToNext());
        }
    }*/
}
