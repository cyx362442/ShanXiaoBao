package com.zhongbang.sxb;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.zhongbang.sxb.application.ExitAppliation;
import com.zhongbang.sxb.httputils.DownHTTP;
import com.zhongbang.sxb.httputils.VolleyResultListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;

public class SuggestionActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edittext;
    private String mPhone;
    String urlpost = " http://chinazbhf.com/app/Operation.ashx";
    HashMap<String, String> map = new HashMap<String, String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);
        ExitAppliation.getInstance().addActivity(this);
        SharedPreferences sp = getSharedPreferences("users", Context.MODE_PRIVATE);
        mPhone = sp.getString("name", "");
        edittext = (EditText) findViewById(R.id.editText1);
        findViewById(R.id.btn_tijiao).setOnClickListener(this);
        findViewById(R.id.imageView_return).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_tijiao:
                String text = edittext.getText().toString().trim();
                http();
                map.put("type", "so");
                map.put("username", mPhone);
                map.put("txt", text);
                break;
            case R.id.imageView_return:
                finish();
                break;
        }
    }
    private void http() {
        // TODO Auto-generated method stub
        DownHTTP.postVolley(urlpost, map, new VolleyResultListener() {
            @Override
            public void onErrorResponse(VolleyError arg0) {
                // TODO Auto-generated method stub
                Toast.makeText(SuggestionActivity.this,"提交失败",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onResponse(String arg0) {
                // TODO Auto-generated method stub
                try {

                    String st = new JSONArray(arg0).getJSONObject(0).getString(
                            "parmone");
                    Toast.makeText(SuggestionActivity.this,st,Toast.LENGTH_SHORT).show();
                    finish();

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        });
    }
}
