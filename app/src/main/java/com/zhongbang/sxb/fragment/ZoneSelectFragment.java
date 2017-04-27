package com.zhongbang.sxb.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.zhongbang.sxb.R;
import com.zhongbang.sxb.adapter.ListAdapter;
import com.zhongbang.sxb.bean.CityZone;
import com.zhongbang.sxb.colleciton.WebView_PayActivity;
import com.zhongbang.sxb.event.MessageEvent;
import com.zhongbang.sxb.httputils.DownHTTP;
import com.zhongbang.sxb.httputils.VolleyResultListener;
import com.zhongbang.sxb.region.City;
import com.zhongbang.sxb.webview.Link2Activity;
import com.zhongbang.sxb.webview.WebViewActivity;


import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ZoneSelectFragment extends Fragment implements AdapterView.OnItemClickListener {
    @Bind(R.id.listView)
    ListView mListView;
    private String mCity;
    private final String urlCity="http://chinazbhf.com:8081/sxb/app/user/districts.app";
    private final String urlCityZone="http://chinazbhf.com:8081/SHXBWD/mj/area_good.html?area_id=";
    private HashMap<String,String>map=new HashMap<>();
    private CityZone[] mCities;

    public ZoneSelectFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_zone_select, container, false);
        ButterKnife.bind(this, inflate);
        Bundle bundle = getArguments();
        mCity = bundle.getString("city");
        mListView.setOnItemClickListener(this);
        return inflate;
    }

    @Override
    public void onStart() {
        super.onStart();
        map.clear();
        map.put("city",mCity);
        DownHTTP.postVolley(urlCity, map, new VolleyResultListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                mCities = gson.fromJson(response, CityZone[].class);
                ListAdapter adapter = new ListAdapter(getContext(), mCities);
                mListView.setAdapter(adapter);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String district = mCities[position].getDistrict();
        String cityZone = mCities[position].getId()+"";
        toWebView(district,urlCityZone+cityZone);
    }
    private void toWebView(String contents,String url) {
        Intent intent = new Intent(getActivity(), Link2Activity.class);
        intent.putExtra("title", contents);
        intent.putExtra("url",url);
        startActivity(intent);
    }
}
