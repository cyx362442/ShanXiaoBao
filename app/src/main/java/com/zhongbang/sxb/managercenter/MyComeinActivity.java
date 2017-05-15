package com.zhongbang.sxb.managercenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.zhongbang.sxb.R;
import com.zhongbang.sxb.account.InterfaceUrl;
import com.zhongbang.sxb.application.ExitAppliation;
import com.zhongbang.sxb.bean.MyComing;
import com.zhongbang.sxb.colleciton.WebView_PayActivity;
import com.zhongbang.sxb.httputils.DownHTTP;
import com.zhongbang.sxb.httputils.VolleyResultListener;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MyComeinActivity extends AppCompatActivity implements View.OnClickListener {
    private final String urlIncome="http://chinazbhf.com:8081/SHXB/POST";
    private final String url="http://chinazbhf.com:8081/sxb/app/pay/incoming.app?username=";

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
    private TextView mTvTotalWxPay;
    private TextView mTvUnsettleWxPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_comein);
        ExitAppliation.getInstance().addActivity(this);
        SharedPreferences sp = getSharedPreferences("users", Context.MODE_PRIVATE);
        mPhone = sp.getString("name", "");
        startAnim();
        initControl();
        StringBuilder b = new StringBuilder(mPhone);
        DownHTTP.getVolley(url + mPhone + "&sign=" + getMD5(b.reverse().toString()), new VolleyResultListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MyComeinActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                mDrawable.stop();
                mRel_loading.setVisibility(View.GONE);
            }
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                MyComing myComing = gson.fromJson(response, MyComing.class);
                if(myComing.getStatus().equals("0")){
                    return;
                }
                MyComing.DataBean data = myComing.getData();
                text_ye.setText(data.getTotalRemaining());
                text_ljsr.setText(data.getTotalMoney());
                text_wdljsr.setText(data.getTotalEarnings());
                text_yskljsr.setText(data.getTotalIncoming());
                text_frljsr.setText(data.getTotalProfit());
                text_frljsrye.setText(data.getUnsettledProfit());
                mTvTotalWxPay.setText(data.getTotalWxpay());
                mTvUnsettleWxPay.setText(data.getUnsettledWxpay());
                text_zfbt0.setText(data.getTotalAlipay());
                text_zfbt0ye.setText(data.getUnsettledAlipay());
                text_zgylt0.setText(data.getTotalUnionpay());
                text_zgylt0ye.setText(data.getUnsettledUnionpay());

                mDrawable.stop();
                mRel_loading.setVisibility(View.GONE);
            }
        });

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
        text_zfbt0 = (TextView) findViewById(R.id.text4_fenrun);
        text_zfbt0ye = (TextView) findViewById(R.id.text4_kejiesuan);
        text_zgylt0 = (TextView) findViewById(R.id.text5_fenrun);
        text_zgylt0ye = (TextView) findViewById(R.id.text5_kejiesuan);

        mTvTotalWxPay = (TextView) findViewById(R.id.tv_totalWXPay);
        mTvUnsettleWxPay = (TextView) findViewById(R.id.tv_unsettledWxpay);
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
                intent_url(InterfaceUrl.url+"/tg/customer_list.html?id="
                                + mPhone,
                        "分润累计收入");
                break;
            case R.id.text1_jiesuan:
//                intent_url(
//                        InterfaceUrl.url+"/tg/customer_list.html?id="+mPhone,
//                        "分润累计收入");
                break;
            case R.id.text3_jiesuan:
//                intent_url(zgylt1, "O2O微信T+1");
                finish();
                break;
            case R.id.text4_jiesuan:
//                goto_jiesuan("支付宝");
                finish();
                break;
            case R.id.text5_jiesuan:
//                intent_url(zgylt1, "中国银联T+1");
                finish();
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
        intent.putExtra("chanel", title);// 注意传过去的是String类型那么那边取值就必须根据类型来取值
        intent.setClass(this, WebView_PayActivity.class);// 这里如果放在事件被点击为什么会报错
        startActivity(intent);
    }
    public String getMD5(String info)
    {
        try
        {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(info.getBytes("UTF-8"));
            byte[] encryption = md5.digest();

            StringBuffer strBuf = new StringBuffer();
            for (int i = 0; i < encryption.length; i++)
            {
                if (Integer.toHexString(0xff & encryption[i]).length() == 1)
                {
                    strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
                }
                else
                {
                    strBuf.append(Integer.toHexString(0xff & encryption[i]));
                }
            }

            return strBuf.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            return "";
        }
        catch (UnsupportedEncodingException e)
        {
            return "";
        }
    }
}
