package edu.jsu.newssysclient.ui.main;

import edu.jsu.newssysclient.R;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

/**
 * 显示一个网络地址
 * @author zuo
 *
 */
public class WebViewActivity extends MyBaseActivity{
	public static String WEBVIEWURL = "edu.jsu.newssysclient.url";
	String url = "www.baidu.com";
	WebView web;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 获取url
		if (savedInstanceState != null){
			 url= savedInstanceState.getString(WEBVIEWURL);
		}
		if (getIntent() != null && getIntent().getStringExtra(WEBVIEWURL) != null) {
			url = getIntent().getStringExtra(WEBVIEWURL);
			getActionBar().setTitle(url);
		}
		
		setContentView(R.layout.webview_activity);
		
		// 绑定控件
		web = (WebView) findViewById(R.id.webview);
		web.getSettings().setJavaScriptEnabled(true);
		web.loadUrl(url);
		
		Toast.makeText(this, "链接到 : "+url, 0).show();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString(WEBVIEWURL, url);
		super.onSaveInstanceState(outState);
	}
}
