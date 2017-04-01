package com.zhongbang.sxb.region;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhongbang.sxb.R;

import java.util.ArrayList;

public class CitySelect01Activity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_back, btn_right, btn_submit;
    private ListView lv_city;
    private ArrayList<MyRegion> regions;

    private CityAdapter adapter;
    private static int PROVINCE = 0x00;
    private static int CITY = 0x01;
    private static int DISTRICT = 0x02;
    private CityUtils util;

    private TextView[] tvs = new TextView[3];
    private int[] ids = { R.id.rb_province, R.id.rb_city, R.id.rb_district };

    private City city;
    int last, current;
    private String mFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_city_select01);
        viewInit();
    }
    private void viewInit() {

        city = new City();
        Intent in = getIntent();
        city = in.getParcelableExtra("city");
        mFrom = in.getStringExtra("from");
        for (int i = 0; i < tvs.length; i++) {
            tvs[i] = (TextView) findViewById(ids[i]);
            tvs[i].setOnClickListener(this);
        }

        if(city==null){
            city = new City();
            city.setProvince("");
            city.setCity("");
            city.setDistrict("");
        }else{
            if(city.getProvince()!=null&&!city.getProvince().equals("")){
                tvs[0].setText(city.getProvince());
            }
            if(city.getCity()!=null&&!city.getCity().equals("")){
                tvs[1].setText(city.getCity());
            }
            if(city.getDistrict()!=null&&!city.getDistrict().equals("")){
                tvs[2].setText(city.getDistrict());
            }
        }

        btn_back = (Button) findViewById(R.id.btn_back);
        btn_right = (Button) findViewById(R.id.btn_right);
        btn_right.setText("确认");

        //--------单城市就把完成按钮和容纳多城市的控件隐藏
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setVisibility(View.GONE);
        findViewById(R.id.scrollview).setVisibility(View.GONE);


        util = new CityUtils(this, hand);
        util.initProvince();
        tvs[current].setBackgroundColor(0xff999999);
        lv_city = (ListView) findViewById(R.id.lv_city);

        regions = new ArrayList<MyRegion>();
        adapter = new CityAdapter(this);
        lv_city.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        lv_city.setOnItemClickListener(onItemClickListener);
        btn_back.setOnClickListener(this);
        btn_right.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
    }
    @SuppressLint("HandlerLeak")
    Handler hand = new Handler() {
        @SuppressWarnings("unchecked")
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {

                case 1:
                    System.out.println("省份列表what======" + msg.what);

                    regions = (ArrayList<MyRegion>) msg.obj;
                    adapter.clear();
                    adapter.addAll(regions);
                    adapter.update();
                    break;

                case 2:
                    System.out.println("城市列表what======" + msg.what);
                    regions = (ArrayList<MyRegion>) msg.obj;
                    adapter.clear();
                    adapter.addAll(regions);
                    adapter.update();
                    break;

                case 3:
                    System.out.println("区/县列表what======" + msg.what);
                    regions = (ArrayList<MyRegion>) msg.obj;
                    adapter.clear();
                    adapter.addAll(regions);
                    adapter.update();
                    break;
            }
        }
    };
    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {

            if (current == PROVINCE) {
                String newProvince = regions.get(arg2).getName();
                if (!newProvince.equals(city.getProvince())) {
                    city.setProvince(newProvince);
                    tvs[0].setText(regions.get(arg2).getName());
                    city.setRegionId(regions.get(arg2).getId());
                    city.setProvinceCode(regions.get(arg2).getId());
                    city.setCityCode("");
                    city.setDistrictCode("");
                    tvs[1].setText("市");
                    if(TextUtils.isEmpty(mFrom)||!mFrom.equals("location")){
                        tvs[2].setText("区");
                    }else{
                        tvs[2].setText("…");
                    }
                }

                current = 1;
                //点击省份列表中的省份就初始化城市列表
                util.initCities(city.getProvinceCode());
            } else if (current == CITY) {
                String newCity = regions.get(arg2).getName();
                if (!newCity.equals(city.getCity())) {
                    city.setCity(newCity);
                    tvs[1].setText(regions.get(arg2).getName());
                    city.setRegionId(regions.get(arg2).getId());
                    city.setCityCode(regions.get(arg2).getId());
                    city.setDistrictCode("");
                    if(TextUtils.isEmpty(mFrom)||!mFrom.equals("location")){
                        tvs[2].setText("区");
                    }else{
                        tvs[2].setText("…");
                    }
                }

                /**点击城市列表中的城市就初始化区县列表*/
                if(TextUtils.isEmpty(mFrom)||!mFrom.equals("location")){
                    util.initDistricts(city.getCityCode());
                    current = 2;
                }

            }
            else if (current == DISTRICT) {
                current = 2;
                city.setDistrictCode(regions.get(arg2).getId());
                city.setRegionId(regions.get(arg2).getId());
                city.setDistrict(regions.get(arg2).getName());
                tvs[2].setText(regions.get(arg2).getName());

            }
            tvs[last].setBackgroundColor(Color.WHITE);
            tvs[current].setBackgroundColor(Color.GRAY);
            last = current;
        }
    };
    class CityAdapter extends ArrayAdapter<MyRegion> {

        LayoutInflater inflater;

        public CityAdapter(Context con) {
            super(con, 0);
            inflater = LayoutInflater.from(CitySelect01Activity.this);
        }

        @Override
        public View getView(int arg0, View v, ViewGroup arg2) {
            v = inflater.inflate(R.layout.city_item, null);
            TextView tv_city = (TextView) v.findViewById(R.id.tv_city);
            tv_city.setText(getItem(arg0).getName());
            return v;
        }

        public void update() {
            this.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back://返回按钮监听
                finish();
                break;
            case R.id.btn_right://确认按钮监听

                Intent in = new Intent();
                in.putExtra("city", city);
                setResult(2,in);
                finish();
                break;
        }
        if (ids[0] == v.getId()) {
            current = 0;
            util.initProvince();
            tvs[last].setBackgroundColor(Color.WHITE);
            tvs[current].setBackgroundColor(Color.GRAY);
            last = current;
        } else if (ids[1] == v.getId()) {
            if (city.getProvinceCode() == null|| city.getProvinceCode().equals("")) {
                current = 0;
                Toast.makeText(CitySelect01Activity.this, "您还没有选择省份",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            util.initCities(city.getProvinceCode());
            current = 1;
            tvs[last].setBackgroundColor(Color.WHITE);
            tvs[current].setBackgroundColor(Color.GRAY);
            last = current;
        } else if (ids[2] == v.getId()) {
            if (city.getProvinceCode() == null
                    || city.getProvinceCode().equals("")) {
                Toast.makeText(CitySelect01Activity.this, "您还没有选择省份",
                        Toast.LENGTH_SHORT).show();
                current = 0;
                util.initProvince();
                return;
            } else if (city.getCityCode() == null
                    || city.getCityCode().equals("")) {
                Toast.makeText(CitySelect01Activity.this, "您还没有选择城市",
                        Toast.LENGTH_SHORT).show();
                current = 1;
                util.initCities(city.getProvince());
                return;
            }
            current = 2;
            util.initDistricts(city.getCityCode());
            tvs[last].setBackgroundColor(Color.WHITE);
            tvs[current].setBackgroundColor(Color.GRAY);
            last = current;
        }
    }
}
