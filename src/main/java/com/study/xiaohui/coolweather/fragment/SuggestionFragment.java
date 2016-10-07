package com.study.xiaohui.coolweather.fragment;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.study.xiaohui.coolweather.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by xiaohui on 2016/10/2.
 */

public class SuggestionFragment extends Fragment {
    private TextView comfBrf;
    private TextView comContent;
    private TextView cwBrf;
    private TextView cwContent;
    private TextView dressBrf;
    private TextView dressContent;
    private SharedPreferences prefs;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getActivity().getSharedPreferences("weatherData", MODE_PRIVATE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_txt,container,false);
        comfBrf = (TextView) view.findViewById(R.id.comf_brf);
        comContent = (TextView) view.findViewById(R.id.comContent);
        cwContent = (TextView) view.findViewById(R.id.cwContent);
        cwBrf = (TextView) view.findViewById(R.id.cwbrf);
        dressBrf = (TextView) view.findViewById(R.id.dress_brf);
        dressContent = (TextView) view.findViewById(R.id.dressContent);
        comfBrf.setText(prefs.getString("comf_brf", ""));
        comContent.setText(prefs.getString("comf_txt", ""));
        cwBrf.setText(prefs.getString("cw_brf", ""));
        cwContent.setText(prefs.getString("cw_txt", ""));
        dressBrf.setText(prefs.getString("drsg_brf", ""));
        dressContent.setText(prefs.getString("drsg_txt", ""));
        return view;
    }
}
