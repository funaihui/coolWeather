package com.study.xiaohui.coolweather.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.study.xiaohui.coolweather.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends android.app.Fragment {
    private TextView temText;
    private TextView weatherText;
    private TextView pmText;
    private TextView weatherQuality;
    private TextView tmpToday;
    private SharedPreferences prefs;
    public WeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getActivity().getSharedPreferences("weatherData", MODE_PRIVATE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_layout, container, false);
        temText = (TextView) view.findViewById(R.id.tmp);
        weatherText = (TextView) view.findViewById(R.id.wea);
        pmText = (TextView) view.findViewById(R.id.pm);
        weatherQuality = (TextView) view.findViewById(R.id.aqi_src);
        tmpToday = (TextView) view.findViewById(R.id.tmp_today);
        temText.setText(prefs.getString("tmp", ""));
        tmpToday.setText(prefs.getString("min0", "") + "~" + prefs.getString("max0", "")
                + getResources().getString(R.string.tmpC));
        weatherQuality.setText(prefs.getString("qlty", ""));
        weatherText.setText(prefs.getString("cond_txt", ""));
        pmText.setText(prefs.getString("pm25", ""));
        return view;
    }

}
