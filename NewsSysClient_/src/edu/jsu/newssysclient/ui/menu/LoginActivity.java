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
 * ��¼����
 * 	��Ҫ���ܣ�
 * 		��¼:done
 * 	��Ҫ���ܣ�
 * 		�������룺debug
 * 		�Զ���¼��done
 * 	δ��ʵ�֣�
 * 		�����˺ŵ�¼����qq�˺�
 * @author zuo
 *
 */
@EActivity(R.layout.login_layout)
public class LoginActivity extends MyBaseActivity {
	private CacheApi cache = new Cache();
	
	// ��¼ѡ��
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
		if (verify()) {	// ͨ����֤
			AppLogger.i("��ʼ��¼");
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
	 * ��¼�ĺ�̨�߳�
	 * @author zuo
	 *
	 */
	class MyLoginTask extends AsyncTask<String, Integer, String>{
		@Override
		protected void onPreExecute() {
			mLogin.setEnabled(false);
			showLoadingProgressDialog("��½��...");
		}

		@Override
		protected String doInBackground(String... params) {
			return UserHelper.login(userName.getText().toString(), passwd.getText().toString());
		}
		
		@Override
		protected void onPostExecute(String result) {
			mLogin.setEnabled(true);
			dismissProgressDialog();
			// �жϽ��
			if (result == null) {
				Toast.makeText(getApplicationContext(), "���ӷ������쳣~", 0).show();
			} else {
				// �жϵ�½���
				AppLogger.i("��½���棬�����ַ������" + result);
				String jsonResult = JsonUtil.getJsonResult(result);
				AppLogger.i("json result :" + jsonResult);
				if ("false".equals(jsonResult)) {	// ʧ��
					Toast.makeText(mContext, "��½ʧ��", 0).show();
				} else if ("true".equals(jsonResult)){
					Toast.makeText(mContext, "��½�ɹ�", 0).show();
					// �����û���Ϣ
					UserInfo userInfo = JsonUtil.getJsonUserInfo(result);
					UserInfoManager.saveUserInfo(userInfo);
					// ��¼�û��Ƿ��Զ���¼
					UserInfoManager.saveAutoLoginFlagByUserID(userInfo.getUserID(), isAutoLogin);
					// ��¼�û��Ƿ񱣴�����
					UserInfoManager.saveRememberPwdFlagByUserID(userInfo.getUserID(), isRememberPwd);
					// ���������ղ��߳�
					UserCollectManager.cacheUserCollectFromNet();
					// �رյ�½ҳ��
					((Activity) mContext).finish();
					//IntentUtil.go2AllTypeNews(mContext);
				}
			}
		}
	}
	
	/**
	 * ��֤�����Ƿ���ã��û����������Ƿ�Ϊ��
	 * @return	true,��֤ͨ��
	 */
	public boolean verify() {
		if (!NetUtil.canConnectNet()) {		// �������
			Toast.makeText(this, "û�����磬�޷����ӷ�����~", 0).show();
			return false;
		}
		
		if (StringUtil.isEmpty(userName.getText().toString())) {
		Toast.makeText(this, "�û�������Ϊ��~", 0).show();
			// ��ת���û����༭��
			
			return false;
		}

		if (StringUtil.isEmpty(passwd.getText().toString())) {
			Toast.makeText(this, "���벻��Ϊ��~", 0).show();
			// ��ת���û����༭��
			
			return false;
		}
		
		return true;
	}
	
	/**
	 * ���ע�ᰴť����ת��ע�����
	 */
	@Click
	public void mRegister() {
		Intent intent = new Intent(this, RegisterActivity_.class);
		finish();
		startActivity(intent);
	}
}
