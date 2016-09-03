package com.itgrape.robot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import turing.os.http.core.ErrorMessage;
import turing.os.http.core.HttpConnectionListener;
import turing.os.http.core.RequestResult;
import utils.PrefUtils;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import bean.ChatBean;
import bean.VoiceBean;
import bean.VoiceBean.WSBean;
import bmobbean.MyUser;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.google.gson.Gson;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.turing.androidsdk.InitListener;
import com.turing.androidsdk.SDKInit;
import com.turing.androidsdk.SDKInitBuilder;
import com.turing.androidsdk.TuringApiManager;
import com.zxing.activity.CaptureActivity;

public class MainActivity extends SlidingFragmentActivity {

	private final String TURING_APIKEY = "3997fa63be044ce29f83163c678e10bc";
	private final String TURING_SECRET = "a97dfab6a8fa670e";
	private final String UNIQUEID = "123456789";
	private TuringApiManager m;

	private long theCurrentTime = 0;
	private long theLastTime = -1000;

	private EditText et_send;
	private ListView lv_talk;

	TextView tv_temp;
	TextView tv_city;

	TextView tv_user;
	ImageView iv_user;

	LinearLayout activityRootView;

	ArrayList<ChatBean> mChatBeans;
	private ListAdapter mListAdapter;

	private long currentTime = 0, oldTime = 0;
	private int mmPic[] = { R.drawable.p1, R.drawable.p2, R.drawable.p3,
			R.drawable.p4 };
	private int flag = 0;

	boolean voice;
	int speakerID;

	StringBuffer sb;

	String speakerId[] = { "xiaoyan", "xiaoyu", "catherine", "henry", "vimary",
			"vixy", "vixq", "vixf", "vixm", "vixl", "vixr", "vixyun", "vixk",
			"vixqa", "vixying", "vixx", "vinn", "vils", "Mariane", "Allabent",
			"Gabriela", "Abha", "XiaoYun" };

	String weatherData = null;
	String city = "天津";

	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();

	private ImageView more_top, scanqr;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		mLocationClient.registerLocationListener(myListener); // 注册监听函数
		mLocationClient.start();

		Bmob.initialize(this, "2dca890d44de4f0b293414179d90eb68");

		setBehindContentView(R.layout.left_menu);// 设置侧边栏
		final SlidingMenu slidingMenu = getSlidingMenu();// 获取侧边栏对象
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);// 设置全屏触摸
		int width = getWindowManager().getDefaultDisplay().getWidth();// 获取屏幕宽度
		slidingMenu.setBehindOffset(width * 110 / 320);// 设置预留屏幕的宽度
		slidingMenu.setFadeDegree(0.35f);

		tv_temp = (TextView) findViewById(R.id.tv_temp);
		tv_city = (TextView) findViewById(R.id.tv_city);

		LinearLayout ll_user;
		ll_user = (LinearLayout) findViewById(R.id.ll_user);
		iv_user = (ImageView) findViewById(R.id.iv_user);
		tv_user = (TextView) findViewById(R.id.tv_user);

		more_top = (ImageView) findViewById(R.id.more_top);
		scanqr = (ImageView) findViewById(R.id.scanqr);
		more_top.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				toggle();
			}
		});
		scanqr.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Intent intent = new Intent(MainActivity.this, Setting.class);
				// startActivity(intent);
				Toast toast = Toast.makeText(MainActivity.this, "扫描条形码或者二维码",
						Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.TOP, 0, 250);
				toast.show();
				Intent intent = new Intent(MainActivity.this,
						CaptureActivity.class);
				startActivityForResult(intent, 0);
			}
		});

		ll_user.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
				if (userInfo == null) {
					Intent intent = new Intent(MainActivity.this,
							LoginAndRegisterActivity.class);
					startActivity(intent);
				} else {

					Intent intent = new Intent(MainActivity.this,
							UserDetail.class);
					startActivity(intent);
				}
			}
		});

		ImageView setting_left = (ImageView) findViewById(R.id.setting_left);

		LinearLayout ll_weather, ll_train, ll_joke, ll_beauty, ll_news, ll_shipin, ll_more;
		ll_weather = (LinearLayout) findViewById(R.id.ll_weather);
		ll_train = (LinearLayout) findViewById(R.id.ll_train);
		ll_joke = (LinearLayout) findViewById(R.id.ll_joke);
		ll_beauty = (LinearLayout) findViewById(R.id.ll_beauty);
		ll_news = (LinearLayout) findViewById(R.id.ll_news);
		ll_shipin = (LinearLayout) findViewById(R.id.ll_shipin);
		ll_more = (LinearLayout) findViewById(R.id.ll_more);
		ll_weather.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String request = city + "今天的天气";
				toMessage(request);
				toggle();
			}
		});
		ll_train.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String request = "石家庄到天津今天的火车";
				toMessage(request);
				toggle();
			}
		});
		ll_joke.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String request = "讲个笑话";
				toMessage(request);
				toggle();
			}
		});
		ll_beauty.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String request = "来张美女图片";
				toMessage(request);
				toggle();
			}
		});
		ll_news.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String request = "新闻";
				toMessage(request);
				toggle();
			}
		});
		ll_shipin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
				if (userInfo == null) {
					new AlertDialog.Builder(MainActivity.this)
							.setTitle("提示")
							.setMessage("观看视频前请先注册账号")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											Intent intent = new Intent(
													MainActivity.this,
													LoginAndRegisterActivity.class);
											startActivity(intent);
										}

									}).setNegativeButton("取消", null).show();
				} else {
					Boolean isVip = userInfo.isVip();
					if (isVip) {
						toMessage("视频");
					} else {
						new AlertDialog.Builder(MainActivity.this)
								.setTitle("提示").setMessage("请先联系开发者升级为VIP")
								.setPositiveButton("确定", null).show();
					}
				}
				toggle();
			}
		});
		ll_more.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				toggle();
			}
		});

		setting_left.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, Setting.class);
				startActivity(intent);
				toggle();
			}
		});

		tv_temp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Uri uri = Uri.parse("http://m.weather.com.cn/mweather");
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
		});

		new Thread(new Runnable() {
			@Override
			public void run() {
				parseWeatherData(city);
				try {
					Thread.sleep(1000 * 60 * 10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();

		MyApplication.setMainContext(this);

		initData();

		SpeechUtility.createUtility(this, SpeechConstant.APPID + "=57b30cd5");

		initView();

		// et_send.setOnFocusChangeListener(new OnFocusChangeListener() {
		//
		// @Override
		// public void onFocusChange(View v, boolean hasFocus) {
		// if (hasFocus) {
		// slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);//
		// 设置不启动
		// } else {
		// slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);//
		// 设置全屏触摸
		// }
		// }
		// });
		activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						int heightDiff = activityRootView.getRootView()
								.getHeight() - activityRootView.getHeight();
						if (heightDiff > 100) { // 高度变小100像素则认为键盘弹出
							slidingMenu
									.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
						} else {
							slidingMenu
									.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
						}
					}
				});

		SDKInitBuilder builder = new SDKInitBuilder(this)
				.setSecret(TURING_SECRET).setTuringKey(TURING_APIKEY)
				.setUniqueId(UNIQUEID);
		SDKInit.init(builder, new InitListener() {
			@Override
			public void onFail(String arg0) {
			}

			@Override
			public void onComplete() {

				m = new TuringApiManager(MainActivity.this);
				m.setHttpListener(new HttpConnectionListener() {

					@Override
					public void onSuccess(RequestResult result) {
						String text = result.getContent().toString();
						parseData(text);
						Log.e("text", text);
					}

					@Override
					public void onError(ErrorMessage arg0) {
					}
				});
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		checkUser();
	}

	@Override
	public void onBackPressed() {
		theCurrentTime = System.currentTimeMillis();
		if (theCurrentTime - theLastTime < 1000) {
			finish();
		} else {
			Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT)
					.show();
		}
		theLastTime = theCurrentTime;
	}

	public void listenUI(View view) {
		RecognizerDialog mDialog = new RecognizerDialog(this, mInitListener);
		mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
		mDialog.setParameter(SpeechConstant.ACCENT, "mandarin");

		mDialog.setListener(mRecognizerDialogListener);
		mDialog.show();
	}

	private com.iflytek.cloud.InitListener mInitListener = new com.iflytek.cloud.InitListener() {

		@Override
		public void onInit(int arg0) {

		}
	};
	private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {

		@Override
		public void onResult(RecognizerResult result, boolean isLast) {
			if (!isLast) {
				Gson gson = new Gson();
				VoiceBean bean = gson.fromJson(result.getResultString(),
						VoiceBean.class);
				ArrayList<WSBean> ws = bean.ws;

				sb = new StringBuffer();
				for (WSBean wsBean : ws) {
					String text = wsBean.cw.get(0).w;
					sb.append(text);
				}
			} else {
				String text = sb.toString();
				toMessage(text);
			}
		}

		@Override
		public void onError(SpeechError arg0) {

		}
	};

	public void read(String speaker) {
		voice = PrefUtils.getBoolean(this, "voice", true);
		speakerID = PrefUtils.getInt(this, "speaker", 0);
		if (voice) {
			SpeechSynthesizer mTts = SpeechSynthesizer.createSynthesizer(this,
					null);
			mTts.setParameter(SpeechConstant.VOICE_NAME, speakerId[speakerID]); // 设置发音人
			mTts.setParameter(SpeechConstant.SPEED, "50");// 设置语速
			mTts.setParameter(SpeechConstant.VOLUME, "80");// 设置音量，范围 0~100
			mTts.setParameter(SpeechConstant.ENGINE_TYPE,
					SpeechConstant.TYPE_CLOUD); // 设置云端
			mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH,
					"./sdcard/iflytek.pcm");

			mTts.startSpeaking(speaker, mSynListener);
		}
	}

	private SynthesizerListener mSynListener = new SynthesizerListener() {
		// 会话结束回调接口，没有错误时， error为null
		public void onCompleted(SpeechError error) {
		}

		// 缓冲进度回调
		// percent为缓冲进度0~100， beginPos为缓冲音频在文本中开始位置， endPos表示缓冲音频在文本中结束位置，
		// info为附加信息。
		public void onBufferProgress(int percent, int beginPos, int endPos,
				String info) {
		}

		// 开始播放
		public void onSpeakBegin() {
		}

		// 暂停播放
		public void onSpeakPaused() {
		}

		// 播放进度回调
		// percent为播放进度0~100,beginPos为播放音频在文本中开始位置， endPos表示播放音频在文本中结束位置.
		public void onSpeakProgress(int percent, int beginPos, int endPos) {
		}

		// 恢复播放回调接口
		public void onSpeakResumed() {
		}

		// 会话事件回调接口
		public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
		}
	};

	public void initView() {

		activityRootView = (LinearLayout) findViewById(R.id.activityRoot);

		et_send = (EditText) findViewById(R.id.et_send);
		lv_talk = (ListView) findViewById(R.id.lv_talk);
		mChatBeans = new ArrayList<ChatBean>();

		ChatBean welcomeBean = new ChatBean(2, "你好", "", "");
		mChatBeans.add(welcomeBean);

		mListAdapter = new ListAdapter();
		lv_talk.setAdapter(mListAdapter);

		lv_talk.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ChatBean chatBean = mChatBeans.get(position);
				String url = chatBean.url;
				if (!TextUtils.isEmpty(url)) {
					if (url.startsWith("http")) {

					} else {
						url = "http://www.baidu.com/s?word=" + url;
					}
					Uri uri = Uri.parse(url);
					Intent intent = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(intent);
				} else {

				}
			}
		});
	}

	public void parseData(final String result) {
		ChatBean fromBean;

		String text = "";
		String url = "";
		try {
			JSONObject object = new JSONObject(result);
			text = object.getString("text");
			read(text);
			if (object.getLong("code") == 200000) {
				url = object.getString("url");
			}
			if (object.getLong("code") == 302000) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(1500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						Intent intent = new Intent(MainActivity.this,
								News.class);
						intent.putExtra("newsjson", result);
						startActivity(intent);
					}
				}).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		fromBean = new ChatBean(2, text, url, "");

		mChatBeans.add(fromBean);
		mListAdapter.notifyDataSetChanged();
	}

	public void send(View view) {
		String request = et_send.getText().toString();

		if (!TextUtils.isEmpty(request)) {
			et_send.setText("");
			toMessage(request);
		}
	}

	public void toMessage(String request) {

		ChatBean toBean = new ChatBean(1, request, "", "");
		mChatBeans.add(toBean);
		mListAdapter.notifyDataSetChanged();

		if (request.contains("美女")) {
			Random random = new Random();
			int num = random.nextInt(15000);
			String url = "http://route.showapi.com/819-1?showapi_appid=23566&type=&num=1&page="
					+ num + "&showapi_sign=cd243d84ab5043c196a2aa355c65fc81";

			HttpUtils http = new HttpUtils();
			http.send(HttpMethod.GET, url, new RequestCallBack<String>() {

				@Override
				public void onFailure(HttpException arg0, String arg1) {

				}

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					String result = responseInfo.result;
					parseMMData(result);
				}
			});
		} else if (request.contains("新闻")) {
			m.requestTuringAPI(request);
		} else if (request.contains("视频")) {
			ChatBean fromBean = new ChatBean(2, "已为您打开视频窗口", "", "");
			mChatBeans.add(fromBean);
			mListAdapter.notifyDataSetChanged();

			Intent intent = new Intent(MainActivity.this, VideoActivity.class);
			startActivity(intent);
		} else {
			m.requestTuringAPI(request);
		}
	}

	public void parseMMData(String result) {

		try {
			JSONObject object = new JSONObject(result);
			JSONObject MMsObject = object.getJSONObject("showapi_res_body");
			JSONObject MMBodyObject = MMsObject.getJSONObject("0");
			String thumb = MMBodyObject.getString("thumb");
			String title = MMBodyObject.getString("title");

			read(title);

			ChatBean frombBean = new ChatBean(2, title, "", thumb);
			mChatBeans.add(frombBean);
			mListAdapter.notifyDataSetChanged();

			// Toast.makeText(MainActivity.this, thumb,
			// Toast.LENGTH_SHORT).show();

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	class ListAdapter extends BaseAdapter {

		private String url;
		private String attachPic;

		@Override
		public int getCount() {
			return mChatBeans.size();
		}

		@Override
		public ChatBean getItem(int position) {
			return mChatBeans.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			TextView tv_ask = null, tv_answer = null;
			TextView ask_time = null, answer_time = null;
			TextView attachURL = null;
			ImageView attachPIC = null;

			int type = mChatBeans.get(position).type;
			if (type == 1) {
				convertView = View.inflate(MainActivity.this,
						R.layout.ask_chat_cell, null);
				tv_ask = (TextView) convertView.findViewById(R.id.tv_ask);
				tv_ask.setText(mChatBeans.get(position).text);

				ask_time = (TextView) convertView.findViewById(R.id.ask_time);
				ask_time.setText(getTime());
			} else if (type == 2) {
				convertView = View.inflate(MainActivity.this,
						R.layout.answer_chat_cell, null);
				tv_answer = (TextView) convertView.findViewById(R.id.tv_answer);
				tv_answer.setText(mChatBeans.get(position).text);

				answer_time = (TextView) convertView
						.findViewById(R.id.answer_time);
				answer_time.setText(getTime());

				attachURL = (TextView) convertView.findViewById(R.id.attachURL);
				attachPIC = (ImageView) convertView
						.findViewById(R.id.attachPIC);

				url = mChatBeans.get(position).url;

				if (!url.equals("")) {
					attachURL.setText(url);
				}

				attachPic = mChatBeans.get(position).attachPIC;
				if (attachPic != "") {
					BitmapUtils bitmapUtils = new BitmapUtils(MainActivity.this);
					bitmapUtils.display(attachPIC, attachPic);
				}
			}
			return convertView;
		}
	}

	public String getTime() {

		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat();
		String time = format.format(date);

		currentTime = System.currentTimeMillis();
		if (currentTime - oldTime > 3 * 60 * 1000) {
			oldTime = currentTime;
			return time;
		} else {
			return "";
		}
	}

	public int getMMPIC() {
		Random random = new Random();
		int ran = random.nextInt(mmPic.length);
		return ran;
	}

	public void initData() {
		PrefUtils.setBoolean(this, "create", true);
	}

	public void parseWeatherData(String city) {

		HttpUtils http = new HttpUtils();
		http.send(HttpMethod.GET,
				"http://api.jisuapi.com/weather/query?appkey=13164ba1bb472789&city="
						+ city, new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						weatherData = responseInfo.result;
						try {
							JSONObject object = new JSONObject(weatherData);
							JSONObject resultObject = object
									.getJSONObject("result");
							String city = resultObject.getString("city");
							String temp = resultObject.getString("temp");
							String weather = resultObject.getString("weather");
							// Toast.makeText(MainActivity.this,
							// city + ":" + temp + ":" + weather,
							// Toast.LENGTH_SHORT).show();

							tv_temp = (TextView) findViewById(R.id.tv_temp);
							tv_city = (TextView) findViewById(R.id.tv_city);

							tv_city.setText(city + " " + weather);
							tv_temp.setText(temp + "°C");

						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			String text = data.getExtras().getString("result");
			ChatBean fromBean = new ChatBean(2, "二维码信息如下...", text, "");
			mChatBeans.add(fromBean);
			mListAdapter.notifyDataSetChanged();
			read("二维码信息如下");
		}
	}

	public void checkUser() {
		MyUser user = BmobUser.getCurrentUser(MyUser.class);
		if (user == null) {
			tv_user.setText("登陆");
			iv_user.setImageResource(R.drawable.user);
		} else {
			String username = user.getUsername();
			tv_user.setText(username);
			iv_user.setImageResource(R.drawable.userlogin);

			BitmapUtils bitmapUtils = new BitmapUtils(MainActivity.this);
			bitmapUtils.configDefaultLoadFailedImage(R.drawable.userlogin);
			bitmapUtils.configDefaultLoadingImage(R.drawable.userlogin);

			String portraitUrl = user.getPortraitUrl();
			if (!TextUtils.isEmpty(portraitUrl)) {
				bitmapUtils.display(iv_user, portraitUrl);
			}

		}
	}

	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
		int span = 1000;
		option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);// 可选，默认false,设置是否使用gps
		option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		option.setIgnoreKillProcess(false);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
		option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
		option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
		mLocationClient.setLocOption(option);
	}

	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {

			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			getGeoAddress(latitude, longitude);
		}

		void getGeoAddress(double latitude, double longitude) {
			String coordinates = "http://api.jisuapi.com/geoconvert/coord2addr?lat="
					+ latitude
					+ "&lng="
					+ longitude
					+ "&type=google&appkey=13164ba1bb472789";
			HttpUtils http = new HttpUtils();
			http.send(HttpMethod.GET, "", new RequestCallBack<String>() {
				@Override
				public void onFailure(HttpException arg0, String arg1) {

				}

				@Override
				public void onSuccess(ResponseInfo<String> arg0) {
					JSONObject object = new JSONObject();
					try {
						String geo = object.getJSONObject("result").getString(
								"city");
						city = geo.replace("市", "");
						parseWeatherData(city);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		}

		public void setting(View view) {

		}

		public void moreTop(View view) {
			toggle();
		}
	}
}
