package com.zhongbang.sxb.managercenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.zhongbang.sxb.R;
import com.zhongbang.sxb.application.ExitAppliation;
import com.zhongbang.sxb.colleciton.WebView_PayActivity;
import com.zhongbang.sxb.httputils.DownHTTP;
import com.zhongbang.sxb.httputils.VolleyResultListener;
import com.zhongbang.sxb.webview.WebViewActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MyComeinActivity extends AppCompatActivity implements View.OnClickListener {
    private final String urlIncome="http://chinazbhf.com:8081/SHXB/POST";

    private AnimationDrawable mDrawable;
    private RelativeLayout mRel_loading;
    private ImageView mImg_load;
    private String mPhone;
    private String ye;
    private String ljsr;
    private String wdljsr;
    private String yskljsr;
    private String frljsr;
    private String frljsrye;
    private String zgylt1;
    private String zgylt1ye;
    private String zfbt0;
    private String zfbt0ye;
    private String zgylt0;
    private String zgylt0ye;
    private TextView text_ye;
    private TextView text_ljsr;
    private TextView text_wdljsr;
    private TextView text_yskljsr;
    private TextView text_frljsr;
    private TextView text_frljsrye;
    private TextView text_zfbt0;
    private TextView text_zfbt0ye;
    private TextView text_zgylt0;
    private TextView text_zgylt0ye;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_comein);
        ExitAppliation.getInstance().addActivity(this);
        SharedPreferences sp = getSharedPreferences("users", Context.MODE_PRIVATE);
        mPhone = sp.getString("name", "");
        startAnim();
        initControl();
        http();
    }
    /**
     * 启动动画
     */
    private void startAnim() {
        mRel_loading = (RelativeLayout) findViewById(R.id.rl_load);
        mImg_load = (ImageView) findViewById(R.id.image_load);
        mDrawable = (AnimationDrawable) mImg_load.getDrawable();
        mDrawable.start();
    }
    private void initControl() {
        // TODO Auto-generated method stub
        text_ye = (TextView) findViewById(R.id.text_ye);
        text_ljsr = (TextView) findViewById(R.id.text_ljsr);
        text_wdljsr = (TextView) findViewById(R.id.text_wdljsr);
        text_yskljsr = (TextView) findViewById(R.id.text_yskljsr);
        text_frljsr = (TextView) findViewById(R.id.text1_fenrun);
        text_frljsrye = (TextView) findViewById(R.id.text1_kejiesuan);
        // text_zgylt1 = (TextView) findViewById(R.id.text2_fenrun);
        // text_zgylt1ye = (TextView) findViewById(R.id.text2_kejiesuan);
        text_zfbt0 = (TextView) findViewById(R.id.text4_fenrun);
        text_zfbt0ye = (TextView) findViewById(R.id.text4_kejiesuan);
        text_zgylt0 = (TextView) findViewById(R.id.text5_fenrun);
        text_zgylt0ye = (TextView) findViewById(R.id.text5_kejiesuan);
        findViewById(R.id.my_hongbao).setOnClickListener(this);
        findViewById(R.id.image_return).setOnClickListener(this);
        findViewById(R.id.text_zhangdan).setOnClickListener(this);
        findViewById(R.id.text1_xiangqing).setOnClickListener(this);
        findViewById(R.id.text2_xiangqing1).setOnClickListener(this);
        findViewById(R.id.text1_jiesuan).setOnClickListener(this);
        findViewById(R.id.text3_jiesuan).setOnClickListener(this);
        findViewById(R.id.text4_jiesuan).setOnClickListener(this);
        findViewById(R.id.text5_jiesuan).setOnClickListener(this);
    }
    private void http() {
        HashMap<String, String> hasmap = new HashMap<String, String>();
        hasmap.put("type_all", "channel");
        hasmap.put("username", mPhone);
        DownHTTP.postVolley(urlIncome, hasmap, new VolleyResultListener() {
            @Override
            public void onErrorResponse(VolleyError arg0) {
                // TODO Auto-generated method stub
                Toast.makeText(MyComeinActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                mDrawable.stop();
                mRel_loading.setVisibility(View.GONE);
            }
            @Override
            public void onResponse(String arg0) {
                // TODO Auto-generated method stub
                try {
                    JSONObject object = new JSONArray(arg0).getJSONObject(0);
                    ye = object.getString("ye");
                    ljsr = object.getString("ljsr");
                    wdljsr = object.getString("wdljsr");
                    yskljsr = object.getString("yskljsr");
                    frljsr = object.getString("frljsr");
                    frljsrye = object.getString("frljsrye");
                    zgylt1 = object.getString("zgylt1");
                    zgylt1ye = object.getString("zgylt1ye");
                    zfbt0 = object.getString("zfbt0");
                    zfbt0ye = object.getString("zfbt0ye");
                    zgylt0 = object.getString("zgylt0");
                    zgylt0ye = object.getString("zgylt0ye");
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    mDrawable.stop();
                    mRel_loading.setVisibility(View.GONE);
                }
                setTextView();
            }
        });
    }
    protected void setTextView() {
        // TODO Auto-generated method stub
        text_ye.setText(ye);
        text_ljsr.setText(ljsr);
        text_wdljsr.setText(wdljsr);
        text_yskljsr.setText(yskljsr);
        text_frljsr.setText(frljsr);
        text_frljsrye.setText(frljsrye);
        text_zfbt0.setText(zfbt0);
        text_zfbt0ye.setText(zfbt0ye);
        text_zgylt0.setText(zgylt0);
        text_zgylt0ye.setText(zgylt0ye);
        mDrawable.stop();
        mRel_loading.setVisibility(View.GONE);
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.image_return:
                finish();
                break;
            case R.id.text_zhangdan:
//                startActivity(new Intent(MyComeinActivity.this,MyBillActivity.class));
                break;
            case R.id.text1_xiangqing:
//                intent_url(InterfaceUrl.url+"/tg/customer_list.html?id="
//                                + mPhone,
//                        "分润累计收入");
                break;
            case R.id.text1_jiesuan:
//                intent_url(
//                        InterfaceUrl.url+"/tg/customer_list.html?id="+mPhone,
//                        "分润累计收入");
                break;
            case R.id.text3_jiesuan:
                intent_url(zgylt1, "O2O微信T+1");
                break;
            case R.id.text4_jiesuan:
                goto_jiesuan("支付宝");
                break;
            case R.id.text5_jiesuan:
                intent_url(zgylt1, "中国银联T+1");
                break;
            case R.id.my_hongbao:
//                Intent intent1 = new Intent(MyComeinActivity.this,
//                        Hongbao_Main.class);
//                startActivity(intent1);
                break;
        }
    }
    public void goto_jiesuan(String st) {
        Intent intent = new Intent(this, BalanceActivity.class);
        intent.putExtra("channel", st);
        startActivity(intent);
    }
    private void intent_url(String url, String title) {
        Intent intent = new Intent();
        intent.putExtra("url", url);// 注意传过去的是String类型那么那边取值就必须根据类型来取值
        intent.putExtra("title", title);// 注意传过去的是String类型那么那边取值就必须根据类型来取值
        intent.setClass(this, WebView_PayActivity.class);// 这里如果放在事件被点击为什么会报错
        startActivity(intent);
    }
}
