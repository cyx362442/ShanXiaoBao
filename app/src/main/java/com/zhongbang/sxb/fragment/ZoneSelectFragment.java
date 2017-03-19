package com.zhongbang.sxb.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zhongbang.sxb.R;
import com.zhongbang.sxb.webview.Link2Activity;
import com.zhongbang.sxb.webview.WebViewActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ZoneSelectFragment extends Fragment {
    @Bind(R.id.img_huli)
    ImageView mImgHuli;
    @Bind(R.id.img_siming)
    ImageView mImgSiming;
    @Bind(R.id.img_jimei)
    ImageView mImgJimei;

    public ZoneSelectFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_zone_select, container, false);
        ButterKnife.bind(this, inflate);
        return inflate;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.img_huli, R.id.img_siming, R.id.img_jimei})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_huli:
                toWebView("湖里区");
                break;
            case R.id.img_siming:
                toWebView("思明区");
                break;
            case R.id.img_jimei:
                toWebView("集美区");
                break;
        }
    }

    private void toWebView(String contents) {
        Intent intent = new Intent(getActivity(), Link2Activity.class);
        intent.putExtra("title",contents);
        startActivity(intent);
    }
}
