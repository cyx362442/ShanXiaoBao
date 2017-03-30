package com.zhongbang.sxb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zhongbang.sxb.R;
import com.zhongbang.sxb.bean.CityZone;

import java.util.zip.Inflater;

/**
 * Created by Administrator on 2017-03-28.
 */

public class ListAdapter extends BaseAdapter {
    Context context;
    CityZone[]zones;
    int[]imgId={R.mipmap.icon_zone1,R.mipmap.icon_zone2,R.mipmap.icon_zone3};
    public ListAdapter(Context context, CityZone[] zones) {
        this.context = context;
        this.zones = zones;
    }

    @Override
    public int getCount() {
        return zones.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHold hold=null;
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, null);
            hold=new ViewHold();
            hold.image= (ImageView) convertView.findViewById(R.id.img_zone);
            hold.text= (TextView) convertView.findViewById(R.id.tv_zone);
            convertView.setTag(hold);
        }else{
            hold= (ViewHold) convertView.getTag();
        }
//        hold.image.setImageResource(imgId[position%imgId.length]);
        Picasso.with(context).load(imgId[position%imgId.length]).into(hold.image);
        hold.text.setText(zones[position].getDistrict());
        return convertView;
    }
    class ViewHold{
        ImageView image;
        TextView text;
    }
}
