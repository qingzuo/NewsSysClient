package edu.jsu.newssysclient.ui.main;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import edu.jsu.newssysclient.R;
import edu.jsu.newssysclient.URLHelper;
import edu.jsu.newssysclient.bean.Item;
import edu.jsu.newssysclient.bean.ItemList;
import edu.jsu.newssysclient.bean.UserInfo;
import edu.jsu.newssysclient.dao.cache.Cache;
import edu.jsu.newssysclient.dao.cache.CacheApi;
import edu.jsu.newssysclient.debug.AppLogger;
import edu.jsu.newssysclient.util.local.JsonUtil;
import edu.jsu.newssysclient.util.local.StringUtil;
import edu.jsu.newssysclient.util.local.UserCollectManager;
import edu.jsu.newssysclient.util.local.UserCommentManager;
import edu.jsu.newssysclient.util.local.UserInfoManager;
import edu.jsu.newssysclient.util.net.UserHelper;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.Toast;

/**
 * 欢迎界面
 * 功能：初始化信息，登录
 * @author zuo
 *
 */
@EActivity(R.layout.welcome_layout)
public class WelcomeActivity extends Activity {
	private CacheApi cache = new Cache();
	private UserInfo userInfo;

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		// 更改服务器地址
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		URLHelper.initAll(sp.getString(StringUtil.getString(R.string.prekey_serverip), URLHelper.SERVERURL));
		//AppLogger.i(URLHelper.NEWSLIST);
		
		// 获取用户
    	userInfo = UserInfoManager.getUserInfo();
    	// 判断是否要自动登录
//    	if (userInfo != null && true == UserInfoManager.getAutoLoginFlagByUserID(userInfo.getUserID())) {
    	if (userInfo != null) {
    		// 启动登录线程
    		loginBackgroundWork();
    	}
    	// 启动主界面
		backgroundWork();
	}
	
	
	/**
	 * 延迟3秒启动主界面
	 */
	@Background(delay=1500)
	public void backgroundWork() {
		startAllTypeNewsActivity();
	}
	
	
	/**
	 * 延迟5秒启动登录线程
	 */
	@Background(delay=5000)
	public void loginBackgroundWork() {
		UserInfoManager.loginTask(userInfo);
	}
	
	@UiThread
	public void startAllTypeNewsActivity() {
		Intent intent = new Intent(this, AllTypeNewsActivity.class);
		startActivity(intent);
		finish();
		// 切换动画
		overridePendingTransition(R.anim.enter_righttoleft, R.anim.out_righttoleft);
	}
}
