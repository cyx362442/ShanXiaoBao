package com.zhongbang.sxb.account;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.zhongbang.sxb.R;
import com.zhongbang.sxb.httputils.DownHTTP;
import com.zhongbang.sxb.httputils.VolleyResultListener;
import com.zhongbang.sxb.region.City;
import com.zhongbang.sxb.region.CitySelect01Activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class BankconfirmActivity extends AppCompatActivity implements View.OnClickListener {
    private HashMap<String, String>hashMap_bank=new HashMap<String, String>();
    private String province_name;
    private String mPhone;
    private String city_name;
    private RelativeLayout mLoading;
    private Spinner mSpinner;
    private EditText mName;
    private EditText mBank_number;
    private String bank_name="";//所属银行
    private EditText mSub_bank;
    private TextView mProvince;
    private TextView mCity;
    private String[] mBanks;
    private ImageView mImage_confirm;
    private TextView mText_confirm;
    private Intent mIntent;
    private String mAudit;
    private TextView mNext;
    private AnimationDrawable mDrawable;
    private ImageView mImage_load;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bankconfirm);
        SharedPreferences sp = getSharedPreferences("users", MODE_PRIVATE);
        mPhone = sp.getString("name", "");
        mAudit = getIntent().getStringExtra("audit");
        initControl();//初始化各控件
        startAnim();//启用帧动画
        //判断审核状态
        initAudit();
        http_return_bank();// 得到银行卡信息
        chooseBank();//选择所属银行
        initClick();//各控件点击事件
    }
    private void initAudit() {
        mText_confirm.setText(mAudit);
        if (!mAudit.equals("已审核")) {
            mNext.setText("下一步");
        } else {
            mNext.setText("查看更多");
            mImage_confirm.setImageResource(R.mipmap.prompt_not_tg);
            mBank_number.setFocusable(false);
            mName.setFocusable(false);
            mSub_bank.setFocusable(false);
        }
    }
    /**
     * 各控件点击事件
     */
    private void initClick() {
        mProvince.setOnClickListener(this);//省份
        mCity.setOnClickListener(this);//市区
        findViewById(R.id.imageView_return).setOnClickListener(this);//返回
        findViewById(R.id.rel_next).setOnClickListener(this);
    }
    /**
     * 初始化各控件
     */
    private void initControl() {
        mLoading = (RelativeLayout) findViewById(R.id.rl_load);//帧动画
        mImage_load = (ImageView) findViewById(R.id.image_load);
        mName = (EditText) findViewById(R.id.editText_name1);//姓名
        mBank_number = (EditText) findViewById(R.id.editText_bank_number);//卡号
        mSpinner = (Spinner) findViewById(R.id.text_bank_name);//所属银行
        mSub_bank = (EditText) findViewById(R.id.editText_zhihang);//支行
        mProvince = (TextView) findViewById(R.id.text_sheng);//省份
        mCity = (TextView) findViewById(R.id.text_shi);//市
        mImage_confirm = (ImageView) findViewById(R.id.image_confirm);//认证与否图标
        mText_confirm = (TextView) findViewById(R.id.text_confirm);//认证与否文本
        mNext = (TextView) findViewById(R.id.tv_next);
    }
    /**
     * 返回银行卡信息
     */
    private void http_return_bank() {
        // TODO Auto-generated method stub
        mLoading.setVisibility(View.VISIBLE);
        String url=InterfaceUrl.url+"/app/Bank_card_return.aspx";
        hashMap_bank.clear();
        hashMap_bank.put("username", mPhone);
        DownHTTP.postVolley(url, hashMap_bank, new VolleyResultListener() {
            @Override
            public void onErrorResponse(VolleyError arg0) {
                // TODO Auto-generated method stub
                stopAnim();
            }
            @Override
            public void onResponse(String arg0) {
                // TODO Auto-generated method stub
                try {
                    JSONArray jsonArray = new JSONArray(arg0);
                    for(int i =0;i<jsonArray.length();i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        String name = object.getString("name");
                        String bank_name = object.getString("bank_card_name");
                        String province = object.getString("bank_card_province");
                        String city = object.getString("bank_card_city");
                        String subBank = object.getString("bank_card_branch");
                        String bank_number = object.getString("Bank_card_number");
                        //根据传回的bank_name获得在数组对应的角标j,根据j设置spinner的当前item;
                        L:for(int j=0;j<mBanks.length;j++){
                            if(bank_name.equals(mBanks[j])){
                                mSpinner.setSelection(j, true);
                                break L;
                            }
                        }
                        mName.setText(name);
                        mProvince.setText(province);
                        mCity.setText(city);
                        mSub_bank.setText(subBank);
                        mBank_number.setText(bank_number);
                        stopAnim();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    stopAnim();
                }
            }
        });
    }
    /**
     * 各控件点击监听
     * @param v
     */
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.rel_next://下一步
                String str = mNext.getText().toString();
                if(str.equals("下一步")){
                    startAnim();
                    submitBank();
                }
                mIntent = new Intent(this,ID_PhotoActivity.class);
                mIntent.putExtra("audit", mAudit);
                startActivity(mIntent);
                finish();
                break;
            case R.id.text_sheng://选择省份
                chooseRegion();
                break;
            case R.id.text_shi:
                chooseRegion();//选择市区
                break;
            case R.id.imageView_return://返回
                mIntent = new Intent(this,PersonalDataActivity.class);
                startActivity(mIntent);
                finish();
                break;
            default:
                break;
        }
    }
    /**
     * 提交银行卡信息
     */
    private void submitBank() {
        String name = mName.getText().toString().trim();//姓名
        String bank_card = mBank_number.getText().toString().trim();//卡号
        String subBank = mSub_bank.getText().toString().trim();//支行
        String province = mProvince.getText().toString().trim();//省
        String city = mCity.getText().toString().trim();//市
        hashMap_bank.clear();
        hashMap_bank.put("username", mPhone);
        hashMap_bank.put("name", name);
        hashMap_bank.put("bank_card_name", bank_name);
        hashMap_bank.put("bank_card_province", province);
        hashMap_bank.put("bank_card_city", city);
        hashMap_bank.put("bank_card_branch", subBank);
        hashMap_bank.put("Bank_card_number", bank_card);
        String url=InterfaceUrl.url+"/app/Bank_card_set.aspx";
        DownHTTP.postVolley(url, hashMap_bank, new VolleyResultListener() {
            @Override
            public void onErrorResponse(VolleyError arg0) {
                // TODO Auto-generated method stub
                stopAnim();
            }
            @Override
            public void onResponse(String arg0) {
                // TODO Auto-generated method stub
                if(arg0.equals("更新成功")){
                    Toast.makeText(BankconfirmActivity.this,"银行卡数据提交成功",Toast.LENGTH_LONG).show();
                }
                stopAnim();
            }
        });
    }
    /**
     * 跳至省市选择界面
     */
    private void chooseRegion() {
        // TODO Auto-generated method stub
        Intent intent = new Intent(this,CitySelect01Activity.class);
        intent.putExtra("city", city_name);
        startActivityForResult(intent, 1);
    }
    /**
     * 选择所属银行
     */
    private void chooseBank() {
        mBanks = getResources().getStringArray(R.array.planets);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,mBanks);
        adapter.setDropDownViewResource(R.layout.drop_down_item);//自定义的样式和适配器连接起来
        mSpinner.setAdapter(adapter);//适应适配器
        /**
         * spinner下列框选中监听
         */
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                bank_name=mBanks[arg2];//获取当前选中的银行，初始值为mBanks[0];
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
    }
    /**
     * 等待选择省、市回传的值
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            if(resultCode==2){
                City city = data.getParcelableExtra("city");
                province_name = city.getProvince();
                city_name = city.getCity();
                mProvince.setText(province_name);
                mCity.setText(city_name);
            }
        }
    }
    private void startAnim() {
        mDrawable = (AnimationDrawable) mImage_load.getDrawable();
        mDrawable.start();
    }
    private void stopAnim() {
        mDrawable.stop();
        mLoading.setVisibility(View.INVISIBLE);
    }
}
