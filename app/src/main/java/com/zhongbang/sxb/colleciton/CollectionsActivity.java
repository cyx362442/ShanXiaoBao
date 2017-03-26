package com.zhongbang.sxb.colleciton;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.zhongbang.sxb.R;
import com.zhongbang.sxb.account.InterfaceUrl;
import com.zhongbang.sxb.account.PersonalDataActivity;
import com.zhongbang.sxb.application.ExitAppliation;
import com.zhongbang.sxb.httputils.DownHTTP;
import com.zhongbang.sxb.httputils.VolleyResultListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;

public class CollectionsActivity extends AppCompatActivity implements View.OnClickListener {
    private final String auditUrl="http://chinazbhf.com/app/user_data_return.aspx";
    private final String wUrl="http://chinazbhf.com/pay_wangye/wxpay.html?user=";
    private final String zUrl="http://chinazbhf.com/pay_wangye/alipay.html?user=";
    private final String kUrl="http://chinazbhf.com/pay_wangye/Ybt1Pay.html?user=";

    private HashMap<String,String>hashMap_user=new HashMap<String,String>();
    private String str="";
    private TextView mMoney;//显示输入金额
    private String phone;
    private RelativeLayout mLoading;
    private ImageView mImg_load;
    private AnimationDrawable mDrawable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collections);
        ExitAppliation.getInstance().addActivity(this);
        SharedPreferences sp = getSharedPreferences("users", Context.MODE_PRIVATE);
        phone = sp.getString("name", "");
        mLoading = (RelativeLayout)findViewById(R.id.rl_load);//动画
        mImg_load = (ImageView)findViewById(R.id.image_load);
        mDrawable = (AnimationDrawable) mImg_load.getDrawable();
        mLoading.setVisibility(View.INVISIBLE);

        mMoney = (TextView)findViewById(R.id.textView6);
        initClick();//按键点击监听
    }
    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if(!str.equals("")){//记住上次输入的金额的状态
            mMoney.setText(str+" ");
        }
    }
    /**
     *获得登录者信息
     */
    private void getUserData(String money,String channel) {
        hashMap_user.clear();
        hashMap_user.put("username", phone);
        HTTP_users(money,channel);
    }
    /**
     * 发送post请求,从服务器获取用户信息。
     */
    private void HTTP_users(final String money,final String channel) {
        DownHTTP.postVolley(auditUrl, hashMap_user,
                new VolleyResultListener() {
                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        // TODO Auto-generated method stub
                        stopAnim();
                    }
                    @Override
                    public void onResponse(String arg0) {
                        // TODO Auto-generated method stub
                        if(TextUtils.isEmpty(arg0)){
                            Users.setDialog(CollectionsActivity.this);
                        }
                        try {
                            //解析Json数据，获取店员级别、审核状态。
                            JSONArray jsonArray = new JSONArray(arg0);
                                String userAudit = jsonArray.getJSONObject(0).getString("audit");
                                if(channel.equals("支付宝")){//支付宝
                                    toPay(zUrl+phone+"&je="+money,channel,money);
                                }else if(userAudit.equals("已审核")&&channel.equals("微信")){
                                    toPay(wUrl+phone+"&zfjeall"+money,channel,money);
                                }else if(userAudit.equals("已审核")&&channel.equals("中国银联")){
                                    toPay(kUrl+phone+"&je="+money,channel,money);
                                }else{
                                    Users.setDialog(CollectionsActivity.this);
                                }
                            stopAnim();
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            stopAnim();
                        }
                    }
                });
        }
    /**
     * 数字按键点击监听
     */
    private void initClick() {
        findViewById(R.id.image_return).setOnClickListener(this);
        findViewById(R.id.ImageView02).setOnClickListener(this);
        findViewById(R.id.ImageView11).setOnClickListener(this);
        findViewById(R.id.ImageView04).setOnClickListener(this);
        findViewById(R.id.ImageView03).setOnClickListener(this);
        findViewById(R.id.ImageView05).setOnClickListener(this);
        findViewById(R.id.ImageView07).setOnClickListener(this);
        findViewById(R.id.ImageView06).setOnClickListener(this);
        findViewById(R.id.ImageView14).setOnClickListener(this);
        findViewById(R.id.ImageView10).setOnClickListener(this);
        findViewById(R.id.ImageView09).setOnClickListener(this);
        findViewById(R.id.ImageView01).setOnClickListener(this);
        findViewById(R.id.ImageView13).setOnClickListener(this);
        findViewById(R.id.ImageView12).setOnClickListener(this);//微信支付
        findViewById(R.id.ImageView08).setOnClickListener(this);//支付宝
        findViewById(R.id.img_collection).setOnClickListener(this);//刷卡收款
    }
    @Override
    public void onClick(View arg0) {
        String money="";
        switch (arg0.getId()) {
            case R.id.image_return:
                finish();
                break;
            case R.id.ImageView01://"0"
                writeMsg("0");
                break;
            case R.id.ImageView02://"."
                if(!str.contains(".")&&!str.equals("")){
                    writeMsg(".");
                }
                break;
            case R.id.ImageView13://"1"
                writeMsg("1");
                break;
            case R.id.ImageView10://"2"
                writeMsg("2");
                break;
            case R.id.ImageView09://"3"
                writeMsg("3");
                break;
            case R.id.ImageView14://"4"
                writeMsg("4");
                break;
            case R.id.ImageView07://"5"
                writeMsg("5");
                break;
            case R.id.ImageView06://"6"
                writeMsg("6");
                break;
            case R.id.ImageView04://"7"
                writeMsg("7");
                break;
            case R.id.ImageView03://"8"
                writeMsg("8");
                break;
            case R.id.ImageView05://"9"
                writeMsg("9");
                break;
            case R.id.ImageView11://"del"
                if(str.length()>0){
                    str=str.substring(0,str.length()-1);
                }
                mMoney.setText(str+" ");
                break;
            case R.id.ImageView12://微信支付
                money = mMoney.getText().toString().trim();
//			if(Double.valueOf(money)>1000){
//				TsUtils.show(getActivity(), "微信支付金额不能大于1000");
//			}else{
                startAnim();
                getUserData(money,"微信");
//			}
                break;
            case R.id.ImageView08://支付宝
                money = mMoney.getText().toString().trim();
                if(money.contains(".")){
                    Toast.makeText(this,"金额不能包含小数点",Toast.LENGTH_LONG).show();
                }else{
                    startAnim();
                    getUserData(money,"支付宝");
                }
                break;
            case R.id.img_collection:
                money = mMoney.getText().toString().trim();
                startAnim();
                getUserData(money,"中国银联");
                break;
            default:
                break;
        }
    }
    private void toPay(String url,String chanel,String money) {
        if(str.length()==0){
            Toast.makeText(this,"请输入金额",Toast.LENGTH_LONG).show();
        }else{
            //去掉金额前面误输入的“0”
            if(Double.parseDouble(money)>=1&&money.startsWith("0")){
                money=money.replaceFirst("^0*", "");
            }
            if(money.endsWith(".")){
                money=money.substring(0,money.indexOf("."));
            }
            Intent intent = new Intent(this,WebView_PayActivity.class);
            intent.putExtra("url", url);
            intent.putExtra("chanel", chanel);
            startActivity(intent);
        }
    }
    private void writeMsg(String num){
        str=str+num;
        mMoney.setText(str+" ");
    }
    private void startAnim() {
        mDrawable.start();
        mLoading.setVisibility(View.VISIBLE);
    }
    private void stopAnim() {
        mDrawable.stop();
        mLoading.setVisibility(View.INVISIBLE);
    }
}
