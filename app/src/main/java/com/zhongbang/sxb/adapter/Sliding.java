package com.zhongbang.sxb.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.zhongbang.sxb.R;

public class Sliding extends Fragment{
	private ImageView image_guanggaotu;
	private String url;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.layout_sliding, null);
		image_guanggaotu = (ImageView) view.findViewById(R.id.imageView_sliding);
		Picasso.with(getContext()).load(url).centerInside().fit().into(image_guanggaotu);
		return view;
	}
	public void setImagetu(String url){
		this.url = url;
	}
}
