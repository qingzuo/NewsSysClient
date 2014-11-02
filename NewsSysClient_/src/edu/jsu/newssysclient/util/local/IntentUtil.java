package edu.jsu.newssysclient.util.local;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import edu.jsu.newssysclient.R;
import edu.jsu.newssysclient.debug.AppLogger;
import edu.jsu.newssysclient.ui.main.AllTypeNewsActivity;
import edu.jsu.newssysclient.ui.main.ShowNewsActivity;
import edu.jsu.newssysclient.ui.main.WebViewActivity;
import edu.jsu.newssysclient.ui.main.WelcomeActivity_;
import edu.jsu.newssysclient.ui.menu.ChangeUserInfoActivity;
import edu.jsu.newssysclient.ui.menu.ChangeUserInfoActivity_;
import edu.jsu.newssysclient.ui.menu.LoginActivity_;

/**
 * Intent 工具，用于启动不同的界面
 * @author zuo
 *
 */
public class IntentUtil {

	/**
	 * 跳到登录界面
	 * @param context
	 */
	public static void go2Login(Context context) { 
		Toast.makeText(context, StringUtil.getString(R.string.please_login_first), 0).show();
		Intent intent = new Intent(context, LoginActivity_.class);
		context.startActivity(intent);
	}

	/**
	 * 跳到更改用户信息界面
	 * @param context
	 */
	public static void go2WebViewActivity(Context context, String url) {
		Intent intent = new Intent(context, WebViewActivity.class);
		if (url != null) {
			intent.putExtra(WebViewActivity.WEBVIEWURL, url);
		}
		context.startActivity(intent);
	}

	/**
	 * 跳到更改用户信息界面
	 * @param context
	 */
	public static void go2ChangeUserInfo(Context context, int mode) {
		Intent intent = new Intent(context, ChangeUserInfoActivity_.class);
		intent.putExtra(ChangeUserInfoActivity.CHANGEUSERINFOMODE, mode);
		context.startActivity(intent);
	}

	/**
	 * 跳到主界面界面并打开侧边栏菜单
	 * @param context
	 */
	public static void go2WelcomeActivity(Context context) {
		Intent intent = new Intent(context, WelcomeActivity_.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		//intent.putExtra(AllTypeNewsActivity.NEEDOPENMENU, true);
		context.startActivity(intent);
	}
	
	public static void restartApp(Context context) {
		Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());  
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
        context.startActivity(intent);  
	}

	/**
	 * 跳到主界面界面并打开侧边栏菜单
	 * @param context
	 */
	public static void go2AllTypeNews(Context context) {
		Intent intent = new Intent(context, AllTypeNewsActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
		//intent.putExtra(AllTypeNewsActivity.NEEDOPENMENU, true);
		context.startActivity(intent);
	}

	/**
	 * 跳到普通新闻详细界面
	 * @param context
	 * @param id
	 */
	public static void go2ShowNewsActivity(Context context, String id) {
		Intent intent;
		intent = new Intent(context,
				ShowNewsActivity.class);
		// 传递新闻id
		intent.putExtra(ShowNewsActivity.SHOWNEWSACTIVITYNEWSID, id);
		AppLogger.i("新闻列表传入ID到新闻详细界面的ID: " + id);
		context.startActivity(intent);
	}
	
	/**
	 * 启动一个activity
	 * @param context	
	 * @param cls
	 */
	public static void go2Activity(Context context,  Class<?> cls) {
		Intent intent;
		intent = new Intent(context,
				cls);
		context.startActivity(intent);
	}
}
