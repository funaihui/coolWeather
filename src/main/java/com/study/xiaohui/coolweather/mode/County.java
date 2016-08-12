package com.study.xiaohui.coolweather.mode;

/**
 * Created by xiaohui on 2016/8/8.
 */
public class County {
    private int id;
    private String countyName;
    private String countyCode;
    private int cityId;


    public County() {

    }

    public County(int cityId, String countyCode, String countyName, int id) {
        this.cityId = cityId;
        this.countyCode = countyCode;
        this.countyName = countyName;
        this.id = id;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCountyCode() {
        return countyCode;
    }

    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
