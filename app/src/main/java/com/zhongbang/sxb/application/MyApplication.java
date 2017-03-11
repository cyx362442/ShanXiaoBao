package com.zhongbang.sxb.application;

import android.app.Application;

import com.zhongbang.sxb.httputils.MyVolley;

/**
 * Created by Administrator on 2017-03-11.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        MyVolley.init(this);//使用http协议必须这里先初始化
    }
}
