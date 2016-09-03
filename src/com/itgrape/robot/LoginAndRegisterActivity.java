package com.itgrape.robot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class LoginAndRegisterActivity extends Activity {

	Button btn_login, btn_register;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login_and_register);

		btn_login = (Button) findViewById(R.id.btn_login);
		btn_register = (Button) findViewById(R.id.btn_register);

		btn_login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginAndRegisterActivity.this,Login.class);
				startActivity(intent);
			}
		});
		btn_register.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginAndRegisterActivity.this,Register.class);
				startActivity(intent);
			}
		});
	}
}
