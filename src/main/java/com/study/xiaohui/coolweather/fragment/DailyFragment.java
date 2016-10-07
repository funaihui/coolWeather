package com.study.xiaohui.coolweather.fragment;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.study.xiaohui.coolweather.R;
import com.study.xiaohui.coolweather.tools.DateTools;
import com.study.xiaohui.coolweather.util.Utility;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by xiaohui on 2016/9/30.
 */

public class DailyFragment extends Fragment {
    private SharedPreferences preferences = null;
    private static final String TAG = "WizardFu";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getActivity().getSharedPreferences("weatherData", MODE_PRIVATE);
        Log.i(TAG, "DailyFragment: onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "DailyFragment: onCreateView");
        View view = inflater.inflate(R.layout.daily_forecast_layout, container, false);
        TextView todayTemp1 = (TextView) view.findViewById(R.id.today_tmp1);
        TextView todayTemp2 = (TextView) view.findViewById(R.id.today_tmp2);
        TextView todayTemp3 = (TextView) view.findViewById(R.id.today_tmp3);
        TextView todayTemp4 = (TextView) view.findViewById(R.id.today_tmp4);
        TextView todayTemp5 = (TextView) view.findViewById(R.id.today_tmp5);
        TextView todayTemp6 = (TextView) view.findViewById(R.id.today_tmp6);
        TextView todayTemp7 = (TextView) view.findViewById(R.id.today_tmp7);
        TextView tomorrow = (TextView) view.findViewById(R.id.tomorrow);
        TextView tomorrow1 = (TextView) view.findViewById(R.id.tomorrow1);
        TextView tomorrow2 = (TextView) view.findViewById(R.id.tomorrow2);
        TextView tomorrow3 = (TextView) view.findViewById(R.id.tomorrow3);
        TextView tomorrow4 = (TextView) view.findViewById(R.id.tomorrow4);
        TextView tomorrow5 = (TextView) view.findViewById(R.id.tomorrow5);
        TextView windSc1 = (TextView) view.findViewById(R.id.wind_sd1);
        TextView windSc2 = (TextView) view.findViewById(R.id.wind_sd2);
        TextView windSc3 = (TextView) view.findViewById(R.id.wind_sd3);
        TextView windSc4 = (TextView) view.findViewById(R.id.wind_sd4);
        TextView windSc5 = (TextView) view.findViewById(R.id.wind_sd5);
        TextView windSc6 = (TextView) view.findViewById(R.id.wind_sd6);
        TextView windSc7 = (TextView) view.findViewById(R.id.wind_sd7);
        ImageView weatherImage1 = (ImageView) view.findViewById(R.id.weather_image1);
        ImageView weatherImage2 = (ImageView) view.findViewById(R.id.weather_image2);
        ImageView weatherImage3 = (ImageView) view.findViewById(R.id.weather_image3);
        ImageView weatherImage4 = (ImageView) view.findViewById(R.id.weather_image4);
        ImageView weatherImage5 = (ImageView) view.findViewById(R.id.weather_image5);
        ImageView weatherImage6 = (ImageView) view.findViewById(R.id.weather_image6);
        ImageView weatherImage7 = (ImageView) view.findViewById(R.id.weather_image7);
        /**
         * 温度
         */
        if (preferences.getString("min0", "") != null && preferences.getString("min0", "") != "") {
            todayTemp1.setText(preferences.getString("min0", "") + "~" + preferences.getString("max0", "")
                    + getResources().getString(R.string.tmpC));
            todayTemp2.setText(preferences.getString("min1", "") + "~" + preferences.getString("max1", "")
                    + getResources().getString(R.string.tmpC));
            todayTemp3.setText(preferences.getString("min2", "") + "~" + preferences.getString("max2", "")
                    + getResources().getString(R.string.tmpC));
            todayTemp4.setText(preferences.getString("min3", "") + "~" + preferences.getString("max3", "")
                    + getResources().getString(R.string.tmpC));
            todayTemp5.setText(preferences.getString("min4", "") + "~" + preferences.getString("max4", "")
                    + getResources().getString(R.string.tmpC));
            todayTemp6.setText(preferences.getString("min5", "") + "~" + preferences.getString("max5", "")
                    + getResources().getString(R.string.tmpC));
            todayTemp7.setText(preferences.getString("min6", "") + "~" + preferences.getString("max6", "")
                    + getResources().getString(R.string.tmpC));
        }
        /**
         * 周几
         */
        tomorrow.setText(DateTools.getDayOfWeek(preferences.getString("date1", "")));
        tomorrow1.setText(DateTools.getDayOfWeek(preferences.getString("date2", "")));
        tomorrow2.setText(DateTools.getDayOfWeek(preferences.getString("date3", "")));
        tomorrow3.setText(DateTools.getDayOfWeek(preferences.getString("date4", "")));
        tomorrow4.setText(DateTools.getDayOfWeek(preferences.getString("date5", "")));
        tomorrow5.setText(DateTools.getDayOfWeek(preferences.getString("date6", "")));
        /**
         * 风力
         */
        windSc1.setText(preferences.getString("sc0", ""));
        windSc2.setText(preferences.getString("sc1", ""));
        windSc3.setText(preferences.getString("sc2", ""));
        windSc4.setText(preferences.getString("sc3", ""));
        windSc5.setText(preferences.getString("sc4", ""));
        windSc6.setText(preferences.getString("sc5", ""));
        windSc7.setText(preferences.getString("sc6", ""));
        /**
         * 天气图标
         */
        Utility.getWeatherPicture(preferences.getString("cond_code", ""), weatherImage1);
        Utility.getWeatherPicture(preferences.getString("code_d1", ""), weatherImage2);
        Utility.getWeatherPicture(preferences.getString("code_d2", ""), weatherImage3);
        Utility.getWeatherPicture(preferences.getString("code_d3", ""), weatherImage4);
        Utility.getWeatherPicture(preferences.getString("code_d4", ""), weatherImage5);
        Utility.getWeatherPicture(preferences.getString("code_d5", ""), weatherImage6);
        Utility.getWeatherPicture(preferences.getString("code_d6", ""), weatherImage7);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
