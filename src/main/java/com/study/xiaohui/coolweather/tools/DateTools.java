package com.study.xiaohui.coolweather.tools;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by xiaohui on 2016/9/29.
 */

public class DateTools {
    private static final String TAG = "Wizard";
    /**
     * 获得当前日期
     */
    private static String nowDate(){
        String nowDate = "";
        SimpleDateFormat format = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
        format.applyLocalizedPattern("yyyy-MM-dd");
        nowDate = format.format(new Date());
        return nowDate;
    }
    public static String issueTime(Context context){
        boolean flag = false;
        SharedPreferences prefs = context.getSharedPreferences("weatherData",MODE_PRIVATE);
        String loc = prefs.getString("loc","");
        String resultLoc = "";
        if (loc != null && loc != ""){
            resultLoc = loc.substring(0,10);
        }
        flag = resultLoc.equals(nowDate());
        if (flag){
            return loc.substring(11);
        }else {
            return loc;
        }
    }


    //获取日期
    private static int dateTools(Date str) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(str);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    //日期与星期的转换
    private static String weekDay(int day) {
        switch (day) {
            case 1:
                return "周日";
            case 2:
                return "周一";
            case 3:
                return "周二";
            case 4:
                return "周三";
            case 5:
                return "周四";
            case 6:
                return "周五";
            case 7:
                return "周六";
            default:
                return null;
        }
    }

    //把当前的日期传递给日期转换方法，将转换结果作为字符串返回
    public static String getDayOfWeek(String date) {
        int day = 0;
        if (date != null && date != ""){
            java.sql.Date resultDate = java.sql.Date.valueOf(date);
            day= dateTools(resultDate);
        }
        return weekDay(day);
    }
}
