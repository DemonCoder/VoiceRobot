package com.itgrape.robot;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;
import bmobbean.MyUser;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class PhotoDialog extends Activity {

	public static final int TAKE_PHOTO = 1;
	public static final int CROP_PHOTO = 2;
	private Uri imageUri;
	private String username;
	private File outputImage;

	BmobFile bmobFile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_layout);
		MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
		username = userInfo.getUsername();

		outputImage = new File(Environment.getExternalStorageDirectory(),
				"outputImage" + username + ".jpg");

		bmobFile = new BmobFile(outputImage);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case TAKE_PHOTO:
			if (resultCode == RESULT_OK) {
				Intent intent = new Intent("com.android.camera.action.CROP");
				intent.setDataAndType(imageUri, "image/*");
				intent.putExtra("scale", true);
				intent.putExtra("aspectX", 1);// 裁剪框比例
				intent.putExtra("aspectY", 1);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, CROP_PHOTO); // 启动裁剪程序
			}
			break;
		case CROP_PHOTO:
			if (resultCode == RESULT_OK) {
				Toast.makeText(PhotoDialog.this, "头像正在更新，请稍后...", Toast.LENGTH_LONG)
						.show();
				bmobFile = new BmobFile(outputImage);
				bmobFile.uploadblock(new UploadFileListener() {
					@Override
					public void done(BmobException e) {
						if (e == null) {

							String portraitUrl = bmobFile.getFileUrl();
							MyUser newUser = new MyUser();// 实例化一个新的user类来承载用户信息变化
							MyUser userInfo = BmobUser
									.getCurrentUser(MyUser.class);
							newUser.setPortraitUrl(portraitUrl);
							newUser.update(userInfo.getObjectId(),
									new UpdateListener() {
										@Override
										public void done(BmobException e) {
											if (e == null) {
												Toast.makeText(
														PhotoDialog.this,
														"头像修改成功",
														Toast.LENGTH_SHORT)
														.show();
											} else {
												Toast.makeText(
														PhotoDialog.this,
														"头像修改失败",
														Toast.LENGTH_SHORT)
														.show();
											}
										}
									});
						} else {
							Toast.makeText(PhotoDialog.this, "上传头像失败",
									Toast.LENGTH_SHORT).show();
						}

					}
				});

			}
			finish();
			break;
		default:
			break;
		}
	}

	public void takePic(View view) {
		try {
			if (outputImage.exists()) {
				outputImage.delete();
			}
			outputImage.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		imageUri = Uri.fromFile(outputImage);
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(intent, TAKE_PHOTO); // 启动相机程序
	}

	public void getPic(View view) {
		try {
			if (outputImage.exists()) {
				outputImage.delete();
			}
			outputImage.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		imageUri = Uri.fromFile(outputImage);
		Intent intent = new Intent("android.intent.action.GET_CONTENT");
		intent.setType("image/*");
		intent.putExtra("crop", true);
		intent.putExtra("scale", true);
		intent.putExtra("aspectX", 1);// 裁剪框比例
		intent.putExtra("aspectY", 1);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(intent, CROP_PHOTO);
	}
}
