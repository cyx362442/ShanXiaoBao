package com.zhongbang.sxb.managercenter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zhongbang.sxb.R;
import com.zhongbang.sxb.application.ExitAppliation;

public class ToBalanceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_balance);
        ExitAppliation.getInstance().addActivity(this);
        TextView text1 = (TextView) findViewById(R.id.text_1);
        TextView text2 = (TextView) findViewById(R.id.text_2);
        String st1 = getIntent().getStringExtra("V1");
        String st2 = getIntent().getStringExtra("V2");
        text1.setText(st1);
        text2.setText(st2);
        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();

            }
        });
        findViewById(R.id.imageView1).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
    }
}
