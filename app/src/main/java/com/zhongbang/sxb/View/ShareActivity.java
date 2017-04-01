package com.zhongbang.sxb.View;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXWebpageObject;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.zhongbang.sxb.R;
import com.zhongbang.sxb.account.InterfaceUrl;
import com.zhongbang.sxb.httputils.DownHTTP;
import com.zhongbang.sxb.httputils.VolleyResultListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ShareActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String WX_APP_ID = "wxcc14276107059709";
    private IWXAPI wxApi;
    String phone = "";// 手机号码[账号]-phone
    String sharecontent;
    private Tencent mTencent;
    private String name;
    private String url = InterfaceUrl.url + "/saoyisao/index.html";
    String tt = "闪销宝";
    private Bitmap thumb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        mTencent = Tencent.createInstance("1104928364",
                this.getApplicationContext());
        wxApi = WXAPIFactory.createWXAPI(this, WX_APP_ID);
        wxApi.registerApp(WX_APP_ID);
        SharedPreferences sp = getSharedPreferences("users", MODE_PRIVATE);
        phone = sp.getString("name", "");
        http_read_name(phone);
        url = InterfaceUrl.url + "/saoyisao/index.html?user=" + phone;
        initUI();
    }

    private void initUI() {
        findViewById(R.id.imageView_sms).setOnClickListener(this);
        findViewById(R.id.imageView_weixin).setOnClickListener(this);
        findViewById(R.id.imageView_pengyou).setOnClickListener(this);
        findViewById(R.id.QQ_share).setOnClickListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return super.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView_sms:
                sendSMS();
                finish();
                break;
            case R.id.imageView_weixin:
                // Toast.makeText(this, "微信分享", Toast.LENGTH_SHORT).show();
                wechatShare(0);
                finish();
                break;
            case R.id.imageView_pengyou:
                wechatShare(1);
                finish();
                break;
            case R.id.QQ_share:
                // shareToQQzone();
                onClickShare();
                finish();
                break;
            default:
                break;
        }
    }

    private void sendSMS() {

        Uri uri = Uri.parse("SMSto:");
        Intent sentintent = new Intent(Intent.ACTION_VIEW, uri);
        sentintent.putExtra("sms_body", sharecontent + url);
        sentintent.setType("vnd.android-dir/mms-sms");
        startActivityForResult(sentintent, 1001);
    }

    private void wechatShare(int flag) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = tt;
        msg.description = sharecontent;
        // 这里替换一张自己工程里的图片资源
//		if (tt.equals("全民微商")) {
//			thumb = BitmapFactory.decodeResource(getResources(),
//					R.drawable.logo2);
//		} else {
//			thumb = BitmapFactory.decodeResource(getResources(),
//					R.drawable.ic_launcher);
//		}
        thumb= BitmapFactory.decodeResource(getResources(), R.mipmap.logo80);//注意图片不能太大，否则无法分享
        msg.setThumbImage(thumb);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession
                : SendMessageToWX.Req.WXSceneTimeline;
        wxApi.sendReq(req);
    }

    private void onClickShare() {
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,
                QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, tt);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, sharecontent);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, url);
//		if (tt.equals("全民微店")) {
//			params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,
//					InterfaceUrl.interface_url + "/fx.png");
//
//		} else {
//			params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,
//					InterfaceUrl.interface_url + "/logo.png");
//
//		}
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,
                "http://7xtejr.com1.z0.glb.clouddn.com/logo_qmws.png");
        mTencent.shareToQQ(ShareActivity.this, params, new BaseUiListener());
    }

    private void shareToQQzone() {
        final Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,
                QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);

        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, tt);
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, sharecontent);
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, url);
        ArrayList<String> imageUrls = new ArrayList();
        imageUrls.add(InterfaceUrl.url + "/fx.png");
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
        params.putInt(QzoneShare.SHARE_TO_QQ_EXT_INT,
                QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);

        mTencent.shareToQzone(ShareActivity.this, params, new BaseUiListener());
    }

    private class BaseUiListener implements IUiListener {

        @Override
        public void onCancel() {
            // TODO Auto-generated method stub
            Toast.makeText(ShareActivity.this, "QQ分享取消", 1).show();

        }

        @Override
        public void onComplete(Object arg0) {
            // TODO Auto-generated method stub
            Toast.makeText(ShareActivity.this, "QQ分享完成", 1).show();

        }

        @Override
        public void onError(UiError arg0) {
            // TODO Auto-generated method stub
            Toast.makeText(ShareActivity.this, "QQ分享出错", 1).show();

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null != mTencent)
            mTencent.onActivityResult(requestCode, resultCode, data);
    }

    private void http_read_name(final String phone) {
        HashMap<String, String> HashMap_post = new HashMap<String, String>();
        HashMap_post.put("username", phone);// 此时这里就是要传的网页上对应的参数name的值
		/* 使用POST请求 */
        DownHTTP.postVolley(InterfaceUrl.url
                        + "/app/user_data_return.aspx", HashMap_post,
                new VolleyResultListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        Toast.makeText(ShareActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String arg0) {
                        try {
                            JSONArray jsonArray = new JSONArray(arg0);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray
                                        .getJSONObject(i);

                                name = jsonObject.getString("name");
                                sharecontent = name
                                        + "邀请您一起来注册，手机免费开店赚钱，点击免费领取手机POS机，边分享边赚佣金";
                                weidianfenxiang();
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            Toast.makeText(ShareActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    protected void weidianfenxiang() {
        // TODO Auto-generated method stub
        String str = getIntent().getStringExtra("url");
        Log.e("str====", str);
        if (str != null) {
//			sharecontent = name + getIntent().getStringExtra("contents");
            url =str;
            sharecontent=name+"邀请您一起来手机免费开店赚钱，免费领取手机POS机，边分享边赚钱，请点击安装中国银联渠道服务全民微商互联网金融+微商平台";
            if(name==null||name.equals("")){
                tt="环球微商";
            }else{
//				tt = name + getIntent().getStringExtra("tt");
                tt=name;
            }
        }
    }
}
