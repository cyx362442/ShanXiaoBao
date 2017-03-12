package com.zhongbang.sxb;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.zhongbang.sxb.bean.VersionContents;
import com.zhongbang.sxb.fragment.CenterFragment;
import com.zhongbang.sxb.fragment.SlidingMain;
import com.zhongbang.sxb.httputils.DownHTTP;
import com.zhongbang.sxb.httputils.VolleyResultListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private final String updateUrl="http://chinazbhf.com/app/return_Homepage.aspx";
    private final String currentVersion="1.1";
    private HashMap<String, String> mPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        centerFragment();
        sliding();//滑动条自动滑动

    }

    @Override
    protected void onStart() {
        super.onStart();
        mPost = new HashMap<>();
        DownHTTP.postVolley(updateUrl, mPost, new VolleyResultListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                VersionContents[] contentses = gson.fromJson(response, VersionContents[].class);
                if(!contentses[0].system_version.equals(currentVersion)){
                    showDialog_version(contentses[0].author_contact);
                }else{
                    
                }
            }
        });
    }

    private void showDialog_version(final String url) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("版本更新")
                .setIcon(R.mipmap.ic_launcher)
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

    private void centerFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction bt = fm.beginTransaction();
        bt.replace(R.id.layout_fragment2, new CenterFragment());//替换布局为 片段控件功能
        bt.commit();
    }

    /**滑动条功能功能*/
    public void sliding(){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction bt = fm.beginTransaction();
        bt.replace(R.id.layout_fragment, new SlidingMain());//替换布局为 片段控件功能
        bt.commit();
    }
}
