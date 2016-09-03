package com.itgrape.robot;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import bmobbean.MyUser;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class SetNickName extends Activity {

	EditText et_nickname;
	ProgressBar pb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.nickname_layout);

		et_nickname = (EditText) findViewById(R.id.et_nickname);
		pb = (ProgressBar) findViewById(R.id.pb);
	}

	public void submit(View view) {
		String nickname = et_nickname.getText().toString();
		if (TextUtils.isEmpty(nickname)) {
			Toast.makeText(SetNickName.this, "请先输入...", Toast.LENGTH_SHORT)
					.show();
		} else {

			pb.setVisibility(View.VISIBLE);

			MyUser newUser = new MyUser();// 实例化一个新的user类来承载用户信息变化
			MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
			newUser.setNickname(nickname);
			newUser.update(userInfo.getObjectId(), new UpdateListener() {
				@Override
				public void done(BmobException e) {
					pb.setVisibility(View.GONE);
					if (e == null) {
						Toast.makeText(SetNickName.this, "昵称修改成功",
								Toast.LENGTH_SHORT).show();
						finish();
					} else {
						Toast.makeText(SetNickName.this, "昵称修改失败",
								Toast.LENGTH_SHORT).show();
					}
				}
			});

		}
	}
}
