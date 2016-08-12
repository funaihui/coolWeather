package com.study.xiaohui.coolweather.util;

/**
 * Created by xiaohui on 2016/8/8.
 */
public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
