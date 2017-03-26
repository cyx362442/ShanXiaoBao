package com.zhongbang.sxb.application;

import android.app.Application;
import android.app.Service;
import android.os.Vibrator;

import com.baidu.mapapi.SDKInitializer;
import com.zhongbang.sxb.httputils.MyVolley;
import com.zhongbang.sxb.service.LocationService;

/**
 * Created by Administrator on 2017-03-11.
 */

public class MyApplication extends Application {
    public LocationService locationService;
    public Vibrator mVibrator;
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        MyVolley.init(this);//使用http协议必须这里先初始化
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());
    }
}
