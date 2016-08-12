package com.study.xiaohui.coolweather.mode;

/**
 * Created by xiaohui on 2016/8/8.
 */
public class City {
    private int id;
    private String cityName;
    private String cityCode;
    private int provinceId;

    public City() {

    }

    public City(String cityCode, String cityName, int id, int provinceId) {
        this.cityCode = cityCode;
        this.cityName = cityName;
        this.id = id;
        this.provinceId = provinceId;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }
}
