package com.zhongbang.sxb.application;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2017-03-22.
 */

public class ExitAppliation extends Application {
    private List<Activity> activityList = new LinkedList();
    private static ExitAppliation instance;
    private ExitAppliation(){}
    public static ExitAppliation getInstance() {
        if (null == instance){
            instance = new ExitAppliation();
        }
        return instance;
    }
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }
    // 遍历所有Activity并finish
    public void exit(){
        for (Activity activity : activityList){
            if(activity!=null){
                activity.finish();
            }
        }
        System.exit(0);
    }
}
