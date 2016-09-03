package base;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.voicerecognition.android.v;
import com.itgrape.robot.MainActivity;
import com.itgrape.robot.MyApplication;
import com.itgrape.robot.R;
import com.itgrape.robot.Setting;

public class BaseTitle extends LinearLayout {

	TextView tv_title;
	ImageView btn_back;
	ImageView btn_setting;

	public BaseTitle(Context context) {
		super(context);
	}

	public BaseTitle(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		LayoutInflater.from(context).inflate(R.layout.base_title, this);

		TypedArray typeArray = context.obtainStyledAttributes(attrs,
				R.styleable.MyView);
		String titleText = typeArray.getString(R.styleable.MyView_titleText);
		int backView = typeArray.getInt(R.styleable.MyView_viewBack, 1);
		int setView = typeArray.getInt(R.styleable.MyView_viewSet, 1);

		typeArray.recycle();

		tv_title = (TextView) findViewById(R.id.tv_news_title);
		btn_back = (ImageView) findViewById(R.id.btn_back);
		btn_setting = (ImageView) findViewById(R.id.btn_setting);

		tv_title.setText(titleText);
		if (backView == 1) {
			btn_back.setVisibility(View.VISIBLE);
		} else {
			btn_back.setVisibility(View.INVISIBLE);
		}
		if (setView == 1) {
			btn_setting.setVisibility(View.VISIBLE);
		} else {
			btn_setting.setVisibility(View.INVISIBLE);
		}
		
		btn_setting.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MainActivity mainActivity = (MainActivity) MyApplication.getMainContext();
				Intent intent = new Intent(mainActivity,Setting.class);
				mainActivity.startActivity(intent);
			}
		});
		btn_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Setting setting = (Setting) MyApplication.getSettingContext();
				setting.finish();
			}
		});
	}
}
