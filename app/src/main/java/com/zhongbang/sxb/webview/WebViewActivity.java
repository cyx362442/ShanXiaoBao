package com.zhongbang.sxb.webview;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongbang.sxb.R;

public class WebViewActivity extends AppCompatActivity implements View.OnClickListener {
    private AnimationDrawable mDrawable;
    private RelativeLayout mRel_loading;
    private TextView mText;
    private WebView mWv;
    private TextView mTitle;
    private ImageView mImg_load;
    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        mUrl = intent.getStringExtra("url");
        initContents();
        mTitle.setText(title);
        findViewById(R.id.imageView1).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setWebView();
        startAnim();
    }

    /**
     * 初始化各组件
     */
    private void initContents() {
        mText = (TextView) findViewById(R.id.textView1);
        mTitle = (TextView) findViewById(R.id.textView3);
        mWv = (WebView) findViewById(R.id.webView2);
        mImg_load = (ImageView) findViewById(R.id.image_load);
        mRel_loading = (RelativeLayout) findViewById(R.id.rl_load);
    }

    /**
     * 启动动画
     */
    private void startAnim() {
        mDrawable = (AnimationDrawable) mImg_load.getDrawable();
        mDrawable.start();
    }

    private void setWebView() {
        WebSettings settings = mWv.getSettings();
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        // 开启 DOM storage API 功能
        settings.setDomStorageEnabled(true);
        // 设置WebView属性，能够执行Javascript脚本
        settings.setJavaScriptEnabled(true);
        // 设置可以访问文件
        settings.setAllowFileAccess(true);
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);// 渲染优先级升级，提高加载速度
        // 设置支持缩放
        settings.setBuiltInZoomControls(true);
        //加载第三方网页时自适应手机屏幕分辨率
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        mWv.setWebChromeClient(new MyWebChromeClient());// 监测webview加载情况
        mWv.setWebViewClient(new MyWebViewClient()); // 设置Web视图,只能在设定的布局范围内跳转
        mWv.loadUrl(mUrl);// 加载url网站
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView1:
                if (mWv.canGoBack()) {
                    mWv.goBack();
                } else {
                    finish();
                }
                break;
        }
    }

    class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }
    }

    class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, final int newProgress) {
            // TODO Auto-generated method stub
            if (newProgress == 100) {
                // 启用子线程更新UI控件
                new Thread() {
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mWv.getSettings().setBlockNetworkImage(false);
                                mDrawable.stop();
                                mRel_loading.setVisibility(View.GONE);
                                mText.setText(newProgress + "%");
                            }
                        });
                    }
                }.start();
            }
        }
    }
}
