package com.zhongbang.sxb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXWebpageObject;
import com.tencent.open.utils.ThreadManager;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.zhongbang.sxb.zxing.EncodingHandler;

import java.io.WriteAbortedException;

public class ShareActivity extends AppCompatActivity implements View.OnClickListener {
    public String from="";
    private String url="http://chinazbhf.com:8081/SHXBZC/zhuce/registered.html?id=";
    private String mPhone;
    private LinearLayout mShares;
    private ImageView mImage;
    private ImageView mImg_return;
    private ImageView mQrImgImageView;
    public static Tencent mTencent;
    private final String WX_APP_ID = "wxa812747aa7e71411";
    private IWXAPI api;
    // 构造一个Req
    private SendMessageToWX.Req req = new SendMessageToWX.Req();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share2);
        SharedPreferences sp = getSharedPreferences("users", Context.MODE_PRIVATE);
        mPhone = sp.getString("name", "");
        mTencent = Tencent.createInstance("1106056864", this);
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, WX_APP_ID, false);
        api.registerApp(WX_APP_ID);

        mImg_return = (ImageView)findViewById(R.id.imageView1);
        //如果被“升级界面”或“更多界面”所调用，返回按钮就设显示
        if(from.equals("mf")||from.equals("uf")){
            mImg_return.setVisibility(View.VISIBLE);
        }
        mImage = (ImageView) findViewById(R.id.imageView4);//渐变透明图片
        mShares = (LinearLayout) findViewById(R.id.LinearLayout_share);//各路分享
        mQrImgImageView = (ImageView) findViewById(R.id.imageView5);//二维码图片

        mImg_return.setOnClickListener(this);
        findViewById(R.id.rel_text1).setOnClickListener(this);//分享监听
        findViewById(R.id.ll_QQ).setOnClickListener(this);
        findViewById(R.id.ll_weixin).setOnClickListener(this);
        findViewById(R.id.ll_pengyou).setOnClickListener(this);
        findViewById(R.id.ll_message).setOnClickListener(this);
        startAlpha();
        createCode();//生成二维码
    }
    /**
     * 生成二维码
     */
    private void createCode() {
        String contentString=url+mPhone;
        if(contentString!=null&&contentString.trim().length()>0){
            try {
                //根据字符串生成二维码图片并显示在界面上，第二个参数为图片的大小（350*350）
                Bitmap logo= BitmapFactory.decodeResource(this.getResources(), R.mipmap.logo80);//中间logo
                Bitmap qrCodeBitmap = EncodingHandler.createQRCode(contentString, 350,logo);
                mQrImgImageView.setImageBitmap(qrCodeBitmap);
            } catch (WriteAbortedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (WriterException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    /**
     * 启动渐变透明补间动画
     */
    private void startAlpha() {
        Animation loadAnimation = AnimationUtils.loadAnimation(this,
                R.anim.alpha_share);
        mImage.startAnimation(loadAnimation);
    }
    /**
     * 各控件点击事件
     */
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.imageView1://返回到之前界面
                finish();
                break;
            case R.id.rel_text1://分享监听
                startTranslate();//平移补间动画
                break;
            case R.id.ll_QQ://QQ分享
                shareQQ();
                break;
            case R.id.ll_weixin://微信分享
                req.scene=SendMessageToWX.Req.WXSceneSession;
                shareWeixin(req);
                break;
            case R.id.ll_pengyou://朋友圈分享
                req.scene=SendMessageToWX.Req.WXSceneTimeline;
                shareWeixin(req);
                break;
            case R.id.ll_message://短信分享
                sendMsg();
                break;
            default:
                break;
        }
    }
    //QQ分享
    private void shareQQ() {
        // TODO Auto-generated method stub
        final Bundle params = new Bundle();
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "闪销宝众享购物平台");//分享标题
        //分享图标
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,"http://od27wgfkz.bkt.clouddn.com/shanxiaobao.png");
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, url+mPhone);//分享网址
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "闪销宝众享购物平台=消费者+商家+合伙人=边消费边赚钱+协同创新+协同致富");//分享内容
        doShareToQQ(params);
    }
    private void doShareToQQ(final Bundle params) {
        // QQ分享要在主线程做
        ThreadManager.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                if (null != mTencent) {
                    mTencent.shareToQQ(ShareActivity.this, params, qqShareListener);
                }
            }
        });
    }
    //QQ分享失败监听
    IUiListener qqShareListener = new IUiListener() {
        @Override
        public void onCancel() {
            Toast.makeText(ShareActivity.this,"分享取消",Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onComplete(Object response) {
            Toast.makeText(ShareActivity.this,"分享完成",Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onError(UiError e) {
            // TODO Auto-generated method stub
            Toast.makeText(ShareActivity.this,"分享出错",Toast.LENGTH_SHORT).show();
        }
    };
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null != mTencent)
            mTencent.onActivityResult(requestCode, resultCode, data);
    }
    /**
     * 短信分享
     */
    private void sendMsg() {
        String smsBody = "闪销宝众享购物平台=消费者+商家+合伙人=边消费边赚钱+协同创新+协同致富"+(url+mPhone);
        Uri smsToUri = Uri.parse( "smsto:" );
        Intent sendIntent =  new  Intent(Intent.ACTION_VIEW, smsToUri);
        //sendIntent.putExtra("address", "123456"); // 电话号码，这行去掉的话，默认就没有电话
        //短信内容
        sendIntent.putExtra( "sms_body", smsBody);
        sendIntent.setType( "vnd.android-dir/mms-sms" );
        startActivityForResult(sendIntent, 1002 );
    }
    /**
     * 微信、朋友圈分享
     * @param req
     */
    private void shareWeixin(SendMessageToWX.Req req) {
        String url_share = url+mPhone;
        // 初始化一个WXTextObject对象
        WXWebpageObject webpage = new WXWebpageObject();//网页类型分享
        webpage.webpageUrl=url_share;//分享的网址

        // 用WXTextObject对象初始化一个WXMediaMessage对象
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title="闪销宝众享购物平台";//分享的标题
        msg.description="闪销宝众享购物平台=消费者+商家+合伙人=边消费边赚钱+协同创新+协同致富";
        //替换自己工程的图片做为图标，注意：图片不能太大，否则分享失败！
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.logo80);
        msg.setThumbImage(thumb);
        req.transaction = String.valueOf(System.currentTimeMillis()); // transaction字段用于唯一标识一个请求
        req.message = msg;
        // 调用api接口发送数据到微信
        api.sendReq(req);
    }
    /**
     * 补间动画-平移
     */
    private void startTranslate() {
        mShares.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this,
                R.anim.translate_share);
        mShares.startAnimation(animation);
    }
}
