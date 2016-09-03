package com.itgrape.robot;

import bmobbean.MyUser;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class SetSignature extends Activity {

	EditText et_signature;
	ProgressBar pb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.signature_layout);

		et_signature = (EditText) findViewById(R.id.et_signature);
		pb = (ProgressBar) findViewById(R.id.pb);
	}

	public void submit(View view) {
		
		String signature = et_signature.getText().toString();
		if (TextUtils.isEmpty(signature)) {
			Toast.makeText(SetSignature.this, "先说点什么吧...", Toast.LENGTH_SHORT)
					.show();
		} else {
			
			pb.setVisibility(View.VISIBLE);
			
			MyUser newUser = new MyUser();// 实例化一个新的user类来承载用户信息变化
			MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
			newUser.setSignature(signature);
			newUser.update(userInfo.getObjectId(), new UpdateListener() {
				@Override
				public void done(BmobException e) {
					
					pb.setVisibility(View.GONE);
					
					if (e == null) {
						Toast.makeText(SetSignature.this, "签名修改成功",
								Toast.LENGTH_SHORT).show();
						finish();
					} else {
						Toast.makeText(SetSignature.this, "签名修改失败",
								Toast.LENGTH_SHORT).show();
					}
				}
			});
		}
	}
}
