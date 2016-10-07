package com.study.xiaohui.coolweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.study.xiaohui.coolweather.mode.AllCity;
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

    public void saveAllCity(AllCity city) {
        if (city != null) {
            ContentValues values = new ContentValues();
            values.put("city", city.getCity());
            values.put("country", city.getCountry());
            values.put("province", city.getProvince());
            values.put("cityId", city.getCityId());
            values.put("lat", city.getLat());
            values.put("lon", city.getLon());
            mDatabase.insert("AllCity", null, values);
        }
    }

    /**
     * 根据城市名称，查询本地数据库中城市的ID
     * @param city
     * @return
     */
    public String loadCityID(String city) {
        String selectCityID=null;
        Cursor cursor = mDatabase.query("AllCity", new String[]{"cityId"}, "city=?",
                new String[]{city}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                selectCityID = cursor.getString(cursor.getColumnIndex("cityId"));
            } while (cursor.moveToNext());
        }
        return selectCityID;
    }

    /**
     * 判断是不是第一次启动程序，如果是则向WeatherData数据库中加数据，否则则不用添加
     */
    public boolean isFirst() {
        boolean flag = false;
        Cursor cursor = mDatabase.query("AllCity", null, null, null,
                null, null, null);
        return flag = cursor.moveToFirst();
    }
}
