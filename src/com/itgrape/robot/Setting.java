package com.itgrape.robot;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import bean.SettingBean;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;

public class Setting extends Activity implements PlatformActionListener {

	private String settings[] = { "选择发音人", "检查更新", "意见反馈", "关于", "分享" };
	private ListView lv_setting;
	private ArrayList<SettingBean> settingList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setting);
		MyApplication.setSettingContext(this);

		settingList = new ArrayList<SettingBean>();
		SettingBean sb0 = new SettingBean(settings[0], R.drawable.speaker);
		SettingBean sb1 = new SettingBean(settings[1], R.drawable.update);
		SettingBean sb2 = new SettingBean(settings[2], R.drawable.message);
		SettingBean sb3 = new SettingBean(settings[3], R.drawable.about);
		SettingBean sb4 = new SettingBean(settings[4], R.drawable.share);

		settingList.add(sb0);
		settingList.add(sb1);
		settingList.add(sb2);
		settingList.add(sb3);
		settingList.add(sb4);

		lv_setting = (ListView) findViewById(R.id.lv_setting);
		SettingAdapter msSettingAdapter = new SettingAdapter();
		lv_setting.setAdapter(msSettingAdapter);
		lv_setting.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String settingText = settingList.get(position).settingText;
				if (settingText.equals(settings[0])) {
					Intent intent = new Intent(Setting.this,
							SelectSpeaker.class);
					startActivity(intent);

				} else if (settingText.equals(settings[1])) {
					new AlertDialog.Builder(Setting.this).setTitle("关于")
							.setMessage("当前是最新版本1.1")
							.setPositiveButton("是", null).show();
				} else if (settingText.equals(settings[2])) {
					ImageView imageView = new ImageView(Setting.this);
					imageView.setImageResource(R.drawable.myqq);
					new AlertDialog.Builder(Setting.this).setTitle("关于")
							.setView(imageView)
							.setMessage("有任何疑问请联系开发者葡萄皮，QQ:2275140495")
							.setPositiveButton("是", null).show();
				} else if (settingText.equals(settings[3])) {
					 ImageView imageView = new ImageView(Setting.this);
					 imageView.setImageResource(R.drawable.about_detail);
					 new AlertDialog.Builder(Setting.this)
					 .setTitle("关于")
					 .setView(imageView)
					 .setMessage("当前版本1.1，制作者：葡萄皮，仅用于相互交流，发现bug请与开发者联系。")
					 .setPositiveButton("是", null).show();
				} else if (settingText.equals(settings[4])) {
					showShare();
				}
			}
		});
	}

	class SettingAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return settingList.size();
		}

		@Override
		public SettingBean getItem(int position) {
			return settingList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(Setting.this, R.layout.setting_cell,
						null);
				holder = new ViewHolder();
				holder.iv_setting = (ImageView) convertView
						.findViewById(R.id.iv_setting);
				holder.tv_setting = (TextView) convertView
						.findViewById(R.id.tv_setting);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.iv_setting
					.setImageResource(settingList.get(position).settingImageId);
			holder.tv_setting.setText(settingList.get(position).settingText);
			return convertView;
		}
	}

	private void showShare() {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();

		// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle("分享");
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl("http://pan.baidu.com/s/1bo76KJt");
		// text是分享文本，所有平台都需要这个字段
		oks.setText("喜欢我就把我分享给你的朋友们吧。");
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		// oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl("http://pan.baidu.com/s/1bo76KJt");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("超级好用，你懂的！");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl("http://pan.baidu.com/s/1bo76KJt");

		// 启动分享GUI
		oks.show(this);
	}

	static class ViewHolder {
		ImageView iv_setting;
		TextView tv_setting;
	}

	@Override
	public void onCancel(Platform arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onError(Platform arg0, int arg1, Throwable arg2) {
		// TODO Auto-generated method stub

	}
}
