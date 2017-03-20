package com.zhongbang.sxb.managercenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.zhongbang.sxb.HelperActivity;
import com.zhongbang.sxb.R;
import com.zhongbang.sxb.httputils.DownHTTP;
import com.zhongbang.sxb.httputils.VolleyResultListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyManagerActivity extends AppCompatActivity {
    @Bind(R.id.imageView_return)
    ImageView mImageViewReturn;
    @Bind(R.id.textView_title)
    TextView mTextViewTitle;
    @Bind(R.id.relativeLayout1)
    RelativeLayout mRelativeLayout1;
    @Bind(R.id.relativeLayout2)
    RelativeLayout mRelativeLayout2;
    @Bind(R.id.tv1)
    TextView mTv1;
    @Bind(R.id.tv2)
    TextView mTv2;
    @Bind(R.id.tv3)
    TextView mTv3;
    @Bind(R.id.tv4)
    TextView mTv4;
    @Bind(R.id.tv5)
    TextView mTv5;
    @Bind(R.id.linearLayout1)
    LinearLayout mLinearLayout1;
    @Bind(R.id.listView_bill)
    ListView mListViewBill;
    @Bind(R.id.image_load)
    ImageView mImageLoad;
    @Bind(R.id.textView1)
    TextView mTextView1;
    @Bind(R.id.rl_load)
    RelativeLayout mRlLoad;

    private AnimationDrawable mDrawable;
    private RelativeLayout mRel_loading;
    private ImageView mImg_load;
    private String urlPost = " http://chinazbhf.com:8081/SHXB/POST";
    //    private String urlPost="http://chinazbjt.cn:8080/JFBHB/POST";
    String pager_num = "0";
    private HashMap<String, String> HashMap_post = new HashMap<String, String>();
    private ArrayList<Zhangdan> zhangdan_hold = new ArrayList<Zhangdan>();
    private LVAdapter lvAdapter;
    private String username;
    private int num = 1;
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_manager);
        ButterKnife.bind(this);
        SharedPreferences sp = getSharedPreferences("users", Context.MODE_PRIVATE);
        username = sp.getString("name", "");

        mImg_load = (ImageView) findViewById(R.id.image_load);
        mRel_loading = (RelativeLayout) findViewById(R.id.rl_load);
        lvAdapter = new LVAdapter();
        mListViewBill.setAdapter(lvAdapter);
        mListViewBill.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = new Intent(MyManagerActivity.this, TxDetailActivity.class);
                intent.putExtra("dingdanhao", zhangdan_hold.get(arg2).Id);
                startActivity(intent);
            }
        });
    }

    public void onStart() {
        super.onStart();
        HashMap_post.clear();// 清空
        inntdata();
    }

    @OnClick({R.id.imageView_return, R.id.tv1, R.id.tv2, R.id.tv3, R.id.tv4, R.id.tv5})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView_return:
                finish();
                break;
            case R.id.tv1:
                break;
            case R.id.tv2:
                break;
            case R.id.tv3:
                mIntent = new Intent(this, MyEarnings.class);
                startActivity(mIntent);
                break;
            case R.id.tv4:
                mIntent = new Intent(this, MyEarnings.class);
                startActivity(mIntent);
                break;
            case R.id.tv5://帮助
                mIntent=new Intent(this, HelperActivity.class);
                startActivity(mIntent);
                break;
        }
    }

    class Zhangdan {
        String total_number2;
        String page_number;
        String order_number;
        String type;
        String real_otain;
        String time1;
        String state;
        String Id;
    }

    class LVAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return zhangdan_hold.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View inflate = getLayoutInflater().inflate(R.layout.relative_bill_lv, null);
            TextView tixian = (TextView) inflate.findViewById(R.id.tixian);
            TextView jine = (TextView) inflate.findViewById(R.id.jine);
            TextView time = (TextView) inflate.findViewById(R.id.time);
            TextView stata = (TextView) inflate.findViewById(R.id.stata);
            tixian.setText(zhangdan_hold.get(position).type);
            jine.setText(zhangdan_hold.get(position).real_otain);
            time.setText(zhangdan_hold.get(position).time1);
            stata.setText(zhangdan_hold.get(position).state);
            if (position == num * 10 - 1) {
                num++;
                inntdata();
            }
            return inflate;
        }
    }

    void inntdata() {
        zhangdan_hold.clear();//初始化之前记得清空容器
        HashMap_post.put("type_all", "wdzd");
        HashMap_post.put("num", "10");
        HashMap_post.put("pag", "" + num);
        HashMap_post.put("username", username);// 此时这里就是要传的网页上对应的参数name的值
        HashMap_post.put("type", pager_num);// 此时这里就是要传的网页上对应的参数String的值
        http();
    }

    private void http() {
        /* 使用POST请求 */
        startAnim();
        DownHTTP.postVolley(urlPost, HashMap_post, new VolleyResultListener() {
            @Override
            public void onErrorResponse(VolleyError arg0) {
                stopAmin();
                Toast.makeText(MyManagerActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String arg0) {
                // TsUtils.show(getActivity().getApplicationContext(), arg0);//
                // 用这个方法弹出提示
                if (TextUtils.isEmpty(arg0)) {
                    Toast.makeText(MyManagerActivity.this, "无数据", Toast.LENGTH_SHORT).show();
                    stopAmin();
                    return;
                }
                Zhangdan zhangdan;
                try {
                    JSONArray array = new JSONArray(arg0);
                    for (int i = 0; i < array.length(); i++) {
                        zhangdan = new Zhangdan();
                        zhangdan.total_number2 = array.getJSONObject(i)
                                .getString("total_number2").trim();
                        zhangdan.page_number = array.getJSONObject(i)
                                .getString("page_number").trim();
                        zhangdan.order_number = array.getJSONObject(i)
                                .getString("order_number").trim();
                        zhangdan.type = array.getJSONObject(i)
                                .getString("type").trim();
                        zhangdan.real_otain = array.getJSONObject(i)
                                .getString("real_otain").trim();
                        zhangdan.time1 = array.getJSONObject(i)
                                .getString("time1").trim();
                        zhangdan.state = array.getJSONObject(i)
                                .getString("state").trim();
                        zhangdan.Id = array.getJSONObject(i).getString("Id")
                                .trim();
                        zhangdan_hold.add(i + num * 10 - 10, zhangdan);
                    }
                    lvAdapter.notifyDataSetChanged();
                    stopAmin();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }

    private void startAnim() {
        mDrawable = (AnimationDrawable) mImg_load.getDrawable();
        mDrawable.start();
    }

    private void stopAmin() {
        mDrawable.stop();
        mRel_loading.setVisibility(View.GONE);
    }
}
