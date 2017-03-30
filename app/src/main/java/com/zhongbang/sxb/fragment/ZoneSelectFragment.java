package com.zhongbang.sxb.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.zhongbang.sxb.R;
import com.zhongbang.sxb.adapter.ListAdapter;
import com.zhongbang.sxb.bean.CityZone;
import com.zhongbang.sxb.event.MessageEvent;
import com.zhongbang.sxb.httputils.DownHTTP;
import com.zhongbang.sxb.httputils.VolleyResultListener;
import com.zhongbang.sxb.region.City;
import com.zhongbang.sxb.webview.Link2Activity;


import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ZoneSelectFragment extends Fragment {
    @Bind(R.id.listView)
    ListView mListView;
    private String mCity;
    private final String urlCity="http://chinazbhf.com:8081/sxb/app/user/districts.app";
    private HashMap<String,String>map=new HashMap<>();
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
                Log.e("=====",response);
                Gson gson = new Gson();
                CityZone[] cities = gson.fromJson(response, CityZone[].class);
                ListAdapter adapter = new ListAdapter(getContext(), cities);
                mListView.setAdapter(adapter);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void toWebView(String contents) {
        Intent intent = new Intent(getActivity(), Link2Activity.class);
        intent.putExtra("title", contents);
        startActivity(intent);
    }
}
