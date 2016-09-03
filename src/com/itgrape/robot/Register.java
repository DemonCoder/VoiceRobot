package com.itgrape.robot;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import bmobbean.MyUser;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends Activity {

	EditText et_user, et_password;
	Button btn_reg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register);

		et_user = (EditText) findViewById(R.id.et_user_reg);
		et_password = (EditText) findViewById(R.id.et_password_reg);
		btn_reg = (Button) findViewById(R.id.btn_reg);
	}

	public void register(View view) {
		String username = et_user.getText().toString();
		String password = et_password.getText().toString();

		MyUser mUser = new MyUser();
		mUser.setUsername(username);
		mUser.setPassword(password);
		mUser.setGender("女");
		mUser.setNickname("小白");
		mUser.setSignature("这家伙很懒，什么也不说...");
		mUser.setPortraitUrl("");
		mUser.setVip(false);
		mUser.signUp(new SaveListener<MyUser>() {

			@Override
			public void done(MyUser s, BmobException e) {
				if (e == null) {
					Toast.makeText(Register.this, "注册成功:" + s.toString(),
							Toast.LENGTH_SHORT).show();
					BmobUser.logOut();
				} else {
					Toast.makeText(Register.this, e.toString(),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		Intent intent = new Intent(Register.this,MainActivity.class);
		startActivity(intent);
	}
}
