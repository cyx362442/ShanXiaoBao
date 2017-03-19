package com.zhongbang.sxb.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhongbang.sxb.LandActivity;
import com.zhongbang.sxb.R;
import com.zhongbang.sxb.webview.Link2Activity;

/**
 * A simple {@link Fragment} subclass.
 */
public class CenterFragment extends Fragment implements View.OnClickListener {
    private final String url="http://chinazbhf.com:8081/SHXBWD/mjgl/sy.html?id=";

    private Intent mIntent;
    private boolean mIsLoad;
    private String mName;

    public CenterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_center, container, false);
        initClick(inflate);
        return inflate;
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences sp = getContext().getSharedPreferences("users", Context.MODE_PRIVATE);
        mIsLoad = sp.getBoolean("isLoad", false);
        mName = sp.getString("name", "");
    }

    private void initClick(View inflate) {
        inflate.findViewById(R.id.ll_01).setOnClickListener(this);
        inflate.findViewById(R.id.ll_02).setOnClickListener(this);
        inflate.findViewById(R.id.ll_03).setOnClickListener(this);
        inflate.findViewById(R.id.ll_04).setOnClickListener(this);
        inflate.findViewById(R.id.ll_05).setOnClickListener(this);
        inflate.findViewById(R.id.ll_06).setOnClickListener(this);
        inflate.findViewById(R.id.ll_07).setOnClickListener(this);
        inflate.findViewById(R.id.ll_08).setOnClickListener(this);
        inflate.findViewById(R.id.ll_09).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_01:
                toWebView("特色美食");
                break;
            case R.id.ll_02:
                toWebView("体闲娱乐");
                break;
            case R.id.ll_03:
                toWebView("美容养生");
                break;
            case R.id.ll_04:
                toWebView("游艇出海");
                break;
            case R.id.ll_05:
                toWebView("精品购物");
                break;
            case R.id.ll_06:
                toWebView("旅游度假");
                break;
            case R.id.ll_07:
                toWebView("机票酒店");
                break;
            case R.id.ll_08:
                toWebView("珠宝饰品");
                break;
            case R.id.ll_09:
                toWebView("建材家居");
                break;
        }
    }

    private void toWebView(String title) {
        if(mIsLoad==false){
            mIntent = new Intent(getActivity(), LandActivity.class);
            startActivity(mIntent);
            return;
        }
        Intent intent = new Intent(getActivity(), Link2Activity.class);
        intent.putExtra("title",title);
        startActivity(intent);
    }
}
