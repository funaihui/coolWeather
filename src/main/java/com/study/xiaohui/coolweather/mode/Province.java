package com.study.xiaohui.coolweather.mode;

/**
 * Created by xiaohui on 2016/8/8.
 */
public class Province {
    private int id;
    private String provinceName;
    private String provinceCode;

    public Province() {

    }

    public Province(int id, String provinceCode, String provinceName) {
        this.id = id;
        this.provinceCode = provinceCode;
        this.provinceName = provinceName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
}
