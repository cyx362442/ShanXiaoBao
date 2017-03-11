package com.zhongbang.sxb.View;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
public class ChildViewPager extends ViewPager{
	/** 触摸时按下的点 **/
	PointF downP = new PointF();
	/** 触摸时当前的点 **/
	PointF curP = new PointF();
	OnSingleTouchListener onSingleTouchListener;

	public ChildViewPager(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}
	public ChildViewPager(Context context)
	{
		super(context);
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0)
	{
		// 当拦截触摸事件到达此位置的时候，返回true，false不拦截[这样可以使在ViewPager内部显示的控件可点击]
		// 说明将onTouch拦截在此控件，进而执行此控件的onTouchEvent
		return false;
	}
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		// 每次进行onTouch事件都记录当前的按下的坐标
		curP.x = event.getX();
		curP.y = event.getY();

		if(event.getAction() == MotionEvent.ACTION_DOWN)
		{
			//记录按下去的时候的坐标
			downP.x = event.getX();
			downP.y = event.getY();
			//通知父控件当前触摸处理是在这个空间里面处理的。
			getParent().requestDisallowInterceptTouchEvent(true);
		}
		if(event.getAction() == MotionEvent.ACTION_MOVE)
		{
			//通知父控件当前触摸处理是在这个空间里面处理的。
			getParent().requestDisallowInterceptTouchEvent(true);
		}
		if(event.getAction() == MotionEvent.ACTION_UP)
		{
			if(downP.x == curP.x && downP.y == curP.y)
			{
				//说明只是单击了
				onSingleTouch();
			}
		}
		return super.onTouchEvent(event);
	}
	private void onSingleTouch() {
		if(onSingleTouchListener != null)
		{
			onSingleTouchListener.onSingleTouch();
		}
	}
	public interface OnSingleTouchListener
	{
		void onSingleTouch();
	}
	public void setOnSingleTouchListener(OnSingleTouchListener onSingleTouchListener)
	{
		this.onSingleTouchListener = onSingleTouchListener;
	}
}
