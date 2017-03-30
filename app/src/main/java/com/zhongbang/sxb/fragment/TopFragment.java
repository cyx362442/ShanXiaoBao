package com.zhongbang.sxb.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.zhongbang.sxb.R;
import com.zhongbang.sxb.ShareActivity;
import com.zhongbang.sxb.application.MyApplication;
import com.zhongbang.sxb.event.MessageEvent;
import com.zhongbang.sxb.region.City;
import com.zhongbang.sxb.region.CitySelect01Activity;
import com.zhongbang.sxb.service.LocationService;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class TopFragment extends Fragment {
    private final String urlCity = "http://chinazbhf.com:8081/sxb/app/user/districts.app";
    @Bind(R.id.tv_location)
    TextView mTvLocation;
    @Bind(R.id.tv_share)
    TextView mTvShare;
    private City city2;
    private boolean isLocation = true;
    private LocationService locationService;
    private String city = "北京";
    HashMap<String, String> hasMap = new HashMap<>();
    private Intent mIntent;

    public TopFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_top, container, false);
        ButterKnife.bind(this, inflate);
        EventBus.getDefault().register(getContext());
        city2 = new City();
        mTvLocation.setMovementMethod(ScrollingMovementMethod.getInstance());
        return inflate;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 2) {
                if (resultCode == 2) {// 等待传回城市的值ֵ
                    locationService.stop();
                    isLocation = false;
                    city2 = data.getParcelableExtra("city");
                    city = city2.getCity();
                    mTvLocation.setText(city);
                    EventBus.getDefault().post(new MessageEvent(city, "back"));
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isLocation) {
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
        EventBus.getDefault().unregister(getContext());
    }

    @OnClick({R.id.tv_location, R.id.tv_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_location:
                mIntent = new Intent(getActivity(), CitySelect01Activity.class);
                mIntent.putExtra("city", city);
                startActivityForResult(mIntent, 2);
                break;
            case R.id.tv_share:
                mIntent=new Intent(getActivity(), ShareActivity.class);
                startActivity(mIntent);
                break;
        }
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
                city = TextUtils.isEmpty(location.getCity()) ? city : location.getCity();
                try {
                    if (mTvLocation != null) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                mTvLocation.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mTvLocation.setText(city);
                                        EventBus.getDefault().post(new MessageEvent(city, ""));
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
