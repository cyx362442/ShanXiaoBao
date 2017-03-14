package com.zhongbang.sxb.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zhongbang.sxb.R;
import com.zhongbang.sxb.adapter.Sliding;

/**
 * A simple {@link Fragment} subclass.
 */
public class SlidingMain extends Fragment {
    /**ViewPager控件对象 [成员变量]注意引包import android.support.v4.view.ViewPager*/
    private ViewPager pager;
    /**将所有的图片控件都添加到数组里面[成员变量]*/
    private int[] image = new int[]{R.id.imageView_xia_tu1,
            R.id.imageView_xia_tu2,R.id.imageView_xia_tu3,R.id.imageView_xia_tu4
            ,R.id.imageView_xia_tu5,R.id.imageView_xia_tu6};
    private String[]imgUrl={
            "http://chinazbhf.com:8081/sxb/attached/rotation/1.jpg",
            "http://chinazbhf.com:8081/sxb/attached/rotation/2.jpg",
            "http://chinazbhf.com:8081/sxb/attached/rotation/3.jpg",
            "http://chinazbhf.com:8081/sxb/attached/rotation/4.jpg",
            "http://chinazbhf.com:8081/sxb/attached/rotation/5.jpg",
            "http://chinazbhf.com:8081/sxb/attached/rotation/6.jpg"
            };
    /**获取布局对象*/
    private View view;
    /**线程对象*/
    private Handler mHandler;
    /**用来判断是否用户在拖拽*/
    private boolean isScroll;
    /**设置线程*/
    private Runnable Runnable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        mHandler=new Handler();
        //设置要得到的布局对象/这里的R.layout.guanggao指的就是你步骤一新建的布局对象xml名
        view = inflater.inflate(R.layout.sliding_main, null);
        pager = (ViewPager) view.findViewById(R.id.guanggao_viewpager);//实例化ViewPager控件

        //起始位置
        pager.setCurrentItem(100000 / 2);

        for (int i = 0; i < image.length; i++) {
            ImageView image_tu = (ImageView) view.findViewById(image[i]);//实例化所有控件对象
            if(i==0){
                image_tu.setBackgroundResource(R.mipmap.sliding_b);//设置背景图片
            }

        }

        //设置Viewpager缓存的个数，左右各缓存2个,当页面左右滑动同时可以显示两个页面的部分显示
        pager.setOffscreenPageLimit(2);

        //得到Fragment的管理器
        //getChildFragmentManager当前片段获取子片段的片段的片段片段管理器
        //【注意引用最新的v4包】
        FragmentManager fm = getChildFragmentManager();
        //设置适配器
        pager.setAdapter(new MyAdapter(fm));

        //ViewPager控件界面更新页面事件监听
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                // TODO Auto-generated method stub

                arg0 %= image.length;
                //通过循环判断操作控件对象
                for (int i = 0; i < image.length; i++) {
                    ImageView image_tu = (ImageView) view.findViewById(image[i]);//实例化控件对象
                    if(arg0==i){
                        image_tu.setBackgroundResource(R.mipmap.sliding_b);//设置背景图片

                    }else{
                        image_tu.setBackgroundResource(R.mipmap.sliding_a);//设置背景图片
                    }

                }
            }

            @Override
            /**当页面在滑动的时候会调用此方法，在滑动被停止之前，此方法回一直调用
             * arg0 :当前页面，及你点击滑动的页面
             * arg1:当前页面偏移的百分比
             * arg2:当前页面偏移的像素位置
             * */
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            //此方法是在状态改变的时候调用，其中arg0这个参数
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub
                switch (arg0) {
                    case ViewPager.SCROLL_STATE_DRAGGING://判断用户拖拽的时候执行
                        //自动滚动暂停
                        isScroll = true;

                        //设置是否拦截该ViewPager里面的控件【例如里面的控件是否可点击】
                        //当ViewPager控件没有执行该事件的时候会自动回复初始状态
                        //ViewPager初始状态决定于onInterceptTouchEvent所返回的boolean类型的值
                        //我们可以通过继承ViewPager自定义控件重写onInterceptTouchEvent来设置它最好是return false
                        pager.requestDisallowInterceptTouchEvent(true);

                        break;
                    case ViewPager.SCROLL_STATE_IDLE://判断自动滚到的时候执行
                        //自动滚动继续开始
                        isScroll = false;

                        //设置是否拦截该ViewPager里面的控件【例如里面的控件是否可点击】
                        //当ViewPager控件没有执行该事件的时候会自动回复初始状态
                        //ViewPager初始状态决定于onInterceptTouchEvent所返回的boolean类型的值
                        //我们可以通过继承ViewPager自定义控件重写onInterceptTouchEvent来设置它最好是return false
                        pager.requestDisallowInterceptTouchEvent(true);

                        break;
                    case ViewPager.SCROLL_STATE_SETTLING://加载完毕后就执行

                        //设置是否拦截该ViewPager里面的控件【例如里面的控件是否可点击】
                        //当ViewPager控件没有执行该事件的时候会自动回复初始状态
                        //ViewPager初始状态决定于onInterceptTouchEvent所返回的boolean类型的值
                        //我们可以通过继承ViewPager自定义控件重写onInterceptTouchEvent来设置它最好是return false
                        pager.requestDisallowInterceptTouchEvent(true);
                        break;
                    default:
                        break;
                }
            }
        });
        xiancheng_guanggao();//【该方法决定自动侧滑线程,不自动侧滑改成手动侧滑该方法注释或者删除掉即可】
        return view;
    }
    @Override//Fragment对象清理view资源时调用，也就是移除fragment中的视图
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();
        // 停止postDelayed
        mHandler.removeCallbacks(Runnable);
    }

    @Override//Fragment对象完成对象清理View资源时调用
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    public void xiancheng_guanggao(){
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                //当前ViewPager的索引ID
                if(!isScroll)
                {
                    int currentItem = pager.getCurrentItem();//获取当前是第几个页面
                    pager.setCurrentItem(currentItem + 1);//自动滑动到下一个页面
                }

                mHandler.postDelayed(this, 4000);
            }
        }, 1000);

    }


    /**自定义类继承了 FragmentPagerAdapter适配器*/
    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
            // TODO Auto-generated constructor stub

        }

        @Override//参数1:获取列的排序数
        public Fragment getItem(int arg0) {
            // TODO Auto-generated method stub
            //【【【注意这里决定了 显示的是什么 页面类对象 注意 页面类对象必须继承了 extends Fragment】】】
            //我们可以通过 获取 int arg0  然后 判断 来进行 显示 页面类对象

            arg0 %= image.length;
            Sliding bujuguanggaotu = new Sliding();
            bujuguanggaotu.setImagetu(imgUrl[arg0]);
            return bujuguanggaotu;//返回指定判断局部刷新控件并且显示出来
//			return null;
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return 100000;//设置有多少列
        }
    }
}
