package com.itgrape.robot;

import utils.PrefUtils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import bmobbean.MyUser;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class Login extends Activity {

	EditText et_user, et_password;
	Button btn_log;
	ProgressBar pb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);

		et_user = (EditText) findViewById(R.id.et_user);
		et_password = (EditText) findViewById(R.id.et_password);
		btn_log = (Button) findViewById(R.id.btn_log);
		pb = (ProgressBar) findViewById(R.id.pb);
	}

	public void login(View view) {

		pb.setVisibility(View.VISIBLE);
		String username = et_user.getText().toString();
		String password = et_password.getText().toString();

		MyUser mUser = new MyUser();
		mUser.setUsername(username);
		mUser.setPassword(password);
		mUser.login(new SaveListener<MyUser>() {

			@Override
			public void done(MyUser myUser, BmobException e) {
				pb.setVisibility(View.GONE);
				if (e == null) {
					Toast.makeText(Login.this, "登陆成功", Toast.LENGTH_SHORT)
							.show();
					Intent intent = new Intent(Login.this,MainActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(Login.this, "登陆失败", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
	}
}
