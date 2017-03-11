package com.zhongbang.sxb;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zhongbang.sxb.fragment.SlidingMain;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sliding();//滑动条自动滑动
    }
    /**滑动条功能功能*/
    public void sliding(){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction bt = fm.beginTransaction();
        bt.replace(R.id.layout_fragment, new SlidingMain());//替换布局为 片段控件功能
        bt.commit();
    }
}
