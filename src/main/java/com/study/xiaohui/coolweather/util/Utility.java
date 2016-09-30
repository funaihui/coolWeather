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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        SharedPreferences weatherPreferences = context.getSharedPreferences("weatherData",Context.MODE_PRIVATE);
        SharedPreferences.Editor dataEdit = weatherPreferences.edit();
        List<Map<String,String>> list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray heWeather = jsonObject.getJSONArray("HeWeather data service 3.0");
            JSONObject weatherData = heWeather.getJSONObject(0);
            JSONObject aqiTotal = weatherData.getJSONObject("aqi");
            JSONObject city = aqiTotal.getJSONObject("city");
            String qlty = city.getString("qlty");
            dataEdit.putString("qlty",qlty);
            String pm25 = city.getString("pm25");
            dataEdit.putString("pm25",pm25);
            JSONObject basic = weatherData.getJSONObject("basic");
            JSONObject update = basic.getJSONObject("update");
            String loc = update.getString("loc");
            dataEdit.putString("loc",loc);
            /**
             * 获取dailyForecast
             */
            JSONArray dailyForecast = weatherData.getJSONArray("daily_forecast");
            for (int i = 0; i < dailyForecast.length(); i++) {
                JSONObject oneDayForecast = dailyForecast.getJSONObject(i);
                Map<String,String> map = new HashMap<>();
                String date = oneDayForecast.getString("date");
                map.put("date",date);
                JSONObject tmp = oneDayForecast.getJSONObject("tmp");
                JSONObject cond = oneDayForecast.getJSONObject("cond");
                String code_d = cond.getString("code_d");
                map.put("code_d",code_d);
                String max= tmp.getString("max");
                map.put("max",max);
                String min = tmp.getString("min");
                map.put("min",min);
                JSONObject wind = oneDayForecast.getJSONObject("wind");
                String sc = wind.getString("sc");
                map.put("sc",sc);
                list.add(map);
            }
            /**
             * 将获取的数据放在SharePreference中
             */
            for (int i = 0; i <list.size() ; i++) {
                dataEdit.putString("date"+i,list.get(i).get("date"));
                dataEdit.putString("max"+i,list.get(i).get("max"));
                dataEdit.putString("min"+i,list.get(i).get("min"));
                dataEdit.putString("sc"+i,list.get(i).get("sc"));
                dataEdit.putString("code_d"+i,list.get(i).get("code_d"));
            }
            /**
             * 获取实时天气并将获取的数据放在SharePreference中
             */
            JSONObject now = weatherData.getJSONObject("now");
            JSONObject cond = now.getJSONObject("cond");
            String tmp = now.getString("tmp");
            String cond_txt = cond.getString("txt");
            String cond_code = cond.getString("code");
            dataEdit.putString("cond_txt",cond_txt);
            dataEdit.putString("tmp",tmp);
            dataEdit.putString("cond_code",cond_code);
            /**
             * 获取天气指数并将获取的数据放在SharePreference中
             */
            JSONObject suggestion = weatherData.getJSONObject("suggestion");
            JSONObject comf = suggestion.getJSONObject("comf");
            String comf_brf = comf.getString("brf");
            String comf_txt = comf.getString("txt");
            Log.i(TAG, "comf_brf: "+comf_brf);
            dataEdit.putString("comf_brf",comf_brf);
            dataEdit.putString("comf_txt",comf_txt);
            JSONObject cw = suggestion.getJSONObject("cw");
            String cw_brf = cw.getString("brf");
            String cw_txt = cw.getString("txt");
            dataEdit.putString("cw_brf",cw_brf);
            dataEdit.putString("cw_txt",cw_txt);
            JSONObject drsg = suggestion.getJSONObject("drsg");
            String drsg_brf = drsg.getString("brf");
            String drsg_txt = drsg.getString("txt");
            dataEdit.putString("drsg_brf",drsg_brf);
            dataEdit.putString("drsg_txt",drsg_txt);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        dataEdit.apply();
    }

    /**
     * 将服务器返回的所有天气信息存储到SharedPreferences文件中。
     *//*
    public static void saveWeatherInfo(Context context,int pm, int aqi, int liveWind,
                                       int liveWeatherCode, String liveTime, String weather,
                                       int liveTemp) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(context).edit();

        editor.putBoolean("city_selected", true);
        editor.putInt("pm2.5", pm);
        editor.putInt("aqi", aqi);
        editor.putInt("liveWeatherCode", liveWeatherCode);
        *//*editor.putString("warningName", warningName);
        editor.putString("warningLevel", warningLevel);
        editor.putString("warningTime", warningTime);
        editor.putString("warningMessage", warningMessage);*//*
        editor.putInt("liveWind", liveWind);
        editor.putString("liveTime", liveTime);
        editor.putString("liveWeather", weather);
        editor.putInt("liveTemp", liveTemp);
        editor.apply();
    }
*/
    /*//空气质量数据处理
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
    }*/

    /**
     * 利用 Android-Universal-Image-Loader-master
     * 根据天气编码从网络上获取天气图片，并缓存到本地
     * @param pictureCode：天气编码
     * @param view：需要更新的imageView
     */
    public static void getWeatherPicture(String pictureCode, ImageView view){

        String uri = "http://files.heweather.com/cond_icon/"+pictureCode+".png";
        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)   //内存缓存
                .cacheOnDisk(true)    //硬盘缓存
         .build();

        ImageLoader.getInstance().displayImage(uri,view,displayImageOptions);
    }
}
