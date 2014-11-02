package edu.jsu.newssysclient.util.local;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.widget.Toast;
import edu.jsu.newssysclient.MyApplication;
import edu.jsu.newssysclient.bean.UserInfo;
import edu.jsu.newssysclient.dao.cache.Cache;
import edu.jsu.newssysclient.debug.AppLogger;
import edu.jsu.newssysclient.util.net.UserHelper;

/**
 * 用户信息管理类
 * 	功能：
 * 		保存、获取，删除用户信息
 * 	其他功能：
 * 		记住用户密码，自动登录
 * @author zuo
 *
 */
public class UserInfoManager {
	public static Cache cache = new Cache();

	/**
	 * 用户自动登录后台线程
	 */
	public static void loginTask(UserInfo userInfo) {
		new AsyncTask<UserInfo, Integer, String>() {

			@Override
			protected String doInBackground(UserInfo... params) {
				UserInfo userInfo = params[0];
				return UserHelper.login(userInfo.getUserName(), userInfo.getUserPwd());
			}
			@Override
			protected void onPostExecute(String result) {
				if (result != null) {
					//Toast.makeText(getApplicationContext(), "Login success", 0).show();
					String jsonResult = JsonUtil.getJsonResult(result);
					if ("true".equals(jsonResult)) {
						// 缓存用户信息
						UserInfo userInfo = JsonUtil.getJsonUserInfo(result);
						UserInfoManager.saveUserInfo(userInfo);
						AppLogger.i("登录成功："+result);
						// 启动缓存收藏线程
						UserCollectManager.cacheUserCollectFromNet();
					}
				} else {
					Toast.makeText(MyApplication.getInstance(), "自动登录失败", 0).show();
					AppLogger.i("登录失败：");
				}
			}
		}.execute(userInfo);
	}
	
	/**
	 * Save User Information
	 * @param userInfo	UserInfo Object
	 */
	public static UserInfo saveUserInfo(UserInfo userInfo) {
		cache.put(Cache.USERINFO, userInfo);
		return userInfo;
	}
	
	/**
	 * Save the mark of User Auto Login Flag
	 * @param id	User Id
	 * @param flag	true, mean is auto login
	 */
	public static void saveAutoLoginFlagByUserID(long id, boolean flag) {
		cache.putBoolean(Cache.USERAUTOLOGINFLAGBYUSERID + id, flag);
	}
	
	/**
	 * Get the mark of User Auto Login Flag
	 * @param id	User Id
	 * @return	true, mean is auto login
	 */
	public static boolean getAutoLoginFlagByUserID(long id) {
		return cache.getBolean(Cache.USERAUTOLOGINFLAGBYUSERID + id);
	}
	
	/**
	 * Save the mark of User Remember Password Flag
	 * @param id	User Id
	 * @param flag	true, mean is auto login
	 */
	public static void saveRememberPwdFlagByUserID(long id, boolean flag) {
		cache.putBoolean(Cache.USERREMEMBERPWDFLAGBYUSERID + id, flag);
	}

	/**
	 * Get the mark of User Remember Password Flag
	 * @param id	User Id
	 * @return	true, mean is auto login
	 */
	public static boolean getRememberPwdFlagByUserID(long id) {
		return cache.getBolean(Cache.USERREMEMBERPWDFLAGBYUSERID + id);
	}
	
	/**
	 * Get User Information
	 * @return UserInfo Object
	 */
	public static UserInfo getUserInfo() {
		return cache.get(Cache.USERINFO, UserInfo.class);
	}
	
	/**
	 * Delete/Logout User Information
	 */
	public static void delUserInfo(long uId) {
		cache.delete(Cache.USERINFO);
		cache.delete(Cache.USERCOLLECTLISTBYUSERID + uId);
		saveAutoLoginFlagByUserID(uId, false);
		saveRememberPwdFlagByUserID(uId, false);
	}
}
