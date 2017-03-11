package com.zhongbang.sxb.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zhongbang.sxb.R;

public class Sliding extends Fragment{
	private ImageView image_guanggaotu;
	private int tu;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.layout_sliding, null);
		image_guanggaotu = (ImageView) view.findViewById(R.id.imageView_sliding);
		image_guanggaotu.setImageResource(tu);
		return view;
	}
	public void setImagetu(int tu){
		this.tu = tu;
	}
}
