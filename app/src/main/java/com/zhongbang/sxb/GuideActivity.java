package com.zhongbang.sxb;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

public class GuideActivity extends AppCompatActivity {
    private int[]imgRes={R.mipmap.guide1,R.mipmap.guide2};
    private ArrayList<View>imgList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ViewPager grvViewpager = (ViewPager) findViewById(R.id.viewpager);
        initViewPager();//初始化viewpager
        grvViewpager.setAdapter(new MyPagerAdapter());
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
    class MyPagerAdapter extends PagerAdapter {
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub
//			return super.instantiateItem(container, position);
            View view = imgList.get(position);
            container.addView(view);
            return view;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub
//			super.destroyItem(container, position, object);
            container.removeView(imgList.get(position));
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return imgList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
//			return false;
            return arg0==arg1;
        }
    }
}
