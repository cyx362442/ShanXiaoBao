package com.zhongbang.sxb.colleciton;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.zhongbang.sxb.account.PersonalDataActivity;

public class Users {
	public static void setDialog(final Context context){
		new AlertDialog.Builder(context)
		   .setMessage("您的资料还未完善，无法进行支付，请先完善个人资料！")
		   .setNegativeButton("取消", null)
		   .setPositiveButton("确认", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context,PersonalDataActivity.class);
				context.startActivity(intent);
			}
		})
		  .create().show();
	}
}
