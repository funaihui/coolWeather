package com.study.xiaohui.coolweather.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.study.xiaohui.coolweather.R;
import com.study.xiaohui.coolweather.service.AutoUpdateService;
import com.study.xiaohui.coolweather.util.HttpCallbackListener;
import com.study.xiaohui.coolweather.util.HttpUtil;
import com.study.xiaohui.coolweather.util.Utility;

/**
 * Created by xiaohui on 2016/8/9.
 */
public class WeatherActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener{
    private TextView temText;
    private TextView issueTimeText;
    private TextView weatherText;
    private TextView pmText;
    private TextView weatherQuality;
    private ProgressDialog progressDialog;
    private RadioButton location;
    private SwipeRefreshLayout mRefreshLayout;
    private static final String TAG = "WizardFu";
    private static final String ISSUE = "发布";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_layout);

        initView();
        /**
         * 选择要查询的地区
         */
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeatherActivity.this, ChooseAreaActivity.class);
                startActivity(intent);
                finish();
            }
        });
        String countyCode = getIntent().getStringExtra("county_code");
        Log.i("WizardFu", "county_code: "+countyCode);

        String countyName = getIntent().getStringExtra("county_name");
        if (!TextUtils.isEmpty(countyCode)) {
            // 有县级代号时就去查询天气
            Log.i("WizardFu", "isEmpty: " + TextUtils.isEmpty(countyCode));
            location.setText(countyName);
            queryWeatherCode(countyCode);
        } else {
            // 没有县级代号时就直接显示本地天气
            showWeather();
        }
    }

    /**
     * 初始化各个组件
     */
    private void initView() {
        location = (RadioButton) findViewById(R.id.station);
        temText = (TextView) findViewById(R.id.tmp);
        issueTimeText = (TextView) findViewById(R.id.issue_time);
        weatherText = (TextView) findViewById(R.id.wea);
        pmText = (TextView) findViewById(R.id.pm);
        weatherQuality = (TextView) findViewById(R.id.aqi_src);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        mRefreshLayout.setOnRefreshListener(this);
    }

    private void queryWeatherCode(String countyCode) {
        String address = "http://www.weather.com.cn/data/list3/city" +
                countyCode + ".xml";

        queryFromServer(address, "countyCode");

    }

    private void queryWeatherInfo(String weatherCode) {
        String address = "http://apis.baidu.com/tianyiweather/basicforecast/weatherapi?area=" +
                weatherCode;
        Log.i("WizardFu", "address--->" + address);

        queryFromServer(address, "weatherCode");
    }

    /**
     * 根据传入的地址和类型去向服务器查询天气代号或者天气信息。
     */
    private void queryFromServer(final String address, final String type) {
        showProgressDialog();
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                if ("countyCode".equals(type)) {
                    if (!TextUtils.isEmpty(response)) {
                        // 从服务器返回的数据中解析出天气代号
                        String[] array = response.split("\\|");
                        if (array != null && array.length == 2) {
                            String weatherCode = array[1];
                            Log.i("WizardFu", "weatherCode--->" + weatherCode);
                            SharedPreferences.Editor editor = PreferenceManager
                                    .getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather_code", weatherCode);
                            editor.clear();
                            editor.apply();
                            queryWeatherInfo(weatherCode);

                        }
                    }
                } else if ("weatherCode".equals(type)) {
                    Utility.handleWeatherResponse(WeatherActivity.this,
                            response);

                    Log.i("WizardFu", "response--->" + response);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            showWeather();
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //  publishText.setText("同步失败");
                    }
                });
            }
        });
    }

    /**
     * 从SharedPreferences文件中读取存储的天气信息，并显示到界面上。
     */
    private void showWeather() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences preferencesSite = getSharedPreferences("site", 0);
        location.setText(preferencesSite.getString("countryName", ""));
        String temp = String.valueOf(prefs.getInt("liveTemp", 0));
        String weatherAqi = Utility.getWeatherAquality(prefs.getInt("aqi",0));
        temText.setText(temp);
        weatherQuality.setText(weatherAqi);
        issueTimeText.setText(prefs.getString("liveTime","")+ISSUE);
        if (prefs.getString("liveTime","").equals("")){
            Toast.makeText(this,"对不起该站暂无数据，请查询市区天气",Toast.LENGTH_SHORT).show();
        }
        weatherText.setText(prefs.getString("liveWeather",""));
        pmText.setText(String.valueOf(prefs.getInt("pm2.5",0)));
        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);
    }
    /**
    * 显示进度对话框
    */
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在查询天气数据，请稍后...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherCode =preferences.getString("weather_code", "");
        String address = "http://apis.baidu.com/tianyiweather/basicforecast/weatherapi?area=" +
                weatherCode;
        Log.i(TAG, "onRefresh: weatherCode--->"+weatherCode);
        mRefreshLayout.setRefreshing(true);
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {

                Utility.handleWeatherResponse(WeatherActivity.this,response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.setRefreshing(false);
                        showWeather();
                        Toast.makeText(WeatherActivity.this,"以获取最新天气数据",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }
}