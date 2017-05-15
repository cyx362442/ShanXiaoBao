package com.zhongbang.sxb.managercenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.zhongbang.sxb.R;
import com.zhongbang.sxb.application.ExitAppliation;
import com.zhongbang.sxb.bean.Earning01;
import com.zhongbang.sxb.bean.Earning02;
import com.zhongbang.sxb.bean.Earning03;
import com.zhongbang.sxb.colleciton.Users;
import com.zhongbang.sxb.httputils.DownHTTP;
import com.zhongbang.sxb.httputils.VolleyResultListener;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyEarnings extends AppCompatActivity {
    private final String urlType1 = "http://chinazbhf.com:8081/sxb/app/shop/userIncoming.app?username=";
    private final String urlType2 = "http://chinazbhf.com:8081/sxb/app/shop/generalAgentIncoming.app?username=";
    private final String urlType3 = "http://chinazbhf.com:8081/sxb/app/shop/areaAgentIncoming.app?username=";
    @Bind(R.id.image_return)
    ImageView mImageReturn;
    @Bind(R.id.text_zhangdan)
    TextView mTextZhangdan;
    @Bind(R.id.relativeLayout1)
    RelativeLayout mRelativeLayout1;
    @Bind(R.id.tv_level)
    TextView mTvLevel;
    @Bind(R.id.relativeLayout2)
    RelativeLayout mRelativeLayout2;
    @Bind(R.id.image_load)
    ImageView mImageLoad;
    @Bind(R.id.textView1)
    TextView mTextView1;
    @Bind(R.id.rl_load)
    RelativeLayout mRlLoad;
    AnimationDrawable mDrawable;
    @Bind(R.id.totalInComimg)
    TextView mTotalInComimg;
    @Bind(R.id.lastIncoming)
    TextView mLastIncoming;
    @Bind(R.id.lastTotalPay1)
    TextView mLastTotalPay1;
    @Bind(R.id.lastInComing1)
    TextView mLastInComing1;
    @Bind(R.id.lastTotalPay2)
    TextView mLastTotalPay2;
    @Bind(R.id.lastInComing2)
    TextView mLastInComing2;
    @Bind(R.id.lastTotalPay3)
    TextView mLastTotalPay3;
    @Bind(R.id.lastInComing3)
    TextView mLastInComing3;
    @Bind(R.id.tv1)
    TextView mTv1;
    @Bind(R.id.tv2)
    TextView mTv2;
    @Bind(R.id.tv3)
    TextView mTv3;
    @Bind(R.id.tv4)
    TextView mTv4;
    @Bind(R.id.tv5)
    TextView mTv5;
    @Bind(R.id.tv6)
    TextView mTv6;
    @Bind(R.id.linearLayout3)
    LinearLayout mLinearLayout3;
    private HashMap<String, String> map = new HashMap<>();

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_earnings);
        ExitAppliation.getInstance().addActivity(this);
        ButterKnife.bind(this);
        SharedPreferences sp = getSharedPreferences("users", Context.MODE_PRIVATE);
        username = sp.getString("name", "");
    }

    @Override
    protected void onStart() {
        super.onStart();
        mTvLevel.setText(Users.userType);
        startAnim();
        if (Users.userType.equals("注册用户")) {
            mTv1.setText("昨日商城消费额");
            mTv2.setText("昨日商城消费收益");
            mTv3.setText("昨日机器人系统消费额");
            mTv4.setText("昨日机器人系统消费收益");
            mLinearLayout3.setVisibility(View.INVISIBLE);
            DownHTTP.postVolley(urlType1+username, map, new VolleyResultListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    stopAmin();
                    Toast.makeText(MyEarnings.this, "网络异常", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onResponse(String response) {
                    Gson gson = new Gson();
                    Earning01 earning01 = gson.fromJson(response, Earning01.class);
                    mTotalInComimg.setText(earning01.getMonthIncoming());
                    mLastIncoming.setText(earning01.getLastIncoming());
                    mLastInComing1.setText(earning01.getLastMallIncoming());
                    mLastInComing2.setText(earning01.getLastRebotIncoming());
                    mLastTotalPay1.setText(earning01.getLastMallPay());
                    mLastTotalPay2.setText(earning01.getLastRebotPay());
                    stopAmin();
                }
            });
        }
        else if(Users.userType.equals("普通经销商")){
            mLinearLayout3.setVisibility(View.VISIBLE);
            mTv1.setText("昨日商城总消费额");
            mTv2.setText("昨日商城消费收益");
            mTv3.setText("昨日机器人系统总消费额");
            mTv4.setText("昨日机器人系统消费收益");
            mTv5.setText("昨日总收款金额");
            mTv6.setText("昨日收款收益");
            DownHTTP.postVolley(urlType2+username, map, new VolleyResultListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    stopAmin();
                    Toast.makeText(MyEarnings.this, "网络异常", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onResponse(String response) {
                    Gson gson = new Gson();
                    Earning02 earning02 = gson.fromJson(response, Earning02.class);
                    mTotalInComimg.setText(earning02.getMonthIncoming());
                    mLastIncoming.setText(earning02.getLastIncoming());
                    mLastInComing1.setText(earning02.getLastMallIncoming());
                    mLastInComing2.setText(earning02.getLastRebotIncoming());
                    mLastTotalPay1.setText(earning02.getLastMallPay());
                    mLastTotalPay2.setText(earning02.getLastRebotPay());
                    mLastTotalPay3.setText(earning02.getLastReceipt());
                    mLastInComing3.setText(earning02.getLastReceiptIncoming());
                    stopAmin();
                }
            });
        }else if(Users.userType.equals("区域经销商")){
            mTv1.setText("昨日区域商城总消费额");
            mTv2.setText("昨日区域商城消费收益");
            mTv3.setText("昨日区域机器人系统总消费额");
            mTv4.setText("昨日区域机器人系统消费收益");
            mTv5.setText("昨日区域总收款金额");
            mTv6.setText("昨日区域收款收益");
            mLinearLayout3.setVisibility(View.VISIBLE);
            DownHTTP.postVolley(urlType3+username, map, new VolleyResultListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    stopAmin();
                    Toast.makeText(MyEarnings.this, "网络异常", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onResponse(String response) {
                    Gson gson = new Gson();
                    Earning03 earning03 = gson.fromJson(response, Earning03.class);
                    mTotalInComimg.setText(earning03.getMonthIncoming());
                    mLastIncoming.setText(earning03.getLastIncoming());
                    mLastInComing1.setText(earning03.getLastMallIncoming());
                    mLastInComing2.setText(earning03.getLastRebotIncoming());
                    mLastTotalPay1.setText(earning03.getLastMallPay());
                    mLastTotalPay2.setText(earning03.getLastRebotPay());
                    mLastTotalPay3.setText(earning03.getLastReceipt());
                    mLastInComing3.setText(earning03.getLastReceiptIncoming());
                    stopAmin();
                }
            });
        }
    }

    private void startAnim() {
        mDrawable = (AnimationDrawable) mImageLoad.getDrawable();
        mDrawable.start();
    }

    private void stopAmin() {
        mDrawable.stop();
        mRlLoad.setVisibility(View.GONE);
    }

    @OnClick(R.id.image_return)
    public void onClick() {
        finish();
    }
}
