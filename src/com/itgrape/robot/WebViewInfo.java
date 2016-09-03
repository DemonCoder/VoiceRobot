package com.itgrape.robot;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class WebViewInfo extends Activity {

	private WebView webView;
	private String url;
	private String title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.webview_layout);

		url = getIntent().getStringExtra("url");
		title = getIntent().getStringExtra("title");
		
		TextView tv_news_title = (TextView) findViewById(R.id.tv_news_title);
		tv_news_title.setText(title);
		
		webView = (WebView) findViewById(R.id.webview);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setBuiltInZoomControls(false);
		webView.getSettings().setRenderPriority(RenderPriority.HIGH);
		webView.getSettings().setBlockNetworkImage(true);

		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView.getSettings().setAllowFileAccess(true);
		webView.getSettings().setAppCacheEnabled(true);
		webView.getSettings().setPluginsEnabled(true);
		webView.getSettings().setSaveFormData(false);
		webView.getSettings().setLoadsImagesAutomatically(true);
		String user_agent = "Mozilla/5.0 (Macintosh; U; PPC Mac OS X; en) AppleWebKit/124 (KHTML, like Gecko) Safari/125.1";
		webView.getSettings().setUserAgentString(user_agent);
		webView.getSettings().setSupportZoom(false);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		webView.loadUrl(url);
	}
	
	public void back(View view){
		finish();
	}
	public void title(View view){
	}
	public void size(View view){
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		webView.destroy();
		finish();
	}
}
