package com.zhongbang.sxb.account;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

import java.util.HashMap;

public class PersonalDataActivity extends AppCompatActivity implements View.OnClickListener {
    String st = "";
    /** 所在省份-province */
    private String province = "";
    /** 所在城市-city */
    private String city = "";
    private City city2;
    /** 要传的值的容器 */
    HashMap<String, String> HashMap_post = new HashMap<String, String>();
    /** 注册 */
    HashMap<String, String> HashMap_post1 = new HashMap<String, String>();

    private String phone;
    private EditText mEditText_QQ;//QQ号
    private TextView mText_confirm;//未认证！(完善个人资料)
    private EditText mEditText_card;//身份证号码
    private TextView mTextView_city2;//省-市
    private EditText mEditText_name;//姓名
    private EditText mEditText_phone;//账号
    private EditText mEditText_merchants_name;//商户名称
    private RelativeLayout mRelativeLayout_region;//省-市
    private ImageView mImage_confirm;//认证与否图标
    private TextView mNext;
    private RelativeLayout mLoading;
    private ImageView mImage_load;
    private AnimationDrawable mDrawable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);
        SharedPreferences sp = getSharedPreferences("users", Context.MODE_PRIVATE);
        phone = sp.getString("name", "");
        initWidget();
        startAnim();//开启动画
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        city2 = new City();
        chushihua();
    }
    // 等待传回数值ֵ
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode==1) {
                if (resultCode==2) {// 等待传回城市的值ֵ
                    city2 = data.getParcelableExtra("city");
                    province=city2.getProvince();
                    city=city2.getCity();
                    mTextView_city2.setText(province +"-"+ city);
                    String district = city2.getDistrict();
                    mEditText_merchants_name.setText(district);
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    /**
     * 初始化各控件
     */
    private void initWidget() {
        mLoading = (RelativeLayout) findViewById(R.id.rl_load);
        mImage_load = (ImageView) findViewById(R.id.image_load);
        mEditText_QQ = (EditText) findViewById(R.id.editText_QQ);
        mText_confirm = (TextView) findViewById(R.id.text_confirm);
        mEditText_card = (EditText) findViewById(R.id.editText_card);
        mTextView_city2 = (TextView) findViewById(R.id.textView_city2);
        mEditText_name = (EditText) findViewById(R.id.editText_name);
        mEditText_phone = (EditText) findViewById(R.id.editText_phone);
        mEditText_phone.setText(phone);
        mEditText_merchants_name = (EditText) findViewById(R.id.editText_merchants_name);
        mRelativeLayout_region = (RelativeLayout) findViewById(R.id.relativeLayout_region);
        mImage_confirm = (ImageView) findViewById(R.id.image_confirm);
        mNext = (TextView) findViewById(R.id.tv_next);
        mRelativeLayout_region.setOnClickListener(this);//跳至省市选择
        findViewById(R.id.imageView_return).setOnClickListener(this);//返回
        findViewById(R.id.rel_next).setOnClickListener(this);//下一步
    }
    private void chushihua() {
        HashMap_post1.clear();
        HashMap_post1.put("username", phone);
        http1();
    }
    /**
     *发送post请求，获取个人提交至服务的资料信息
     */
    private void http1() {
        DownHTTP.postVolley(InterfaceUrl.url+"/app/user_data_return.aspx", HashMap_post1,
                new VolleyResultListener() {
                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        stopAnim();
                    }
                    @Override
                    public void onResponse(String arg0) {
                        try {
                            JSONArray array = new JSONArray(arg0);
                            String QQ = array.getJSONObject(0).getString("QQ");
                            String idcard = array.getJSONObject(0).getString(
                                    "Id_number");
                            String shanghu = array.getJSONObject(0).getString(
                                    "merchants_name");
                            province = array.getJSONObject(0).getString(
                                    "province");
                            city = array.getJSONObject(0).getString("city");
                            String name = array.getJSONObject(0).getString(
                                    "name");
                            String phone = array.getJSONObject(0).getString(
                                    "username");
                            mEditText_phone.setText(phone);
                            mTextView_city2.setText(province + "-" + city);
                            mEditText_QQ.setText(QQ);
                            mEditText_merchants_name.setText(shanghu);
                            mEditText_name.setText(name);
                            mEditText_card.setText(idcard);
                            stopAnim();//关闭动画
                            st = array.getJSONObject(0).getString("audit");
                            mText_confirm.setText(st);
                            if (!"已审核".equals(st)) {
                                mNext.setText("下一步");
                            } else {
                                mNext.setText("查看更多");
                                mImage_confirm.setImageResource(R.mipmap.prompt_not_tg);
                                mEditText_card.setFocusable(false);
                                mEditText_phone.setFocusable(false);
                                mEditText_QQ.setFocusable(false);
                                mEditText_merchants_name.setFocusable(false);
                                mEditText_name.setFocusable(false);
                                mRelativeLayout_region.setClickable(false);
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            stopAnim();//关闭动画
                        }
                    }
                });
        }
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.imageView_return:
                finish();
                break;
            case R.id.relativeLayout_region://跳至选择省市地区
                region();
                break;
            case R.id.rel_next://下一步
                String str = mNext.getText().toString();
                if(str.equals("下一步")){
                    next();
                }
                Intent intent = new Intent(PersonalDataActivity.this,BankconfirmActivity.class);
                intent.putExtra("audit", st);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }
    /**选择地区*/
    private void region() {
        Intent in = new Intent(this, CitySelect01Activity.class);
        in.putExtra("city", city);
        startActivityForResult(in, 1);
    }
    /**下一步*/
    private void next() {
        String name = mEditText_name.getText().toString().trim();// 问题答案-answer

        if (name.equals("")) {
            Toast.makeText(this, "请输入[真实姓名]", Toast.LENGTH_SHORT).show();
            return;
        }
        String Id_card = mEditText_card.getText().toString().trim();// 身份证号码-Id_card

        if (Id_card.equals("")) {
            Toast.makeText(this, "请输入您的身份证号码", Toast.LENGTH_SHORT).show();
            return;
        }
        String QQ = mEditText_QQ.getText().toString().trim();// QQ-QQ

        if (QQ.equals("")) {
            Toast.makeText(this, "请输入QQ号码", Toast.LENGTH_SHORT).show();
            return;
        }
        String merchants_name = mEditText_merchants_name.getText().toString()
                .trim();// 商户名称[虚拟网店中文名称]-merchants_name

        if (merchants_name.equals("")) {
            Toast.makeText(this, "请设点击[所在的区、县]进行设置", Toast.LENGTH_SHORT).show();
            return;
        }
        if (province.equals("") || city.equals("")) {
            Toast.makeText(this, "请设点击[所在的省份和城市]进行设置", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        HashMap_post.clear();// 清空
        HashMap_post.put("type", "ws");// 此时这里就是要传的网页上对应的参数String的值ֵ
        HashMap_post.put("username", phone);// 此时这里就是要传的网页上对应的参数String的值ֵ
        HashMap_post.put("QQ", QQ);// 此时这里就是要传的网页上对应的参数String的值ֵ
        HashMap_post.put("Id_card", Id_card);// 此时这里就是要传的网页上对应的参数String的值ֵ
        HashMap_post.put("name", name);// 此时这里就是要传的网页上对应的参数String的值ֵ
        HashMap_post.put("province", province);// 此时这里就是要传的网页上对应的参数String的值ֵ
        HashMap_post.put("city", city);// 此时这里就是要传的网页上对应的参数String的值ֵ
        HashMap_post.put("merchants_name", merchants_name);// 此时这里就是要传的网页上对应的参数String的值ֵ
        http();
    }
    /** HTTP协议POST传值返回数据 */
    private void http() {
		/* 使用POST请求 */
        DownHTTP.postVolley(InterfaceUrl.url+"/app/register.ashx", HashMap_post, new VolleyResultListener() {

            private String st;
            @Override
            public void onErrorResponse(VolleyError arg0) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onResponse(String arg0) {
                try {
                    JSONArray array = new JSONArray(arg0);
                    st = array.getJSONObject(0).getString("parmone");
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if ("执行成功".equals(st)) {
                    // chaoZuoSql.qingkong_user_data();// 清空数据
                    // chaoZuoSql.zeng_user_data(phone2, password2);// 增加数据
                    Toast.makeText(PersonalDataActivity.this, "个人资料上传成功", Toast.LENGTH_SHORT)
                            .show();
                    // intent_you(AuditActivityA.class);
                }
            }
        });
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
