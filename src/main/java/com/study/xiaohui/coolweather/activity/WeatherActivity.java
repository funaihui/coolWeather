package com.study.xiaohui.coolweather.activity;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.study.xiaohui.coolweather.R;
import com.study.xiaohui.coolweather.db.CoolWeatherDB;
import com.study.xiaohui.coolweather.fragment.DailyFragment;
import com.study.xiaohui.coolweather.fragment.SuggestionFragment;
import com.study.xiaohui.coolweather.fragment.WeatherFragment;
import com.study.xiaohui.coolweather.service.AutoUpdateService;
import com.study.xiaohui.coolweather.tools.DateTools;
import com.study.xiaohui.coolweather.util.HttpCallbackListener;
import com.study.xiaohui.coolweather.util.HttpUtil;
import com.study.xiaohui.coolweather.util.Utility;

/**
 * Created by xiaohui on 2016/8/9.
 */
public class WeatherActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private TextView issueTimeText;
    private ProgressDialog progressDialog;
    private RadioButton location;
    private SwipeRefreshLayout mRefreshLayout;
    private FragmentTransaction transaction;
    private android.app.FragmentManager manager;
    private CoolWeatherDB mCoolWeatherDB;
    private DrawerLayout drawer;
    private SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mCoolWeatherDB = CoolWeatherDB.getInstance(this);
        Log.i("WizardFu", "onCreate:ifFirst --->" + mCoolWeatherDB.isFirst());
        /**
         * 首次运行软件添加必要的数据库
         */
        if (!mCoolWeatherDB.isFirst()) {
            String address = " https://api.heweather.com/x3/citylist?" +
                    "search=allchina&key=6598f5b8df35402f8ecf1960c8f908c3";
            HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    Log.i("Wizard", "onFinish: ");
                    Utility.handleCitiesResponse(mCoolWeatherDB, response);
                }

                @Override
                public void onError(Exception e) {
                    Log.i("Wizard", "onError: ");
                }
            });
        }
        initView();
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) toolbar.getLayoutParams();
        param.setMargins(0, 25, 0, 0);
        drawer.addDrawerListener(toggle);

        toggle.syncState();
        manager = getFragmentManager();
        transaction = manager.beginTransaction();
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
        String countyName = getIntent().getStringExtra("county_name");
        if (!TextUtils.isEmpty(countyName)) {
            // 有县级代号时就去查询天气
            Log.i("WizardFu", "isEmpty: " + TextUtils.isEmpty(countyName));
            location.setText(countyName);
            queryCityID(countyName);
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
        issueTimeText = (TextView) findViewById(R.id.issue_time);
        prefs= getSharedPreferences("weatherData", MODE_PRIVATE);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        mRefreshLayout.setOnRefreshListener(this);

    }
    private void queryCityID(String countyName) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        String cityID = mCoolWeatherDB.loadCityID(countyName);
        editor.putString("cityID",cityID);
        editor.apply();
        queryWeatherInfo(cityID);
    }

    private void queryWeatherInfo(String cityID) {
        String address = "http://apis.baidu.com/heweather/weather/free?cityid=" +
                cityID;
        Log.i("WizardFu", "address--->" + address);
        queryFromServer(address);
    }

    /**
     * 根据传入的地址和类型去向服务器查询天气代号或者天气信息。
     */
    private void queryFromServer(final String address) {
        showProgressDialog();
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {

                    Utility.handleWeatherResponse(WeatherActivity.this,
                            response);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            showWeather();
                        }
                    });

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
        SharedPreferences preferencesSite = getSharedPreferences("site", 0);
        location.setText(preferencesSite.getString("countryName", ""));
        /**
         * 判断安卓版本是否大于4.1或4.2，大于则允许改变背景
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            drawer.setBackground(todayWeatherImageSet(prefs.getString("cond_txt","")));
            Log.i("WizardFu", "onCreate: beijing"+prefs.getString("cond_txt",""));
        }
        issueTimeText.setText(DateTools.issueTime(WeatherActivity.this) + "发布");
        manager = getFragmentManager();
        transaction = manager.beginTransaction();
        DailyFragment dailyFragment = new DailyFragment();
        SuggestionFragment suggestionFragment = new SuggestionFragment();
        WeatherFragment weatherFragment = new WeatherFragment();
        transaction.replace(R.id.daily_layout, dailyFragment);
        transaction.replace(R.id.weather_fragment, weatherFragment);
        transaction.replace(R.id.suggestion, suggestionFragment);
        transaction.commit();
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
        String cityID = preferences.getString("cityID", "");
        String address = "http://apis.baidu.com/heweather/weather/free?cityid=" +
                cityID;
        Log.i("WizardFu", "weatherCode:onRefresh " + cityID);
        mRefreshLayout.setRefreshing(true);
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.i("WizardFu", "onFinish: onRefresh" + response);
                Utility.handleWeatherResponse(WeatherActivity.this, response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.setRefreshing(false);
                        showWeather();
                        Toast.makeText(WeatherActivity.this, "已获取最新天气数据", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }
    //设置当天天气图片
    private Drawable todayWeatherImageSet(String weather) {
        if (weather.equals("多云")) {
            return getResources().getDrawable(R.mipmap.bg_weather_cloudy);
        } else if (weather.equals("雾")) {
            return getResources().getDrawable(R.mipmap.bg_weather_fog);
        } else if (weather.equals("中雨")) {
            return getResources().getDrawable(R.mipmap.bg_weather_moderaterain);
        } else if (weather.equals("阴")) {
            return getResources().getDrawable(R.mipmap.bg_weather_overcast);
        } else if (weather.equals("雨")) {
            return getResources().getDrawable(R.mipmap.bg_weather_rain);
        } else if (weather.equals("雪")) {
            return getResources().getDrawable(R.mipmap.bg_weather_snow);
        } else if (weather.equals("晴")) {
            return getResources().getDrawable(R.mipmap.bg_weather_sunny);
        } else if (weather.equals("暴雨")) {
            return getResources().getDrawable(R.mipmap.bg_weather_thunderstorm);
        }
        return getResources().getDrawable(R.drawable.content_bg);
    }
}