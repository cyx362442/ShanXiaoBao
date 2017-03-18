package com.zhongbang.sxb.colleciton;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongbang.sxb.R;

public class WebView_PayActivity extends AppCompatActivity implements View.OnClickListener {
    private String mPhone;
    private WebView mWebView;
    private RelativeLayout mLoading;
    private AnimationDrawable mDrawable;
    private ImageView mImg_load;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view__pay);
        TextView tv_title = (TextView) findViewById(R.id.textView_registered);
        String url = getIntent().getStringExtra("url");
        String chanel = getIntent().getStringExtra("chanel");
        if(chanel!=null){
            tv_title.setText(chanel);
        }
        mLoading = (RelativeLayout) findViewById(R.id.rl_load);//动画
        mImg_load = (ImageView) findViewById(R.id.image_load);
        SharedPreferences sp = getSharedPreferences("users", Context.MODE_PRIVATE);
        mPhone = sp.getString("name", "");
        startAnim();//启动动画
        setWebView(url);//设置webView
        findViewById(R.id.imageView_return).setOnClickListener(this);//返回
    }
    /**
     * 启动动画
     */
    private void startAnim() {
        mLoading.setVisibility(View.VISIBLE);
        mDrawable = (AnimationDrawable) mImg_load.getDrawable();
        mDrawable.start();
    }
    /**
     * 设置webView
     * @param url
     */
    private void setWebView(String url) {
        mWebView = (WebView) findViewById(R.id.webView2);
        WebSettings settings = mWebView.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        settings.setJavaScriptEnabled(true);
        //设置可以访问文件
        settings.setAllowFileAccess(true);
        //设置支持缩放
        settings.setBuiltInZoomControls(true);
        mWebView.loadUrl(url);// 加载url网站
        mWebView.setWebViewClient(new MyWebViewClient()); //设置Web视图,只能在设定的布局范围内跳转
        mWebView.setWebChromeClient(new MyWebChromeClient());//监测网页加载时度，最好放在onPageFinished,否则遇到重定向加载网页时newProgress会以第一次加载的URL为准
    }
    class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            view.loadUrl(url);
            return true;
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            //监测网页加载时度，最好放在onPageFinished,否则遇到重定向加载网页时newProgress会以第一次加载的URL为准
            mWebView.setWebChromeClient(new MyWebChromeClient());
            super.onPageFinished(view, url);
        }
    }
    /**
     * 自定义类继承WebChromeClient 得到网页加载网页情况
     */
    class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            String url = mWebView.getUrl();//得到当前webView的网址
            if(newProgress==100){
                //关闭动画
                mDrawable.stop();
                mLoading.setVisibility(View.INVISIBLE);
            }
        }
    }
    /**
     * 各控件点击事件
     */
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.imageView_return:
                finish();
                break;
            default:
                break;
        }
    }
}
