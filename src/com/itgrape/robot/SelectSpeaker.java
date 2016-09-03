package com.itgrape.robot;

import utils.PrefUtils;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class SelectSpeaker extends Activity {

	TextView tv_setSpeaker;
	ImageView iv_setSpeaker;
	LinearLayout ll_setSpeaker;
	ListView lv_speaker;
	
	int flag_speaker = 0;
	boolean flag_on = true;
	

	String speaker[] = { "小燕 青年女声 中英文（普通话）", "小宇 青年男声 中英文（普通话）", "凯瑟琳 青年女声 英文",
			"亨利 青年男声 英文", "玛丽 青年女声 英文", "小研 青年女声 中英文（普通话）", "小琪 青年女声 中英文（普通话）",
			"小峰 青年男声 中英文（普通话）", "小梅 青年女声 中英文（粤语）", "小莉 青年女声 中英文（台湾普通话）",
			"小蓉 青年女声 汉语（四川话）", "小芸 青年女声 汉语（东北话）", "小坤 青年男声 汉语（河南话）",
			"小强 青年男声 汉语（湖南话）", "小莹 青年女声 汉语（陕西话）", "小新 童年男声 汉语（普通话）",
			"楠楠 童年女声 汉语（普通话）", "老孙 老年男声 汉语（普通话）", "Mariane 法语", "Allabent 俄语",
			"Gabriela 西班牙语", "Abha 印地语", "XiaoYun 越南语" };
	String speakerId[] = { "xiaoyan", "xiaoyu", "catherine", "henry", "vimary",
			"vixy", "vixq", "vixf", "vixm", "vixl", "vixr", "vixyun", "vixk",
			"vixqa", "vixying", "vixx", "vinn", "vils", "Mariane", "Allabent",
			"Gabriela", "Abha", "XiaoYun" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.select_speaker);
		
		initData();

		tv_setSpeaker = (TextView) findViewById(R.id.tv_setSpeaker);
		iv_setSpeaker = (ImageView) findViewById(R.id.iv_setSpeaker);
		ll_setSpeaker = (LinearLayout) findViewById(R.id.ll_setSpeaker);
		lv_speaker = (ListView) findViewById(R.id.lv_select_speaker);
		final SpeakerAdapter mSpeakerAdapter = new SpeakerAdapter();
		lv_speaker.setAdapter(mSpeakerAdapter);
		
		ll_setSpeaker.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (iv_setSpeaker.getVisibility() == View.VISIBLE) {
					
					PrefUtils.setBoolean(SelectSpeaker.this, "voice", false);
					
					iv_setSpeaker.setVisibility(View.INVISIBLE);
					flag_on = false;
					mSpeakerAdapter.notifyDataSetChanged();
				} else {
					
					PrefUtils.setBoolean(SelectSpeaker.this, "voice", true);
					
					iv_setSpeaker.setVisibility(View.VISIBLE);
					flag_on = true;
					mSpeakerAdapter.notifyDataSetChanged();
				}
			}
		});

		lv_speaker.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				iv_setSpeaker.setVisibility(View.VISIBLE);
				flag_on = true;

				flag_speaker = position;
				mSpeakerAdapter.notifyDataSetChanged();
				
				PrefUtils.setInt(SelectSpeaker.this, "speaker", position);
				
			}
		});
	}

	class SpeakerAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return speaker.length;
		}

		@Override
		public String getItem(int position) {
			return speaker[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				convertView = View.inflate(SelectSpeaker.this,
						R.layout.speaker_cell, null);
			}
			TextView tv_speaker = (TextView) convertView
					.findViewById(R.id.tv_speaker);
			ImageView iv_speaker_checked = (ImageView) convertView
					.findViewById(R.id.iv_speaker_checked);

			tv_speaker.setText(getItem(position));
			if (position == flag_speaker && flag_on == true) {
				iv_speaker_checked.setVisibility(View.VISIBLE);
			} else {
				iv_speaker_checked.setVisibility(View.INVISIBLE);
			}

			return convertView;
		}
	}
	
	public void initData(){
		flag_on = PrefUtils.getBoolean(this, "voice", true);
		flag_speaker = PrefUtils.getInt(this, "speaker", 0);
	}
}
