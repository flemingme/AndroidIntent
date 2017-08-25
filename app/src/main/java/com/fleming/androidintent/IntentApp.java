package com.fleming.androidintent;

import android.app.Application;

import com.blankj.utilcode.util.Utils;

/**
 * Created by Fleming on 2017/8/22.
 * Contact me: jialongchen5@gmail.com
 * Github: https://github.com/flemingme
 */

public class IntentApp extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
    }
}
