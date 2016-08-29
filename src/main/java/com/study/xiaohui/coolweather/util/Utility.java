package com.study.xiaohui.coolweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.study.xiaohui.coolweather.db.CoolWeatherDB;
import com.study.xiaohui.coolweather.mode.City;
import com.study.xiaohui.coolweather.mode.County;
import com.study.xiaohui.coolweather.mode.Province;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xiaohui on 2016/8/8.
 */
public class Utility {
    private static final String TAG = "WizardFu";
    public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB,
                                                               String response) {
        if (!TextUtils.isEmpty(response)) {
            String[] allProvinces = response.split(",");
            if (allProvinces != null && allProvinces.length > 0) {
                for (String p : allProvinces) {
                    String[] array = p.split("\\|");
                    Province province = new Province();
                    province.setProvinceCode(array[0]);
                    province.setProvinceName(array[1]);
                    coolWeatherDB.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }

    public synchronized static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB,
                                                            String response, int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            String[] allCities = response.split(",");
            if (allCities != null && allCities.length > 0) {
                for (String c : allCities) {
                    String[] array = c.split("\\|");
                    City city = new City();
                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    city.setProvinceId(provinceId);
                    coolWeatherDB.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }

    public static boolean handleCountiesResponse(CoolWeatherDB coolWeatherDB,
                                                 String response, int cityId) {
        if (!TextUtils.isEmpty(response)) {
            String[] allCounties = response.split(",");
            if (allCounties != null && allCounties.length > 0) {
                for (String c : allCounties) {
                    String[] array = c.split("\\|");
                    County county = new County();
                    county.setCountyCode(array[0]);
                    county.setCountyName(array[1]);
                    county.setCityId(cityId);
                    // 将解析出来的数据存储到County表
                    coolWeatherDB.saveCounty(county);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 解析天气的JSON数据
     */
    public static void handleWeatherResponse(Context context, String response) {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String weatherCode = preferences.getString("weather_code", "");
        try {
            JSONObject jsonObject = new JSONObject(response);
            //空气质量
            JSONObject air = jsonObject.getJSONObject("air");
            JSONObject airSite = air.getJSONObject(weatherCode);
            JSONObject airQuality = airSite.getJSONObject("2001006");
            int pm = airQuality.getInt("001");
            Log.i(TAG,"pm--->"+ pm);

            int aqi = airQuality.getInt("002");
           /* //预警信息
            JSONObject alarm = jsonObject.getJSONObject("alarm");
            Log.i(TAG,"alarm--->"+ alarm);

            JSONObject alarmSite = alarm.getJSONObject(weatherCode);
            Log.i(TAG,"alarmSite--->"+ alarmSite);

            JSONArray warning = alarmSite.getJSONArray("1001003");
            Log.i(TAG,"warning--->"+ warning);


            JSONObject warningNumber = warning.getJSONObject(0);
            String warningName = warningNumber.getString("005");
            Log.i(TAG,"warningName--->"+ warningName);
            String warningLevel = warningNumber.getString("007");
            String warningTime = warningNumber.getString("008");
            String warningMessage = warningNumber.getString("009");*/
            //实时天气
            JSONObject observe = jsonObject.getJSONObject("observe");
            JSONObject observeSite = observe.getJSONObject(weatherCode);
            JSONObject liveNumber = observeSite.getJSONObject("1001002");
            int liveWind = liveNumber.getInt("003");
            String liveTime = liveNumber.getString("000");
            String liveWeather = liveNumber.getString("001");
            int liveWeatherCode = liveNumber.getInt("001");
            CoolWeatherDB coolWeatherDB = CoolWeatherDB.getInstance(context);
            String weather = coolWeatherDB.getWeather(liveWeather);
            int liveTemp = liveNumber.getInt("002");
            saveWeatherInfo(context,pm,aqi,liveWind,liveWeatherCode
            ,liveTime,weather,liveTemp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将服务器返回的所有天气信息存储到SharedPreferences文件中。
     */
    public static void saveWeatherInfo(Context context,int pm, int aqi, int liveWind,
                                       int liveWeatherCode, String liveTime, String weather,
                                       int liveTemp) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(context).edit();

        editor.putBoolean("city_selected", true);
        editor.putInt("pm2.5", pm);
        editor.putInt("aqi", aqi);
        editor.putInt("liveWeatherCode", liveWeatherCode);
        /*editor.putString("warningName", warningName);
        editor.putString("warningLevel", warningLevel);
        editor.putString("warningTime", warningTime);
        editor.putString("warningMessage", warningMessage);*/
        editor.putInt("liveWind", liveWind);
        editor.putString("liveTime", liveTime);
        editor.putString("liveWeather", weather);
        editor.putInt("liveTemp", liveTemp);
        editor.apply();
    }

    //空气质量数据处理
    public static String getWeatherQuality(int aqi) {
        String str = null;
        if (aqi >= 0 && aqi <= 50) {
            str = "优";
            return str;
        } else if (aqi >= 51 && aqi <= 100) {
            str = "良";
            return str;
        } else if (aqi >= 101 && aqi <= 150) {
            str = "轻度污染";
            return str;
        } else if (aqi >= 151 && aqi <= 200) {
            str = "中度污染";
            return str;
        } else if (aqi >= 201 && aqi <= 300) {
            str = "重度污染";
            return str;
        } else if (aqi > 300) {
            str = "严重污染";
            return str;
        }
        return str;
    }

    /**
     * 利用 Android-Universal-Image-Loader-master
     * 根据天气编码从网络上获取天气图片，并缓存到本地
     * @param pictureCode：天气编码
     * @param view：需要更新的imageView
     */
    public static void getWeatherPicture(int pictureCode, ImageView view){

        String uri = "http://m.weather.com.cn/img/a"+pictureCode+".gif";
        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)   //内存缓存
                .cacheOnDisk(true)    //硬盘缓存
         .build();

        ImageLoader.getInstance().displayImage(uri,view,displayImageOptions);
    }
}
