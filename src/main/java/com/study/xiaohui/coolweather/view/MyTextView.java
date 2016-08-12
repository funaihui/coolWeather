package com.study.xiaohui.coolweather.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by xiaohui on 2016/8/9.
 */
public class MyTextView extends TextView{
    public MyTextView(Context context) {
        super(context);
        Init(context);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Init(context);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Init(context);
    }
    public void Init(Context context){
        Typeface font = Typeface.createFromAsset(context.getAssets(),"fonts/Roboto-Thin.ttf");
        setTypeface(font);
    }
}
