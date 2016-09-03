package com.itgrape.robot;

import java.io.File;

import com.lidroid.xutils.BitmapUtils;

import view.SwitchView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import bmobbean.MyUser;
import cn.bmob.v3.BmobUser;

public class UserDetail extends Activity {

	private SwitchView switchView;
	private TextView user, tv_nickname, tv_signature;
	private ImageView portrait;
	private ImageView vip;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_detail);

		MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
		userInfo.getUsername();
		userInfo.getNickname();
		userInfo.getSignature();
		userInfo.getGender();

		switchView = (SwitchView) findViewById(R.id.sv_gender);
		user = (TextView) findViewById(R.id.user);
		tv_nickname = (TextView) findViewById(R.id.tv_nickname);
		tv_signature = (TextView) findViewById(R.id.tv_signature);
		portrait = (ImageView) findViewById(R.id.portrait);
		vip = (ImageView) findViewById(R.id.vip);
	}

	@Override
	protected void onStart() {
		super.onStart();
		MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
		String username = userInfo.getUsername();
		String nickname = userInfo.getNickname();
		String signature = userInfo.getSignature();
		String gender = userInfo.getGender();
		String portraitUrl = userInfo.getPortraitUrl();
		Boolean isVip = userInfo.isVip();
		user.setText(username);
		tv_nickname.setText(nickname);
		tv_signature.setText(signature);
		if (gender.equals("女")) {
			switchView.setOpened(true);
		} else {
			switchView.setOpened(false);
		}

		BitmapUtils bitmapUtils = new BitmapUtils(UserDetail.this);
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.userlogin);
		bitmapUtils.configDefaultLoadingImage(R.drawable.userlogin);
		if (!TextUtils.isEmpty(portraitUrl)) {
			bitmapUtils.display(portrait, portraitUrl);
		}
		
		if (isVip) {
			vip.setImageResource(R.drawable.vip_on);
		}
	}

	public void portrait(View view) {
		Intent intent = new Intent(UserDetail.this, PhotoDialog.class);
		startActivity(intent);
	}

	public void nickname(View view) {
		Intent intent = new Intent(UserDetail.this, SetNickName.class);
		startActivity(intent);
	}

	public void signature(View view) {
		Intent intent = new Intent(UserDetail.this, SetSignature.class);
		startActivity(intent);
	}

	public void logout(View view) {
		new AlertDialog.Builder(UserDetail.this).setTitle("退出")
				.setMessage("真的退出当前账号吗？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						BmobUser.logOut();
						finish();
					}

				}).setNegativeButton("取消", null).show();
	}
}
