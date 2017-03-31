package com.zhongbang.sxb;

import android.content.Intent;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zhongbang.sxb.application.ExitAppliation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GuideActivity extends AppCompatActivity {
    private int[]imgRes={R.mipmap.guide1,R.mipmap.guide2};
    private ArrayList<View>imgList=new ArrayList<>();
    private List<ImageView> imageViews; // 滑动的图片集合
    private ScheduledExecutorService scheduledExecutorService;
    private ViewPager mViewpager;
    private int currentItem = 0; // 当前图片的索引号
    private int[] imageResId; // 图片ID
    // 切换当前显示的图片
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            mViewpager.setCurrentItem(currentItem);// 切换当前显示的图片
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ExitAppliation.getInstance().addActivity(this);
        imageResId = new int[] {R.mipmap.guide1,R.mipmap.guide2};
        imageViews = new ArrayList<ImageView>();

        // 初始化图片资源
        for (int i = 0; i < imageResId.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(imageResId[i]);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageViews.add(imageView);
        }
        mViewpager = (ViewPager) findViewById(R.id.viewpager);
        initViewPager();//初始化viewpager
        mViewpager.setAdapter(new MyAdapter());
        mViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == imgList.size() - 1) {
                    Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
            @Override
            public void onPageSelected(int position) {
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        // 当Activity显示出来后，每两秒钟切换一次图片显示
        scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(),3, 10, TimeUnit.SECONDS);
    }
    private class ScrollTask implements Runnable {
        public void run() {
            synchronized (mViewpager) {
                currentItem = (currentItem + 1) % imageViews.size();
                handler.obtainMessage().sendToTarget(); // 通过Handler切换图片
            }
        }
    }
    private void initViewPager() {
        for(int i=0;i<imgRes.length;i++){
            View inflate = getLayoutInflater().inflate(R.layout.grdviewpager_item, null);
            ImageView image = (ImageView) inflate.findViewById(R.id.imageView1);
            image.setImageResource(imgRes[i]);
            if(i==imgRes.length-1){//最后一张点击进入登录界面
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(GuideActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
            imgList.add(inflate);
        }
    }
    /**
     * 填充ViewPager页面的适配器
     *
     * @author Administrator
     *
     */
    private class MyAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return imageResId.length;
        }
        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(imageViews.get(arg1));
            return imageViews.get(arg1);
        }
        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView((View) arg2);
        }
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }
        @Override
        public Parcelable saveState() {
            return null;
        }
        @Override
        public void startUpdate(View arg0) {
        }
        @Override
        public void finishUpdate(View arg0) {
        }
    }
}
