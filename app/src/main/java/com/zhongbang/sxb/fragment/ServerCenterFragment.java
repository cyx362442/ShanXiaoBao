package com.zhongbang.sxb.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhongbang.sxb.R;
import com.zhongbang.sxb.colleciton.CollectionsActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServerCenterFragment extends Fragment implements View.OnClickListener {
    public ServerCenterFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_server_center, container, false);
        inflate.findViewById(R.id.img_shoukuan).setOnClickListener(this);
        return inflate;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_shoukuan:
                Intent intent = new Intent(getActivity(), CollectionsActivity.class);
                startActivity(intent);
                break;
        }
    }
}
