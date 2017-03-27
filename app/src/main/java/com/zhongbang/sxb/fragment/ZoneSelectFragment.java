package com.zhongbang.sxb.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.zhongbang.sxb.R;
import com.zhongbang.sxb.application.MyApplication;
import com.zhongbang.sxb.baidu.LocationActivity;
import com.zhongbang.sxb.service.LocationService;
import com.zhongbang.sxb.webview.Link2Activity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ZoneSelectFragment extends Fragment {
    @Bind(R.id.img_huli)
    ImageView mImgHuli;
    @Bind(R.id.img_siming)
    ImageView mImgSiming;
    @Bind(R.id.img_jimei)
    ImageView mImgJimei;
    @Bind(R.id.tvLocation)
    TextView mTvLocation;

    private LocationService locationService;

    public ZoneSelectFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_zone_select, container, false);
        ButterKnife.bind(this, inflate);
        mTvLocation.setMovementMethod(ScrollingMovementMethod.getInstance());
        return inflate;
    }

    @Override
    public void onStart() {
        super.onStart();
        // -----------location config ------------
        locationService = ((MyApplication) getActivity().getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        //注册监听
        int type = getActivity().getIntent().getIntExtra("from", 0);
        if (type == 0) {
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        } else if (type == 1) {
            locationService.setLocationOption(locationService.getOption());
        }
        locationService.start();// 定位SDK
    }

    @Override
    public void onStop() {
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.img_huli, R.id.img_siming, R.id.img_jimei})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_huli:
//                toWebView("湖里区");
                Intent intent = new Intent(getActivity(), LocationActivity.class);
                startActivity(intent);
                break;
            case R.id.img_siming:
                toWebView("思明区");
                break;
            case R.id.img_jimei:
                toWebView("集美区");
                break;
        }
    }

    private void toWebView(String contents) {
        Intent intent = new Intent(getActivity(), Link2Activity.class);
        intent.putExtra("title", contents);
        startActivity(intent);
    }

    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                final String city = location.getCity();
                try {
                    if (mTvLocation != null){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                mTvLocation.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mTvLocation.setText(city);
                                    }
                                });
                            }
                        }).start();
                    }
                    //LocationResult.setText(str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        public void onConnectHotSpotMessage(String s, int i) {
        }
    };
}
