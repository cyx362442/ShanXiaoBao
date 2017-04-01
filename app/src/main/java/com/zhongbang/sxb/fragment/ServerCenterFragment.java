package com.zhongbang.sxb.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhongbang.sxb.R;
import com.zhongbang.sxb.colleciton.CollectionsActivity;
import com.zhongbang.sxb.colleciton.WebView_PayActivity;
import com.zhongbang.sxb.managercenter.MyManagerActivity;
import com.zhongbang.sxb.webview.Link2Activity;
import com.zhongbang.sxb.webview.WebViewActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServerCenterFragment extends Fragment implements View.OnClickListener {

    private Intent mIntent;

    public ServerCenterFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_server_center, container, false);
        inflate.findViewById(R.id.img_shoukuan).setOnClickListener(this);
        inflate.findViewById(R.id.img_guanli).setOnClickListener(this);
        inflate.findViewById(R.id.img_jiqiren).setOnClickListener(this);
        return inflate;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_jiqiren:
                String webUrl="http://chinazbhf.com:8081/SHXBWD/mj/goods.html?category=robot";
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("title","机器人系统");
                intent.putExtra("url",webUrl);
                startActivity(intent);
                break;
            case R.id.img_shoukuan:
                mIntent = new Intent(getActivity(), CollectionsActivity.class);
                startActivity(mIntent);
                break;
            case R.id.img_guanli:
                mIntent = new Intent(getActivity(), MyManagerActivity.class);
                startActivity(mIntent);
                break;
        }
    }
}
