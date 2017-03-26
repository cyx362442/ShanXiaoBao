package com.zhongbang.sxb;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.zhongbang.sxb.application.ExitAppliation;
import com.zhongbang.sxb.httputils.DownHTTP;
import com.zhongbang.sxb.httputils.VolleyResultListener;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgetPasswordActivity extends AppCompatActivity {
    private String urlChange="http://chinazbhf.com/app/Operation.ashx";

    @Bind(R.id.image_return)
    ImageView mImageReturn;
    @Bind(R.id.et_phone)
    EditText mEtPhone;
    @Bind(R.id.et_Id)
    EditText mEtId;
    @Bind(R.id.et_password)
    EditText mEtPassword;
    @Bind(R.id.tv_change)
    TextView mTvChange;
    private String phone;
    private HashMap<String,String>map=new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ExitAppliation.getInstance().addActivity(this);
        ButterKnife.bind(this);
        SharedPreferences sp = getSharedPreferences("users", Context.MODE_PRIVATE);
        phone = sp.getString("name", "");
    }

    @Override
    protected void onStart() {
        super.onStart();
        mEtPhone.setText(phone);
    }

    @OnClick({R.id.image_return, R.id.tv_change})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_return:
                finish();
                break;
            case R.id.tv_change:
                String id = mEtId.getText().toString().trim();
                String password = mEtPassword.getText().toString().trim();
                map.clear();
                map.put("username",phone);
                map.put("card",id);
                map.put("pwd",password);
                map.put("type","zhmm");
                DownHTTP.postVolley(urlChange, map, new VolleyResultListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                    @Override
                    public void onResponse(String response) {

                    }
                });
                break;
        }
    }
}
