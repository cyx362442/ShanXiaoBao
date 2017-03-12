package com.zhongbang.sxb.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhongbang.sxb.LandActivity;
import com.zhongbang.sxb.R;
import com.zhongbang.sxb.bean.Users;

/**
 * A simple {@link Fragment} subclass.
 */
public class CenterFragment extends Fragment implements View.OnClickListener {

    private Intent mIntent;

    public CenterFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_center, container, false);
        initClick(inflate);
        return inflate;
    }
    private void initClick(View inflate) {
        inflate.findViewById(R.id.ll_01).setOnClickListener(this);
        inflate.findViewById(R.id.ll_02).setOnClickListener(this);
        inflate.findViewById(R.id.ll_03).setOnClickListener(this);
        inflate.findViewById(R.id.ll_04).setOnClickListener(this);
        inflate.findViewById(R.id.ll_05).setOnClickListener(this);
        inflate.findViewById(R.id.ll_06).setOnClickListener(this);
        inflate.findViewById(R.id.ll_07).setOnClickListener(this);
        inflate.findViewById(R.id.ll_08).setOnClickListener(this);
        inflate.findViewById(R.id.ll_09).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_01:
                isLand();
                break;
            case R.id.ll_02:
                isLand();
                break;
            case R.id.ll_03:
                isLand();
                break;
            case R.id.ll_04:
                isLand();
                break;
            case R.id.ll_05:
                isLand();
                break;
            case R.id.ll_06:
                isLand();
                break;
            case R.id.ll_07:
                isLand();
                break;
        }
    }

    private void isLand() {
        if(Users.load==false){
            mIntent = new Intent(getActivity(), LandActivity.class);
            startActivity(mIntent);
            return;
        }
    }
}
