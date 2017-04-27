package com.zhongbang.sxb.webview;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.net.http.SslError;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXWebpageObject;
import com.zhongbang.sxb.R;
import com.zhongbang.sxb.View.ShareActivity;

public class Link2Activity extends AppCompatActivity implements View.OnClickListener {
    private String urlWeb="http://chinazbhf.com:8081/SHXBWD/mjgl/sy.html?id=";

    private ValueCallback<Uri> mUploadMessage;
    private final static int FILECHOOSER_RESULTCODE = 1;
    /** 进度条控件[成员变量] */
    private ProgressBar mBar;
    /** WebView控件[成员变量] */
    private WebView mWebView;
    /** 链接网址 */
    private String url;
    /** 旋转图片 */
    private ImageView image_rotating;
    /** 加载布局 */
    private View layout_rotating;
    /** 标题名 */
    private TextView text_title;
    private String title;
    private AnimationDrawable mDrawable;
    private RelativeLayout mRel_loading;
    RelativeLayout rel_setphoto_ref;
    RelativeLayout imageView1;
    RelativeLayout layout_exit;

    View tishi;
    IWXAPI wxApi;
    private String mPhone;

    private void wechatShare(int flag) {
        String url = "http://huanqiuzf.com:8080/YB/html/"
                + mPhone + ".html";
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "扫一扫,轻松付款";
        msg.description = "请客户使用微信扫一扫付款";
        // 这里替换一张自己工程里的图片资源
        Bitmap thumb = BitmapFactory.decodeResource(getResources(),
                R.mipmap.logo);
        msg.setThumbImage(thumb);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession
                : SendMessageToWX.Req.WXSceneTimeline;
        wxApi.sendReq(req);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link2);
        SharedPreferences sp = getSharedPreferences("users", MODE_PRIVATE);
        mPhone = sp.getString("name", "");
        image_rotating = (ImageView) findViewById(R.id.image_load);
        layout_rotating = findViewById(R.id.rl_load);
        startAnim();
        text_title = (TextView) findViewById(R.id.textView_title);
        imageView1 = (RelativeLayout) findViewById(R.id.imageView1);
        rel_setphoto_ref = (RelativeLayout) findViewById(R.id.rel_setphoto_ref);
        layout_exit = (RelativeLayout) findViewById(R.id.layout_exit);
        findViewById(R.id.imageView_return).setOnClickListener(this);

        // 【注意添加权限 android.permission.INTERNET】
        mBar = (ProgressBar) findViewById(R.id.progressBar1);// 实例化控件
        mWebView = (WebView) findViewById(R.id.webView);// 实例化控件

        // 设置
        WebSettings settings = mWebView.getSettings();
        // 支持js
        settings.setJavaScriptEnabled(true);
        // 实现内部使用网址带有拨号功能的时候可以弹出拨号,支持HTML5新特性
        settings.setAllowFileAccess(true);// 设置允许访问文件数据
        settings.setBuiltInZoomControls(true);// 设置支持缩放
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            // 参数newProgress获取当前加载的网页百分比
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    layout_rotating.setVisibility(View.GONE);// 隐藏
                    mBar.setVisibility(View.GONE);// 隐藏进度条
                } else {
                    layout_rotating.setVisibility(View.VISIBLE);// 显示
                    mBar.setVisibility(View.GONE);// 显示进度条
                    mBar.setProgress(newProgress);// 设置当前进度条的进度
                    String url2 = mWebView.getUrl();
                    if ("http://120.24.166.34:8080/YB/PostOrGet".equals(url2)&&title.equals("微信支付")) {
                        imageView1.setVisibility(View.VISIBLE);
                    }
                }
            }
            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                        String acceptType, String capture) {
                mUploadMessage = uploadMsg;
                rel_setphoto_ref.setVisibility(View.VISIBLE);

            }

            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                        String acceptType) {
                mUploadMessage = uploadMsg;
                rel_setphoto_ref.setVisibility(View.VISIBLE);
            }

            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUploadMessage = uploadMsg;
                rel_setphoto_ref.setVisibility(View.VISIBLE);
            }
            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     final JsResult result) {
                // TODO Auto-generated method stub
                AlertDialog.Builder b2 = new AlertDialog.Builder(
                        Link2Activity.this)
                        .setTitle("温馨提示")
                        .setMessage(message)
                        .setPositiveButton("确认",
                                new AlertDialog.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        result.confirm();
                                    }
                                });
                b2.setCancelable(false);
                b2.create();
                b2.show();
                return true;
            }
        });
        // 设置字体
        // 设置链接不跳出webview
        mWebView.setWebViewClient(new MyWebView());
        Intent get_intent = getIntent();// 建立取传来消息的对象
//        url = get_intent.getStringExtra("url");
        String title = get_intent.getStringExtra("title");
        mWebView.loadUrl(urlWeb+mPhone);
//        title = get_intent.getStringExtra("title");
        text_title.setText(title);
        // if (title.equals("云商微店")) {
        // text_wodeweidian.setVisibility(View.VISIBLE);
        // tishi.setVisibility(View.VISIBLE);
        // }
//        if (this.title.equals("123")) {
//            layout_exit.setVisibility(View.GONE);
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageView_return:
                finish();
                break;
        }
    }

    class MyWebView extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // 当有新连接时，使用当前的 WebView
            // 调用拨号程序
            if (url.startsWith("tel:")) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
            else if(url.startsWith("intent:")||url.startsWith("jhs:")){
                //注意：由于此处url是intent:、jhs:开头，后面不能像其它url那样跟随view.loadUrl(url);否则将无法加载
                Log.e("url====", url);
                return true;
            }
            else {
                if (url.equals("http://120.24.166.34:8080/YB/PostOrGet")&&title.equals("微信支付")) {
                    imageView1.setVisibility(View.VISIBLE);
                }
                else if (url
                        .equals("http://59.151.121.118/video/zhifutong.apk")) {
                    Uri uri = Uri
                            .parse("http://59.151.121.118/video/zhifutong.apk");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
                else if (url.lastIndexOf("zccg.html?cg=") > 0) {
                    Log.e("url====", url);
                    String[] a = url.split("cg=");
                    String userneme = a[1];
//                    chaoZuoSql.qingkong_user_data();// 清空数据
//                    chaoZuoSql.zeng_user_data(userneme, "132456");// 增加数据
                    mWebView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
//                            intent_you(TheMainActivity.class);
                            finish();
                        }
                    }, 3000);
                }
                else if (url.lastIndexOf("sc=sc") >= 0) {
                    Log.e("url====", url);
                    Intent intent = new Intent(Link2Activity.this,
                            ShareActivity.class);
                    intent.putExtra("url",
                            "http://chinapxsh.com:8080/QMWDWD/mj/index.html?id="
                                    + mPhone);
                    intent.putExtra("tt", "的微商店");
                    startActivity(intent);
                } else if (url.lastIndexOf("&usertg") >= 0) {
                    Log.e("url====", url);
                    Intent intent = new Intent(Link2Activity.this,
                            ShareActivity.class);
                    String[] array = url.split("\\?id");
                    intent.putExtra("url",
                            "http://chinapxsh.com:8080/QMWDWD/mj/spxq_gm.html?id"
                                    + array[1]);
                    intent.putExtra("tt", "的微商品");
                    startActivity(intent);
                }
                view.loadUrl(url);
            }
            return true;
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                       SslError error) {
            // TODO Auto-generated method stub
            handler.proceed();// 忽略SSL验证解决特殊网址被禁止跳转的问题
            super.onReceivedSslError(view, handler, error);
        }
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
            if ("http://120.24.166.34:8080/YB/PostOrGet".equals(url)&&title.equals("微信支付")) {
                imageView1.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    // 任何按钮点击事件
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:// 如果是back键
                if (mWebView.canGoBack())// 判断WebView控件是否可返回上一个页面
                {
                    mWebView.goBack();// 返回上一个访问的页面
                    return true;
                } else {
                    finish();
                }
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

//    public void intent_you(Class<TheMainActivity> class1) {
//        // TODO Auto-generated method stub
//        startActivity(new Intent(this, class1));
//        finish();
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        if (requestCode == 1) {
            if (null == mUploadMessage)
                return;
            Uri result = intent == null || resultCode != RESULT_OK ? null
                    : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        } else {
            try {
                Bitmap bitmap = (Bitmap) intent.getExtras().get("data");// 获取图片
                Uri result = Uri.parse(MediaStore.Images.Media.insertImage(
                        getContentResolver(), bitmap, null, null));
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }

    public void Onclick_cancel(View view) {
        rel_setphoto_ref.setVisibility(View.GONE);
        mUploadMessage.onReceiveValue(null);
    }
    public void Onclick_xiangce(View view) {
        Intent intent = new Intent();
		/* 开启Pictures画面Type设定为image */
        intent.setType("image/*");
		/* 使用Intent.ACTION_GET_CONTENT这个Action */
        intent.setAction(Intent.ACTION_GET_CONTENT);
		/* 取得相片后返回本画面 */
        startActivityForResult(intent, 1);
        rel_setphoto_ref.setVisibility(View.GONE);
    }

    public void Onclick_paizhao(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 通过意图对象跳转到手机自带的拍照功能
        startActivityForResult(intent, 0);// 等待回传消息
        rel_setphoto_ref.setVisibility(View.GONE);
    }

    private void startAnim() {
        mDrawable = (AnimationDrawable) image_rotating.getDrawable();
        mDrawable.start();
    }
    private void stopAnim(){
        mDrawable.stop();
        mRel_loading.setVisibility(View.GONE);
    }
}
