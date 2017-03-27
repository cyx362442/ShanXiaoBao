package com.zhongbang.sxb.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhongbang.sxb.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocationFragment extends Fragment {
    public LocationFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_location, container, false);
        return inflate;
    }
}
