package com.zhongbang.sxb;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.zhongbang.sxb.httputils.DownHTTP;
import com.zhongbang.sxb.httputils.VolleyResultListener;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    private final String urlDuanxin="http://chinazbhf.com:8081/SHXBZC/ZhuCe";
    private final String urlZheCe="http://chinazbhf.com:8081/SHXBZC/ZhuCe";

    private int mTimes=60;

    @Bind(R.id.img_back)
    ImageView mImgBack;
    @Bind(R.id.et_phone)
    EditText mEtPhone;
    @Bind(R.id.et_code)
    EditText mEtCode;
    @Bind(R.id.btn_getcode)
    Button mBtnGetcode;
    @Bind(R.id.et_password)
    EditText mEtPassword;
    @Bind(R.id.et_password_again)
    EditText mEtPasswordAgain;
    @Bind(R.id.et_tuijiantren)
    EditText mEtTuijiantren;
    @Bind(R.id.et_IdCard)
    EditText mEtIdCard;
    @Bind(R.id.btn_login)
    Button mBtnLogin;
    private HashMap<String, String> mMap;
    private Handler mHandler;
    private Runnable mRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.img_back, R.id.btn_getcode, R.id.btn_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_getcode:
                String trim = mEtPhone.getText().toString().trim();
                if(trim.length()!=11){
                    Toast.makeText(this,"请输入正确的手机号码",Toast.LENGTH_SHORT).show();
                }else{
                    mBtnGetcode.setEnabled(false);
                    startTime();
                    Http_code(trim);
                }
                break;
            case R.id.btn_login:
                String phone = mEtPhone.getText().toString().trim();
                String password = mEtPassword.getText().toString().trim();
                String passwrod_again = mEtPasswordAgain.getText().toString().trim();
                String tuijianren = mEtTuijiantren.getText().toString().trim();
                String idCard = mEtIdCard.getText().toString().trim();
                String tjr= TextUtils.isEmpty(tuijianren)?"":tuijianren;
                String id=TextUtils.isEmpty(idCard)?"":idCard;
                if(TextUtils.isEmpty(phone)||TextUtils.isEmpty(password)||TextUtils.isEmpty(passwrod_again)){
                    Toast.makeText(this,"请填写完整信息",Toast.LENGTH_SHORT).show();
                }else if(!password.equals(passwrod_again)){
                    Toast.makeText(this,"两次密码不一致",Toast.LENGTH_SHORT).show();
                }else{
                    mMap=new HashMap<>();
                    mMap.put("type_all","zc");
                    mMap.put("phone",phone);
                    mMap.put("password",password);
                    mMap.put("yzm","");
                    mMap.put("tjr",tjr);
                    mMap.put("idCard",id);
                    DownHTTP.postVolley(urlZheCe, mMap, new VolleyResultListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                        @Override
                        public void onResponse(String response) {
                            if(response.contains("注册成功")){
                                finish();
                            }
                            Toast.makeText(LoginActivity.this,response,Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;
        }
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
                    mTimes=60;
                    mBtnGetcode.setText("获取验证码");
                    mBtnGetcode.setEnabled(true);
                    mHandler.removeCallbacks(mRunnable);
                }
            }
        },0);
    }

    private void Http_code(String phone) {
        mMap = new HashMap<>();
        mMap.put("type_all","duanxin");
        mMap.put("phone",phone);
        DownHTTP.postVolley(urlDuanxin, mMap, new VolleyResultListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
            @Override
            public void onResponse(String response) {
                Toast.makeText(LoginActivity.this,"验证码己发送",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mHandler!=null){
            mHandler.removeCallbacks(mRunnable);
        }
    }
}
