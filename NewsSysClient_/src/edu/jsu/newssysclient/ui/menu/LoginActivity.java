package edu.jsu.newssysclient.ui.menu;

import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import edu.jsu.newssysclient.R;
import edu.jsu.newssysclient.URLHelper;
import edu.jsu.newssysclient.bean.UserInfo;
import edu.jsu.newssysclient.dao.cache.Cache;
import edu.jsu.newssysclient.dao.cache.CacheApi;
import edu.jsu.newssysclient.debug.AppLogger;
import edu.jsu.newssysclient.ui.main.MyBaseActivity;
import edu.jsu.newssysclient.util.local.IntentUtil;
import edu.jsu.newssysclient.util.local.JsonUtil;
import edu.jsu.newssysclient.util.local.StringUtil;
import edu.jsu.newssysclient.util.local.UserCollectManager;
import edu.jsu.newssysclient.util.local.UserInfoManager;
import edu.jsu.newssysclient.util.net.NetUtil;
import edu.jsu.newssysclient.util.net.UserHelper;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 登录界面
 * 	主要功能：
 * 		登录:done
 * 	次要功能：
 * 		保存密码：debug
 * 		自动登录：done
 * 	未来实现：
 * 		其他账号登录，如qq账号
 * @author zuo
 *
 */
@EActivity(R.layout.login_layout)
public class LoginActivity extends MyBaseActivity {
	private CacheApi cache = new Cache();
	
	// 登录选项
	private boolean isRememberPwd = false;
	private boolean isAutoLogin = false;

	@ViewById
	EditText userName;
	@ViewById
	EditText passwd;
	@ViewById
	Button mLogin;
	@ViewById
	CheckBox rememberPwd;
	@ViewById
	CheckBox autoLogin;
	
	@Click
	public void mLogin() {
		if (verify()) {	// 通过验证
			AppLogger.i("开始登录");
			new MyLoginTask().execute();
		}
	}
	
	@CheckedChange
	public void rememberPwd(CompoundButton hello, boolean isChecked) {
		Toast.makeText(this, "remember pwd :" + isChecked, 0).show();
		isRememberPwd = isChecked;
	}

	@CheckedChange
	public void autoLogin(CompoundButton hello, boolean isChecked) {
		Toast.makeText(this, "auto login :" + isChecked, 0).show();
		if (isChecked && rememberPwd.isChecked() != false) {
			rememberPwd.setChecked(true);
		}
		isAutoLogin = isChecked;
	}
	
	/**
	 * 登录的后台线程
	 * @author zuo
	 *
	 */
	class MyLoginTask extends AsyncTask<String, Integer, String>{
		@Override
		protected void onPreExecute() {
			mLogin.setEnabled(false);
			showLoadingProgressDialog("登陆中...");
		}

		@Override
		protected String doInBackground(String... params) {
			return UserHelper.login(userName.getText().toString(), passwd.getText().toString());
		}
		
		@Override
		protected void onPostExecute(String result) {
			mLogin.setEnabled(true);
			dismissProgressDialog();
			// 判断结果
			if (result == null) {
				Toast.makeText(getApplicationContext(), "连接服务器异常~", 0).show();
			} else {
				// 判断登陆结果
				AppLogger.i("登陆界面，返回字符结果：" + result);
				String jsonResult = JsonUtil.getJsonResult(result);
				AppLogger.i("json result :" + jsonResult);
				if ("false".equals(jsonResult)) {	// 失败
					Toast.makeText(mContext, "登陆失败", 0).show();
				} else if ("true".equals(jsonResult)){
					Toast.makeText(mContext, "登陆成功", 0).show();
					// 保存用户信息
					UserInfo userInfo = JsonUtil.getJsonUserInfo(result);
					UserInfoManager.saveUserInfo(userInfo);
					// 记录用户是否自动登录
					UserInfoManager.saveAutoLoginFlagByUserID(userInfo.getUserID(), isAutoLogin);
					// 记录用户是否保存密码
					UserInfoManager.saveRememberPwdFlagByUserID(userInfo.getUserID(), isRememberPwd);
					// 启动缓存收藏线程
					UserCollectManager.cacheUserCollectFromNet();
					// 关闭登陆页面
					((Activity) mContext).finish();
					//IntentUtil.go2AllTypeNews(mContext);
				}
			}
		}
	}
	
	/**
	 * 验证网络是否可用，用户名和密码是否为空
	 * @return	true,验证通过
	 */
	public boolean verify() {
		if (!NetUtil.canConnectNet()) {		// 检查网络
			Toast.makeText(this, "没有网络，无法连接服务器~", 0).show();
			return false;
		}
		
		if (StringUtil.isEmpty(userName.getText().toString())) {
		Toast.makeText(this, "用户名不能为空~", 0).show();
			// 跳转到用户名编辑框
			
			return false;
		}

		if (StringUtil.isEmpty(passwd.getText().toString())) {
			Toast.makeText(this, "密码不能为空~", 0).show();
			// 跳转到用户名编辑框
			
			return false;
		}
		
		return true;
	}
	
	/**
	 * 点击注册按钮，跳转到注册界面
	 */
	@Click
	public void mRegister() {
		Intent intent = new Intent(this, RegisterActivity_.class);
		finish();
		startActivity(intent);
	}
}
