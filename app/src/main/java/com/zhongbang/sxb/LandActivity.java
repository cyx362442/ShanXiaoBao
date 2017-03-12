package com.zhongbang.sxb;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.zhongbang.sxb.bean.Users;
import com.zhongbang.sxb.httputils.DownHTTP;
import com.zhongbang.sxb.httputils.VolleyResultListener;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LandActivity extends AppCompatActivity {
    public final String urlLand="http://chinazbhf.com/app/landing.aspx";
    public final String urlCode1="http://chinazbhf.com/app/user_data_return.aspx";
    public final String urlCode2="http://chinazbhf.com:8081/SHXBZC/ZhuCe";

    @Bind(R.id.imageView_return)
    ImageView mImageViewReturn;
    @Bind(R.id.textView_landing2)
    TextView mTextViewLanding2;
    @Bind(R.id.tv_login)
    TextView mTvLogin;
    @Bind(R.id.relativeLayout_landing)
    RelativeLayout mRelativeLayoutLanding;
    @Bind(R.id.tv_normal)
    TextView mTvNormal;
    @Bind(R.id.tv_quick)
    TextView mTvQuick;
    @Bind(R.id.linearLayout1)
    LinearLayout mLinearLayout1;
    @Bind(R.id.editText_user_name)
    EditText mEditTextUserName;
    @Bind(R.id.editText_password)
    EditText mEditTextPassword;
    @Bind(R.id.rlLand_normal)
    RelativeLayout mRlLandNormal;
    @Bind(R.id.editText_phone)
    EditText mEditTextPhone;
    @Bind(R.id.editText_code)
    EditText mEditTextCode;
    @Bind(R.id.btn_getcode)
    Button mBtnGetcode;
    @Bind(R.id.rlLand_quick)
    RelativeLayout mRlLandQuick;
    @Bind(R.id.btn_land)
    Button mBtnLand;
    @Bind(R.id.tv_forget)
    TextView mTvForget;

    private boolean isNormal=true;
    private Intent mIntent;
    private HashMap<String, String> mMap;
    private Handler mHandler;
    private Runnable mRunnable;
    private int mTimes=30;
    private boolean enLand=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_land);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.imageView_return, R.id.tv_login, R.id.tv_normal, R.id.tv_quick, R.id.editText_code, R.id.btn_getcode, R.id.btn_land, R.id.tv_forget})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView_return:
                finish();
                break;
            case R.id.tv_login:
                mIntent = new Intent(this, LoginActivity.class);
                startActivity(mIntent);
                break;
            case R.id.tv_normal:
                if(isNormal==false){
                    mTvNormal.setTextColor(getResources().getColor(R.color.yellowLand));
                    mTvNormal.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    mTvQuick.setTextColor(Color.parseColor("#494949"));
                    mTvQuick.setBackgroundColor(Color.parseColor("#EBEBEB"));
                    mRlLandNormal.setVisibility(View.VISIBLE);
                    mRlLandQuick.setVisibility(View.INVISIBLE);
                    mTvForget.setVisibility(View.VISIBLE);
                    isNormal=!isNormal;
                }
                break;
            case R.id.tv_quick:
                if(isNormal==true){
                    mTvQuick.setTextColor(getResources().getColor(R.color.yellowLand));
                    mTvQuick.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    mTvNormal.setTextColor(Color.parseColor("#494949"));
                    mTvNormal.setBackgroundColor(Color.parseColor("#EBEBEB"));
                    mRlLandQuick.setVisibility(View.VISIBLE);
                    mRlLandNormal.setVisibility(View.INVISIBLE);
                    mTvForget.setVisibility(View.INVISIBLE);
                    isNormal=!isNormal;
                }
                break;
            case R.id.editText_code:
                break;
            case R.id.btn_getcode:
                String trim = mEditTextPhone.getText().toString().trim();
                mBtnGetcode.setEnabled(false);
                startTime();
                Http_code(trim);
                break;
            case R.id.btn_land:
                if(isNormal==true){
                    normalLand();
                }else if(isNormal==false&&enLand==true){
                    String phone = mEditTextPhone.getText().toString().trim();
                    String code = mEditTextCode.getText().toString().trim();
                    if(TextUtils.isEmpty(phone)||TextUtils.isEmpty(code)){
                        Toast.makeText(this,"请输入电话号及验证码",Toast.LENGTH_SHORT).show();
                    }else{
                        DownHTTP.getVolley("http://chinazbhf.com:8081/sxb/app/user/quickLogin.app?username=" + phone + "&code=" + code, new VolleyResultListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                            @Override
                            public void onResponse(String response) {
                                if(response.contains("已审核")||response.contains("未审核")||
                                        response.contains("审核未通过")||response.contains("黑名单")){
                                    Toast.makeText(LandActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                                    Users.load=true;
                                    finish();
                                }
                            }
                        });
                    }
                }
                break;
            case R.id.tv_forget:
                break;
        }
    }

    private void normalLand() {
        String user = mEditTextUserName.getText().toString().trim();
        String password = mEditTextPassword.getText().toString().trim();
        mMap = new HashMap<>();
        mMap.put("username",user);
        mMap.put("password",password);
        DownHTTP.postVolley(urlLand, mMap, new VolleyResultListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
            @Override
            public void onResponse(String response) {
                Toast.makeText(LandActivity.this,"登录成功("+response+")",Toast.LENGTH_SHORT).show();
                if(!response.contains("登陆失败")){
                    Users.load=true;
                    finish();
                }
            }
        });
    }

    private void Http_code(final String phone) {
        mMap = new HashMap<>();
        mMap.put("phone",phone);
        DownHTTP.postVolley(urlCode1, mMap, new VolleyResultListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
            @Override
            public void onResponse(String response) {
               if(response.contains("信息有误")){
                   Toast.makeText(LandActivity.this,response,Toast.LENGTH_SHORT).show();
               }else{
                   enLand=true;
                   mMap.put("type_all","duanxin");
                   mMap.put("phone",phone);
                   DownHTTP.postVolley(urlCode2, mMap, new VolleyResultListener() {
                       @Override
                       public void onErrorResponse(VolleyError error) {
                       }
                       @Override
                       public void onResponse(String response) {
                            Toast.makeText(LandActivity.this,"验证码己发送",Toast.LENGTH_SHORT).show();
                       }
                   });
               }
            }
        });
    }
    private void startTime() {
        mHandler = new Handler();
        mHandler.postDelayed(mRunnable = new Runnable() {
            @Override
            public void run() {
                mHandler.postDelayed(mRunnable,1000);
                if(mTimes>0){
                    mTimes--;
                    mBtnGetcode.setText(mTimes+"秒后重发");
                }else{
                    mTimes=30;
                    mBtnGetcode.setText("获取验证码");
                    mBtnGetcode.setEnabled(true);
                    mHandler.removeCallbacks(mRunnable);
                }
            }
        },0);
    }
}
