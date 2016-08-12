package com.study.xiaohui.coolweather.mode;

/**
 * Created by xiaohui on 2016/8/10.
 */
public class Weather {
    private int pm;
    private int aqi;
    private String warningName;
    private String warningLevel;
    private String warningTime;
    private String warningMessage;
    private int liveWind;
    private String liveTime;
    private int liveWeather;
    private int liveTemp;
    public Weather(){}

    public Weather(int aqi, int liveTemp, String liveTime, int liveWeather, int liveWind, int pm, String warningLevel, String warningMessage, String warningName, String warningTime) {
        this.aqi = aqi;
        this.liveTemp = liveTemp;
        this.liveTime = liveTime;
        this.liveWeather = liveWeather;
        this.liveWind = liveWind;
        this.pm = pm;
        this.warningLevel = warningLevel;
        this.warningMessage = warningMessage;
        this.warningName = warningName;
        this.warningTime = warningTime;
    }

    public int getAqi() {
        return aqi;
    }

    public void setAqi(int aqi) {
        this.aqi = aqi;
    }

    public int getLiveTemp() {
        return liveTemp;
    }

    public void setLiveTemp(int liveTemp) {
        this.liveTemp = liveTemp;
    }

    public String getLiveTime() {
        return liveTime;
    }

    public void setLiveTime(String liveTime) {
        this.liveTime = liveTime;
    }

    public int getLiveWeather() {
        return liveWeather;
    }

    public void setLiveWeather(int liveWeather) {
        this.liveWeather = liveWeather;
    }

    public int getLiveWind() {
        return liveWind;
    }

    public void setLiveWind(int liveWind) {
        this.liveWind = liveWind;
    }

    public int getPm() {
        return pm;
    }

    public void setPm(int pm) {
        this.pm = pm;
    }

    public String getWarningLevel() {
        return warningLevel;
    }

    public void setWarningLevel(String warningLevel) {
        this.warningLevel = warningLevel;
    }

    public String getWarningMessage() {
        return warningMessage;
    }

    public void setWarningMessage(String warningMessage) {
        this.warningMessage = warningMessage;
    }

    public String getWarningName() {
        return warningName;
    }

    public void setWarningName(String warningName) {
        this.warningName = warningName;
    }

    public String getWarningTime() {
        return warningTime;
    }

    public void setWarningTime(String warningTime) {
        this.warningTime = warningTime;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "aqi=" + aqi +
                ", pm=" + pm +
                ", warningName='" + warningName + '\'' +
                ", warningLevel='" + warningLevel + '\'' +
                ", warningTime='" + warningTime + '\'' +
                ", warningMessage='" + warningMessage + '\'' +
                ", liveWind=" + liveWind +
                ", liveTime='" + liveTime + '\'' +
                ", liveWeather=" + liveWeather +
                ", liveTemp=" + liveTemp +
                '}';
    }
}
