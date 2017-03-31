package com.zhongbang.sxb;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.zhongbang.sxb.account.PersonalDataActivity;
import com.zhongbang.sxb.application.ExitAppliation;
import com.zhongbang.sxb.bean.Audit;
import com.zhongbang.sxb.bean.VersionContents;
import com.zhongbang.sxb.colleciton.Users;
import com.zhongbang.sxb.colleciton.WebView_PayActivity;
import com.zhongbang.sxb.event.MessageEvent;
import com.zhongbang.sxb.fragment.CenterFragment;
import com.zhongbang.sxb.fragment.ServerCenterFragment;
import com.zhongbang.sxb.fragment.SlidingMain;
import com.zhongbang.sxb.fragment.TopFragment;
import com.zhongbang.sxb.fragment.ZoneSelectFragment;
import com.zhongbang.sxb.httputils.DownHTTP;
import com.zhongbang.sxb.httputils.VolleyResultListener;
import com.zhongbang.sxb.region.City;

import java.util.HashMap;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String updateUrl="http://chinazbhf.com/app/return_Homepage.aspx";
    private final String auitUrl="http://chinazbhf.com/app/user_data_return.aspx";
    private final String orderUrl="http://chinazbhf.com:8081/SHXBWD/wd.do";
    private final String orderUrl_search="http://chinazbhf.com:8081/SHXBWD/mjgl/sy.html?id=";

    private final String currentVersion="1.00";
    private HashMap<String, String> mPost;
    private boolean mIsLoad;
    private String mName;
    private SharedPreferences mSp;
    private String city="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ExitAppliation.getInstance().addActivity(this);

        toFragment("",R.id.frame_top,new TopFragment());
        toFragment("",R.id.layout_fragment2,new CenterFragment());
        toFragment("",R.id.layout_fragment,new SlidingMain());//滑动条自动滑动
        findViewById(R.id.layout_directions_use).setOnClickListener(this);
        findViewById(R.id.layout_user_center).setOnClickListener(this);
        findViewById(R.id.imageView_middle).setOnClickListener(this);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void getEventBusMsg(MessageEvent event){
        city=event.msg;
        if(event.from.equals("back")){
            toFragment(city,R.id.layout_fragment2,new ZoneSelectFragment());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mSp = getSharedPreferences("users", Context.MODE_PRIVATE);
        mIsLoad = mSp.getBoolean("isLoad", false);
        mName = mSp.getString("name", "");

        mPost = new HashMap<>();
        mPost.clear();
        DownHTTP.postVolley(updateUrl, mPost, new VolleyResultListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"网络连接失败",Toast.LENGTH_LONG).show();
            }
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                VersionContents[] contentses = gson.fromJson(response, VersionContents[].class);
                if(!contentses[0].system_version.equals(currentVersion)){
                    showDialog_version(contentses[0].author_contact);
                }else{
                    if(mIsLoad==true){
                        Http_auit();
                    }
                }
            }
        });
    }

    private void Http_auit() {
        mPost=new HashMap<String, String>();
        mPost.clear();
        mPost.put("username",mName);
        DownHTTP.postVolley(auitUrl, mPost, new VolleyResultListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
            @Override
            public void onResponse(String response) {
//                if(TextUtils.isEmpty(response)){
//                    showDialog_Audit("用户未实名认证，前去认证");
//                    return;
//                }
                Gson gson = new Gson();
                Audit[] audits = gson.fromJson(response, Audit[].class);
                String audit = audits[0].audit;
                Users.userType=audits[0].user_types;
                if(!audit.equals("已审核")&&!audit.equals("上传认证")){
                    String msg= audit.equals("未审核")?"用户未实名认证，前去认证":"审核未通过，请填写真实资料";
                    showDialog_Audit(msg);
                }else if(audit.equals("已审核")||audit.equals("上传认证")){
                    Http_order();
                }
            }
        });
    }

    private void Http_order() {
        mPost=new HashMap<String, String>();
        mPost.clear();
        mPost.put("type_all","wd_jygl_state_y_w");
        mPost.put("username_seller",mName);
        DownHTTP.postVolley(orderUrl, mPost, new VolleyResultListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
            @Override
            public void onResponse(String response) {
                if(response.contains("y")){
                    showDialog_order();
                }
            }
        });
    }

    private void showDialog_order() {
        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("提示")
                .setIcon(R.mipmap.logo)
                .setMessage("有用户购买了你的商品，请发货")
                .setPositiveButton("前往", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this, WebView_PayActivity.class);
                        intent.putExtra("title","订单查询");
                        intent.putExtra("url",orderUrl_search+mName);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("取消",null)
                .create();
        dialog.show();
    }

    private void showDialog_Audit(String msg) {
        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("提示")
                .setMessage(msg)
                .setIcon(R.mipmap.logo)
                .setPositiveButton("前往", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this, PersonalDataActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("取消",null)
                .create();
        dialog.show();
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
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(mIsLoad==true){
                            Http_auit();
                        }
                    }
                })
                .setMessage("检测到新版本，请及时更新")
                .create();
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_directions_use:
                if(mIsLoad==false){
                    Intent intent = new Intent(this, LandActivity.class);
                    startActivity(intent);
                    return;
                }
                toFragment(city,R.id.layout_fragment2,new ZoneSelectFragment());
                break;
            case R.id.layout_user_center:
                if(mIsLoad==false){
                    Intent intent = new Intent(this, LandActivity.class);
                    startActivity(intent);
                    return;
                }
                toFragment("",R.id.layout_fragment2,new ServerCenterFragment());
                break;
            case R.id.imageView_middle:
                toFragment("",R.id.layout_fragment2,new CenterFragment());
                break;
        }
    }

    private void toFragment(String msg,int id,Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("city", msg);
        fragment.setArguments(bundle);

        transaction.replace(id,fragment);
        transaction.commit();
    }
}
