package com.study.xiaohui.coolweather.mode;

/**
 * Created by xiaohui on 2016/8/8.
 */
public class AllCity {

    private String cityId;
    private String city;
    private String country;
    private String lat;
    private String lon;
    private String province;
    public AllCity() {

    }
    public AllCity(String city, String country, String cityId, String lat, String lon, String province) {
        this.city = city;
        this.country = country;
        this.cityId = cityId;
        this.lat = lat;
        this.lon = lon;
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    @Override
    public String toString() {
        return "City{" +
                "city='" + city + '\'' +
                ", cityId='" + cityId + '\'' +
                ", country='" + country + '\'' +
                ", lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                ", province='" + province + '\'' +
                '}';
    }
}
