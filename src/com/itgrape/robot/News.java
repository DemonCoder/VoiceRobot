package com.itgrape.robot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
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
import bean.NewsBean;
import bean.NewsBean.NewsBody;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;

public class News extends Activity {

	private ListView lv_news;
	private ArrayList<NewsBody> bodylist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.news_layout);

		String newsjson = getIntent().getStringExtra("newsjson");
		parseNewsData(newsjson);

		lv_news = (ListView) findViewById(R.id.lv_news);
		NewsAdapter mNewsAdapter = new NewsAdapter();
		lv_news.setAdapter(mNewsAdapter);

		lv_news.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(News.this, WebViewInfo.class);
				intent.putExtra("url", bodylist.get(position).detailurl);
				intent.putExtra("title", bodylist.get(position).article);
				startActivity(intent);
			}
		});
	}

	public String getTime() {

		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd");
		String time = format.format(date);
		return time;
	}

	public void parseNewsData(String newsjson) {
		Gson gson = new Gson();
		NewsBean newsData = gson.fromJson(newsjson, NewsBean.class);
		bodylist = newsData.list;
	}

	class NewsAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return bodylist.size();
		}

		@Override
		public Object getItem(int position) {
			return bodylist.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(News.this,
						R.layout.newsdetail_layout, null);
				holder = new ViewHolder();
				holder.iv_news_pic = (ImageView) convertView
						.findViewById(R.id.iv_news_pic);
				holder.tv_news_title = (TextView) convertView
						.findViewById(R.id.tv_news_title);
				holder.tv_news_source = (TextView) convertView
						.findViewById(R.id.tv_news_source);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			NewsBody newsBody = bodylist.get(position);
			BitmapUtils bitmapUtils = new BitmapUtils(News.this);
			bitmapUtils.display(holder.iv_news_pic, newsBody.icon);
			holder.tv_news_title.setText(newsBody.article);
//			holder.tv_news_source.setText(newsBody.source);
			holder.tv_news_source.setText(getTime());
			return convertView;
		}
	}

	static class ViewHolder {
		ImageView iv_news_pic;
		TextView tv_news_title;
		TextView tv_news_source;
	}
}
