package com.zhongbang.sxb;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zhongbang.sxb.fragment.SlidingMain;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.ll_01).setOnClickListener(this);
        findViewById(R.id.ll_02).setOnClickListener(this);
        findViewById(R.id.ll_03).setOnClickListener(this);
        findViewById(R.id.ll_04).setOnClickListener(this);
        findViewById(R.id.ll_05).setOnClickListener(this);
        findViewById(R.id.ll_06).setOnClickListener(this);
        findViewById(R.id.ll_07).setOnClickListener(this);
        findViewById(R.id.ll_08).setOnClickListener(this);
        findViewById(R.id.ll_09).setOnClickListener(this);
        sliding();//滑动条自动滑动
    }
    /**滑动条功能功能*/
    public void sliding(){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction bt = fm.beginTransaction();
        bt.replace(R.id.layout_fragment, new SlidingMain());//替换布局为 片段控件功能
        bt.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_01:
                break;
        }
    }
}
