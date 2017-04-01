package com.zhongbang.sxb.account;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.zhongbang.sxb.R;
import com.zhongbang.sxb.httputils.DownHTTP;
import com.zhongbang.sxb.httputils.VolleyResultListener;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class ID_PhotoActivity extends AppCompatActivity implements View.OnClickListener {
    private HashMap<String, String>hashMap_send=new HashMap<String, String>();
    /** 身份证正面SD地址 */
    private String image1 = "";

    /** 身份证反面SD地址 */
    private String image2 = "";

    /** 银行卡正面SD地址 */
    private String image3 = "";

    /** 银行卡反面SD地址 */
    private String image4 = "";

    /** 身份证半身照SD地址 */
    private String image5 = "";

    /** 销售代表合影SD地址 */
    private String image6 = "";

    private ImageView mID_front;
    private ImageView mID_behind;
    private ImageView mBankCard_front;
    private ImageView mBankCard_behind;
    private ImageView mGroupPhoto;
    private ImageView mMyStore;
    private String mPhone;
    private int imgID;
    private Bitmap mBitmap;

    private RelativeLayout mLoading;

    private ImageView mImage_confirm;

    private TextView mText_confirm;

    private String mIsAudit;

    private Intent mIntent;

    private TextView mComplete;

    private ImageView mImage_load;

    private AnimationDrawable mDrawable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id__photo);
        SharedPreferences sp = getSharedPreferences("users", MODE_PRIVATE);
        mPhone = sp.getString("name", "");
        initControl();//初始化各控件
        startAnim();//启用帧动画
        mIsAudit = getIntent().getStringExtra("audit");
        mText_confirm.setText(mIsAudit);
        img();
        if (!mIsAudit.equals("已审核")) {//未审核
            stopAnim();
        } else {//己审核
            mComplete.setText("返回首页");
            mImage_confirm.setImageResource(R.mipmap.prompt_not_tg);
            mID_front.setClickable(false);
            mID_behind.setClickable(false);
            mBankCard_front.setClickable(false);
            mBankCard_behind.setClickable(false);
            mGroupPhoto.setClickable(false);
            mMyStore.setClickable(false);
            String url_load =InterfaceUrl.url+"/img/";
            volley_(mID_front, R.mipmap.img1, url_load + mPhone + "1.jpg");
            volley_(mID_behind, R.mipmap.img2, url_load + mPhone + "2.jpg");
            volley_(mBankCard_front, R.mipmap.img3, url_load + mPhone + "4.jpg");
            volley_(mBankCard_behind, R.mipmap.img4, url_load + mPhone + "5.jpg");
            volley_(mGroupPhoto, R.mipmap.img5, url_load + mPhone + "3.jpg");
            volley_(mMyStore, R.mipmap.img6, url_load + mPhone + "6.jpg");
        }
    }
    /** 显示图片 */
    private void img() {
        String photoPath ="/sdcard/audit/";// 保存的SD中的路径设置
        img_red(photoPath + mPhone + "img1.jpg", mID_front,
                R.id.imageView_img1);// 身份证正面
        img_red(photoPath + mPhone + "img2.jpg", mID_behind,
                R.id.imageView_img2);// 身份证反面
        img_red(photoPath + mPhone + "img3.jpg", mBankCard_front,
                R.id.imageView_img3);// 【银行卡正面】
        img_red(photoPath + mPhone + "img4.jpg", mBankCard_behind,
                R.id.imageView_img4);// 【银行卡反面】
        img_red(photoPath + mPhone + "img5.jpg", mGroupPhoto,
                R.id.imageView_img5);// 【身份证半身照】
        img_red(photoPath + mPhone + "img6.jpg", mMyStore,
                R.id.imageView_img6);// 【我的商铺】
    }
    /**
     * 审核通过返回的图片
     * @param image_photo
     * @param ID
     * @param url
     */
    private void volley_(final ImageView image_photo, final int ID, String url) {

        RequestQueue mQueue = Volley.newRequestQueue(this);//建立一个RequestQueue对象
        ImageRequest imageRequest = new ImageRequest(url,//建立一个ImageRequest对象
                new Response.Listener<Bitmap>() {
                    //图片下载成功，设置到原图片上去
                    @Override
                    public void onResponse(Bitmap response) {
                        @SuppressWarnings("deprecation")
                        Drawable drawable = new BitmapDrawable(response);
                        image_photo.setBackground(drawable);
                        stopAnim();
                    }
                    //"0, 0, Config.RGB_565":图片宽、高按比例进行压缩，此处都为0，不进行压缩。最后一个代表图片的颜色属性
                }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            //图片下载失败，设指定ID的图片到原图片上去（其实还是设为原图片）
            @Override
            public void onErrorResponse(VolleyError error) {
                image_photo.setBackgroundResource(ID);
                stopAnim();
            }
        });
        mQueue.add(imageRequest);//将ImageRequest添加到RequestQueue中
    }
    /**
     * 初始化各控件
     */
    private void initControl() {
        mLoading = (RelativeLayout) findViewById(R.id.rl_load);//帧动画
        mImage_load = (ImageView) findViewById(R.id.image_load);
        mID_front = (ImageView) findViewById(R.id.imageView_img1);//身份证正面
        mID_front.setOnClickListener(this);
        mID_behind = (ImageView) findViewById(R.id.imageView_img2);//身份证背面
        mID_behind.setOnClickListener(this);
        mBankCard_front = (ImageView) findViewById(R.id.imageView_img3);//银行卡正面
        mBankCard_front.setOnClickListener(this);
        mBankCard_behind = (ImageView) findViewById(R.id.imageView_img4);//银行卡背面
        mBankCard_behind.setOnClickListener(this);
        mGroupPhoto = (ImageView) findViewById(R.id.imageView_img5);//合照
        mGroupPhoto.setOnClickListener(this);
        mMyStore = (ImageView) findViewById(R.id.imageView_img6);//我的商铺
        mMyStore.setOnClickListener(this);
        mComplete = (TextView) findViewById(R.id.tv_next);//完成
        findViewById(R.id.rel_next).setOnClickListener(this);
        findViewById(R.id.rela_example).setOnClickListener(this);//查看案例
        findViewById(R.id.imageView_return).setOnClickListener(this);//返回上一级
        mImage_confirm = (ImageView) findViewById(R.id.image_confirm);//认证与否图标
        mText_confirm = (TextView) findViewById(R.id.text_confirm);//认证与否文本
    }
    /**
     * 等待图片对像回传
     */
    @SuppressLint("SdCardPath")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0){//拍照的图片回传
            String photoPath = "/sdcard/audit/";//获取图片的保存的文件夹，注意要跟存储的文件夹一致。
            String photoName = "";//图片获取路径
            switch (imgID) {
                case 1:
                    photoName = photoPath + mPhone + "img"+imgID+".jpg";
                    image1=photoName;
                    mBitmap = BitmapFactory.decodeFile(photoName);
                    SD_img(photoPath, photoName, mID_front, mBitmap);// 防止溢出,导致程序直接死掉,必须用到的方法
                    break;
                case 2:
                    photoName = photoPath + mPhone + "img"+imgID+".jpg";
                    image2=photoName;
                    mBitmap = BitmapFactory.decodeFile(photoName);
                    SD_img(photoPath, photoName, mID_behind, mBitmap);// 防止溢出,导致程序直接死掉,必须用到的方法
                    break;
                case 3:
                    photoName = photoPath + mPhone + "img"+imgID+".jpg";
                    image3=photoName;
                    mBitmap = BitmapFactory.decodeFile(photoName);
                    SD_img(photoPath, photoName, mBankCard_front, mBitmap);// 防止溢出,导致程序直接死掉,必须用到的方法
                    break;
                case 4:
                    photoName = photoPath + mPhone + "img"+imgID+".jpg";
                    image4=photoName;
                    mBitmap = BitmapFactory.decodeFile(photoName);
                    SD_img(photoPath, photoName, mBankCard_behind, mBitmap);// 防止溢出,导致程序直接死掉,必须用到的方法
                    break;
                case 5:
                    photoName = photoPath + mPhone + "img"+imgID+".jpg";
                    image5=photoName;
                    mBitmap = BitmapFactory.decodeFile(photoName);
                    SD_img(photoPath, photoName, mGroupPhoto, mBitmap);// 防止溢出,导致程序直接死掉,必须用到的方法
                    break;
                case 6:
                    photoName = photoPath + mPhone + "img"+imgID+".jpg";
                    image6=photoName;
                    mBitmap = BitmapFactory.decodeFile(photoName);
                    SD_img(photoPath, photoName, mMyStore, mBitmap);// 防止溢出,导致程序直接死掉,必须用到的方法
                    break;
                default:
                    break;
            }
        }else if(resultCode==RESULT_OK){//从相册获取图片
            Uri uri = data.getData();
            ContentResolver cr = this.getContentResolver();
            try {
                mBitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                String photoPath = "/sdcard/audit/";// 保存的SD中的路径设置
                String photoName ="";// 保存图片的名字
                switch (imgID) {
                    case 1:
                        photoName = photoPath + mPhone + "img"+imgID+".jpg";
                        image1=photoName;
                        SD_img(photoPath, photoName, mID_front, mBitmap);// 防止溢出,导致程序直接死掉,必须用到的方法
                        break;
                    case 2:
                        photoName = photoPath + mPhone + "img"+imgID+".jpg";
                        image2=photoName;
                        SD_img(photoPath, photoName, mID_behind, mBitmap);// 防止溢出,导致程序直接死掉,必须用到的方法
                        break;
                    case 3:
                        photoName = photoPath + mPhone + "img"+imgID+".jpg";
                        image3=photoName;
                        SD_img(photoPath, photoName, mBankCard_front, mBitmap);// 防止溢出,导致程序直接死掉,必须用到的方法
                        break;
                    case 4:
                        photoName = photoPath + mPhone + "img"+imgID+".jpg";
                        image4=photoName;
                        SD_img(photoPath, photoName, mBankCard_behind, mBitmap);// 防止溢出,导致程序直接死掉,必须用到的方法
                        break;
                    case 5:
                        photoName = photoPath + mPhone + "img"+imgID+".jpg";
                        image5=photoName;
                        SD_img(photoPath, photoName, mGroupPhoto, mBitmap);// 防止溢出,导致程序直接死掉,必须用到的方法
                        break;
                    case 6:
                        photoName = photoPath + mPhone + "img"+imgID+".jpg";
                        image6=photoName;
                        SD_img(photoPath, photoName, mMyStore, mBitmap);// 防止溢出,导致程序直接死掉,必须用到的方法
                        break;
                    default:
                        break;
                }
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    /**
     * 各控件点击监听
     */
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        String title;
        switch (v.getId()) {
            case R.id.imageView_img1:
                imgID=1;
                title="身份证正面";
                getImage(imgID, title);
                break;
            case R.id.imageView_img2:
                imgID=2;
                title="身份证背面";
                getImage(imgID, title);
                break;
            case R.id.imageView_img3:
                imgID=3;
                title="银行卡正面";
                getImage(imgID, title);
                break;
            case R.id.imageView_img4:
                imgID=4;
                title="银行卡背面";
                getImage(imgID, title);
                break;
            case R.id.imageView_img5:
                imgID=5;
                title="人卡合照";
                getImage(imgID, title);
                break;
            case R.id.imageView_img6:
                imgID=6;
                title="我的商铺";
                getImage(imgID, title);
                break;
            case R.id.rel_next://“完成”监听
                String str = mComplete.getText().toString();
                if(str.equals("完成")){
                    upLoad_image();
                }
                finish();
                break;
            case R.id.rela_example://查看案例
                mIntent = new Intent(ID_PhotoActivity.this,PhotoExampleActivity.class);
                startActivity(mIntent);
                break;
            case R.id.imageView_return://返回上一级
                mIntent = new Intent(ID_PhotoActivity.this,BankconfirmActivity.class);
                mIntent.putExtra("audit", mIsAudit);
                startActivity(mIntent);
                finish();
                break;
            default:
                break;
        }
    }
    /**
     * 证件图片上传
     */
    private void upLoad_image() {//以下3，4，5不能按顺序写，可能服务器那顺层序有误。
        String url_upLoad=InterfaceUrl.url+"/app/photo_chuan.aspx";
        MyAsyncTask asynctask;
        asynctask = new MyAsyncTask(url_upLoad, mPhone + "1.jpg",image1);
        if (!image1.equals("")) {
            asynctask.execute("身份证正面上传");
        }
        asynctask = new MyAsyncTask(url_upLoad, mPhone + "2.jpg",image2);
        if (!image2.equals("")) {
            asynctask.execute("身份证背面上传");
        }
        asynctask = new MyAsyncTask(url_upLoad, mPhone + "4.jpg",image3);
        if (!image3.equals("")) {
            asynctask.execute("银行卡正面上传");
        }
        asynctask = new MyAsyncTask(url_upLoad, mPhone + "5.jpg",image4);
        if (!image4.equals("")) {
            asynctask.execute("银行卡背面上传");
        }
        asynctask = new MyAsyncTask(url_upLoad, mPhone + "3.jpg",image5);
        if (!image5.equals("")) {
            asynctask.execute("人卡合影上传");
        }
        asynctask = new MyAsyncTask(url_upLoad, mPhone + "6.jpg",image6);
        if (!image6.equals("")) {
            asynctask.execute("我的商铺上传");
        }
        if(!image1.equals("")&&!image2.equals("")&&!image3.equals("")&&!image4.equals("")&&!image5.equals("")){
            getUpLoad();
        }
    }

    private void getUpLoad() {
        DownHTTP.getVolley("http://chinazbjt.cn/app/photo_chuan.aspx?type=upload&username="+mPhone, new VolleyResultListener() {
            @Override
            public void onErrorResponse(VolleyError arg0) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onResponse(String arg0) {
                // TODO Auto-generated method stub
                sendMessage();
            }
            private void sendMessage() {
                hashMap_send.clear();
                hashMap_send.put("appName", "积付宝花呗");
                hashMap_send.put("username", mPhone);
                DownHTTP.postVolley("http://chinazbjt.cn:8080/jifubaohuabei/app/system/send-sms.app", hashMap_send, new VolleyResultListener() {
                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        // TODO Auto-generated method stub
                    }
                    @Override
                    public void onResponse(String arg0) {
                        // TODO Auto-generated method stub
                        Log.e("短信发送成功了吗？", arg0);
                    }
                });
            }
        });
    }
    /** 读取SD卡中的图片并且设置图片控件的图片 */
    private void img_red(String path2, ImageView imageview, int imgview) {
        // 设置图片在SD卡目录
        File file = new File(path2);// 读取目录图片
        if (file.exists()) {// 判断是否找到图片
            Bitmap bm = BitmapFactory.decodeFile(path2);// 将目录的图片转换成bitmap类型
            // imageview.setImageBitmap(bm);//设置前景图片
            switch (imgview) {
                case R.id.imageView_img1:
                    image1 = path2;
                    SD_img("", path2, mID_front, bm);// 防止溢出,导致程序直接死掉,必须用到的方法
                    break;
                case R.id.imageView_img2:
                    image2 = path2;
                    SD_img("", path2, mID_behind, bm);// 防止溢出,导致程序直接死掉,必须用到的方法
                    break;
                case R.id.imageView_img3:
                    image3 = path2;
                    SD_img("", path2, mBankCard_front, bm);// 防止溢出,导致程序直接死掉,必须用到的方法
                    break;
                case R.id.imageView_img4:
                    image4 = path2;
                    SD_img("", path2, mBankCard_behind, bm);// 防止溢出,导致程序直接死掉,必须用到的方法
                    break;
                case R.id.imageView_img5:
                    image5 = path2;
                    SD_img("", path2, mGroupPhoto, bm);// 防止溢出,导致程序直接死掉,必须用到的方法
                    break;
                case R.id.imageView_img6:
                    image6 = path2;
                    SD_img("", path2, mMyStore, bm);// 防止溢出,导致程序直接死掉,必须用到的方法
                    break;
                default:
                    break;
            }
        }
    }
    /**
     * 图片上传 获取方式
     * @param imgID
     * @param title
     */
    private void getImage(final int imgID, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ID_PhotoActivity.this);
        builder.setTitle(title);
        builder.setMessage("请选择图片上传方式");
        //拍照方式
        builder.setPositiveButton("拍照",new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(//通过意图对象跳转到手机自带的拍照功能
                        "android.media.action.IMAGE_CAPTURE");
                String photoPath = "/sdcard/audit/";//开辟图片存放的文件夹
                String photoName = "";//图片名称
                /**
                 * 手机拍照后图片保存的路径
                 * getRootDirectory():是手机内存目录 ; getExternalStorageDirectory():是内存卡目录;
                 *  Context.getFilesDir():本app安装目录;
                 */
                Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "suibianxie.jpg"));
                photoName=photoPath + mPhone + "img"+imgID+".jpg";//照片保存的名称
                File file = new File(photoPath);
                if (!file.exists()) { // 检查图片存放的文件夹是否存在
                    file.mkdir(); // 不存在的话 创建文件夹
                }
                File photo = new File(photoName);
                imageUri = Uri.fromFile(photo);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri); //将文件的存储方式和uri指定到了Camera应用中
                startActivityForResult(intent, 0);//拍照完后返回之前Activity,并返回了一结果码
            }
        });
        //从本机相册获取
        builder.setNegativeButton("相册", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
				/* 开启Pictures画面Type设定为image */
                intent.setType("image/*");
				/* 使用Intent.ACTION_GET_CONTENT这个Action */
                intent.setAction(Intent.ACTION_GET_CONTENT);
				/* 取得相片后返回本画面 ,并返回了一结果码*/
                startActivityForResult(intent, 1);
            }
        });
        builder.create().show();
    }
    /**
     * 防止溢出,导致程序直接死掉,必须用到的方法 获取图片设置图片大小然后存储到手机的SD卡中 photoPath SD卡中要保存的图片路径
     * photoName 要保存的图片名字 image目标图片控件 bitMap2 获取得到的 Bitmap类型的图片
     * */
    @SuppressWarnings({ "deprecation", "unused" })
    @SuppressLint("NewApi")
    private void SD_img(String photoPath, String photoName,
                        ImageView image, Bitmap bitMap2) {

        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
            Log.i("内存卡错误==", "请检查您的内存卡");
        } else {

            File file = new File(photoPath);
            if (!file.exists()) { // 检查图片存放的文件夹是否存在
                file.mkdir(); // 不存在的话 创建文件夹
            }

            BitmapFactory.Options op = new BitmapFactory.Options();
            // 设置图片的大小
            // Bitmap bitMap = BitmapFactory.decodeFile(photoName);
            Bitmap bitMap = bitMap2;
            int width = bitMap.getWidth();
            int height = bitMap.getHeight();
            // 设置想要的大小
            int newWidth = 480;
            int newHeight = 640;
            // 计算缩放比例
            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;
            // 取得想要缩放的matrix参数
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            // 得到新的图片
            bitMap = Bitmap.createBitmap(bitMap, 0, 0, width, height, matrix,
                    true);
            // canvas.drawBitmap(bitMap, 0, 0, paint)
            // 防止内存溢出
            op.inSampleSize = 5; // 这个数字越大,图片大小越小.
            Bitmap pic = null;
            pic = BitmapFactory.decodeFile(photoName, op);
            Drawable drawable = new BitmapDrawable(bitMap);
            image.setBackground(drawable); // 这个ImageView是拍照完成后显示图片
            // -----------以下是存储图片到SD卡中------------------
            try {
                FileOutputStream out = new FileOutputStream(photoName);
                bitMap.compress(Bitmap.CompressFormat.JPEG, 70, out);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 异步加载_自定义类名继承AsyncTask异步加载类 参数1 参数1设置的是什么 doInBackground方法参数 就设置什么
     * 设置格式:参数1 + ... params 参数2 参数2设置的是什么 onProgressUpdate方法参数 就设置什么 设置格式:参数2 +
     * ... values 参数3 决定了 重写doInBackground方法的返回值 假设参数3是 String 那么 protected
     * String doInBackground -------------以下是 构造方法参数的说明------------------
     * 上传文件至Server的方法 actionUrl 上传网址【注意写法】 newName保存的图片名字 uploadFile
     * 上传的图片所在的SD卡中所在目录
     * */
    class MyAsyncTask extends AsyncTask<String, Integer, String> {
        String url_upLoad;
        String newName;
        String uploadFile;
        boolean b=true;
        public MyAsyncTask(String url_upLoad, String newName, String uploadFile) {
            this.url_upLoad = url_upLoad;
            this.newName = newName;
            this.uploadFile = uploadFile;
        }
        @Override
        // 异步加载一运行就先执行这里
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            // pro_jindutiao.setVisibility(View.VISIBLE);//设置显示该控件
        }
        @Override
        // 【注意】这里的参数 String... params 将获取的是 .execute("例如传String类型的值")所传来的值
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            // 注意页面对象类继承的是 Fragment所以getActivity().runOnUiThread 如果继承的是Activity
            // 直接runOnUiThread
            runOnUiThread(new Runnable() {// 这个方法是将子线程抛向主线程执行
                @Override
                public void run() {
                    // 更新控件不能再子线程 只能在主线程(想想如果此时不放在runOnUiThread()中运行会报什么错？)
                    mLoading.setVisibility(View.VISIBLE);
                    startAnim();
                }
            });
            // ===============================此时就是异步上传了==========================
            System.out.print("正在发送请求！");
            String end = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            try {
                URL url = new URL(url_upLoad);
                HttpURLConnection con = (HttpURLConnection) url
                        .openConnection();
				/* 允许Input、Output，不使用Cache */
                con.setDoInput(true);
                con.setDoOutput(true);
                con.setUseCaches(false);
				/* 设置传送的method=POST */
                con.setRequestMethod("POST");
				/* setRequestProperty */
                con.setRequestProperty("Connection", "Keep-Alive");
                con.setRequestProperty("Charset", "UTF-8");
                con.setRequestProperty("Content-Type",
                        "multipart/form-data;boundary=" + boundary);
				/* 设置DataOutputStream */
                DataOutputStream ds = new DataOutputStream(
                        con.getOutputStream());
                ds.writeBytes(twoHyphens + boundary + end);
                ds.writeBytes("Content-Disposition: form-data; "
                        + "name=\"file1\";filename=\"" + newName + "\"" + end);
                ds.writeBytes(end);
				/* 取得文件的FileInputStream */
                FileInputStream fStream = new FileInputStream(uploadFile);
				/* 设置每次写入1024bytes */
                System.out.print("已经找到数据正在发送！");
                int bufferSize = 1024 * 10;
                byte[] buffer = new byte[bufferSize];
                int length = -1;
				/* 从文件读取数据至缓冲区 */
                while ((length = fStream.read(buffer)) != -1) {
					/* 将资料写入DataOutputStream中 */
                    ds.write(buffer, 0, length);
                }
                ds.writeBytes(end);
                ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
				/* close streams */
                fStream.close();
                ds.flush();
				/* 取得Response内容 */
                InputStream is = con.getInputStream();
                int ch;
                StringBuffer b = new StringBuffer();
                while ((ch = is.read()) != -1) {
                    b.append((char) ch);
                }
				/* 将Response显示于Dialog */
                // showDialog("上传成功");
				/* 关闭DataOutputStream */
                ds.close();
            } catch (Exception e) {
//				 showDialog("上传失败");
                b=false;
                e.printStackTrace();
                stopAnim();
            }
            // ===============================此时就是异步上传了==========================
            String str ="";
            if(b==true){
                str= params[0];
            }else{
                str="网络异常，"+params[0]+"失败！";
            }
            return str;
        }
        @Override
        // 接收publishProgress()方法传来的值 只要有值被传过来 这里就一定会自动执行
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
            int i = values[0];
            // text_tishi.setText("已经下载"+i+"%");
            // pro_jindutiao.setProgress(i);
        }
        @Override
        // 参数 String result 就是默认获取 重写doInBackground方法执行完成后 的String的返回值
        // 重写doInBackground方法的返回值如果改变了 这里的参数也要跟着改变
        protected void onPostExecute(final String result) {
            // text_tishi.setText(result);//赋值给 控件
            // 注意页面对象类继承的是 Fragment所以getActivity().runOnUiThread 如果继承的是Activity
            // 直接runOnUiThread
            runOnUiThread(new Runnable() {// 这个方法是将子线程抛向主线程执行
                @Override
                public void run() {
                    // 更新控件不能再子线程 只能在主线程(想想如果此时不放在runOnUiThread()中运行会报什么错？)
                    Toast.makeText(ID_PhotoActivity.this, result, 0).show();
                    stopAnim();
                }
            });
            super.onPostExecute(result);
        }
    }
    /**
     * 启动桢动画
     */
    private void startAnim() {
        mDrawable = (AnimationDrawable) mImage_load.getDrawable();
        mDrawable.start();//启动动画
    }
    private void stopAnim() {
        mDrawable.stop();
        mLoading.setVisibility(View.INVISIBLE);
    }
}
