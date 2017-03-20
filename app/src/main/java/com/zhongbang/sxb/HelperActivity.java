package com.zhongbang.sxb;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.zhongbang.sxb.bean.VersionContents;
import com.zhongbang.sxb.colleciton.WebView_PayActivity;
import com.zhongbang.sxb.httputils.DownHTTP;
import com.zhongbang.sxb.httputils.VolleyResultListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HelperActivity extends AppCompatActivity {
    private String url;
    private String title;
    private final String currentVersion="1.00";

    @Bind(R.id.img_head)
    ImageView mImgHead;
    @Bind(R.id.textView_title)
    TextView mTextViewTitle;
    @Bind(R.id.relativeLayout1)
    RelativeLayout mRelativeLayout1;
    @Bind(R.id.ll_help01)
    LinearLayout mLlHelp01;
    @Bind(R.id.ll_help02)
    LinearLayout mLlHelp02;
    @Bind(R.id.ll_help03)
    LinearLayout mLlHelp03;
    @Bind(R.id.ll_help04)
    LinearLayout mLlHelp04;
    @Bind(R.id.ll_help05)
    LinearLayout mLlHelp05;
    @Bind(R.id.ll_help06)
    LinearLayout mLlHelp06;
    @Bind(R.id.ll_help07)
    LinearLayout mLlHelp07;
    @Bind(R.id.tv_exit)
    TextView mTvExit;
    @Bind(R.id.linearLayout1)
    LinearLayout mLinearLayout1;
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.ll_help01, R.id.ll_help02, R.id.ll_help03, R.id.ll_help04, R.id.ll_help05, R.id.ll_help06, R.id.ll_help07, R.id.tv_exit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_help01:
                url="http://chinazbhf.com:8081/sxb/attached/about-us.html";
                title="关于我们";
                toWebView();
                break;
            case R.id.ll_help02:
                url="http://chinazbhf.com:8081/sxb/attached/about-zz.html";
                title="关于资质";
                toWebView();
                break;
            case R.id.ll_help03:
                url="http://chinazbhf.com:8081/sxb/attached/manual.html";
                title="操作手册";
                toWebView();
                break;
            case R.id.ll_help04:
                url="http://chinazbhf.com:8081/sxb/attached/questions.html";
                title="常见问题";
                toWebView();
                break;
            case R.id.ll_help05:
                url="http://chinazbhf.com:8081/sxb/attached/services.html";
                title="客户服务";
                toWebView();
                break;
            case R.id.ll_help06:
                mIntent=new Intent(this,SuggestionActivity.class);
                startActivity(mIntent);
                break;
            case R.id.ll_help07:
                url="http://chinazbhf.com/app/return_Homepage.aspx";
                HashMap<String, String> map = new HashMap<>();
                DownHTTP.postVolley(url, map, new VolleyResultListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        VersionContents[] contentses = gson.fromJson(response, VersionContents[].class);
                        Log.e("=====",contentses[0].system_version);
                        if(!contentses[0].system_version.equals(currentVersion)){
                            showDialog_version(contentses[0].author_contact);
                        }
                    }
                });
                break;
            case R.id.tv_exit:
                SharedPreferences sp = getSharedPreferences("users", MODE_PRIVATE);
                SharedPreferences.Editor edit = sp.edit();
                edit.clear();
                edit.commit();
                finish();
                break;
        }
    }

    private void toWebView() {
        mIntent = new Intent(this, WebView_PayActivity.class);
        mIntent.putExtra("title",title);
        mIntent.putExtra("url",url);
        startActivity(mIntent);
    }
    private void showDialog_version(final String url) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("版本更新")
                .setIcon(R.mipmap.logo)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消",null)
                .setMessage("检测到新版本，请及时更新")
                .create();
        dialog.show();
    }
}
