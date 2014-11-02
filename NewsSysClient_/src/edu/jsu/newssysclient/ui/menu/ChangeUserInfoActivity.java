package edu.jsu.newssysclient.ui.menu;

import java.io.File;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.json.JSONObject;

import edu.jsu.newssysclient.R;
import edu.jsu.newssysclient.URLHelper;
import edu.jsu.newssysclient.bean.UserInfo;
import edu.jsu.newssysclient.debug.AppLogger;
import edu.jsu.newssysclient.ui.main.AllTypeNewsActivity;
import edu.jsu.newssysclient.ui.main.MyBaseActivity;
import edu.jsu.newssysclient.ui.util.ImageViewUtil;
import edu.jsu.newssysclient.ui.util.UserViewUtil;
import edu.jsu.newssysclient.util.local.JsonUtil;
import edu.jsu.newssysclient.util.local.StringUtil;
import edu.jsu.newssysclient.util.local.UserInfoManager;
import edu.jsu.newssysclient.util.net.NetUtil;
import edu.jsu.newssysclient.util.net.UserHelper;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.UserManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * �û���Ϣ���Ľ��� �����û�ͷ�� �����û����룺 �����û�����ǩ��������
 * 
 * @author zuo
 * 
 */
@EActivity(R.layout.change_userinfo_layout)
public class ChangeUserInfoActivity extends MyBaseActivity {
	static final int REQUEST_TAKE_PHOTO = 221;
	private static final int SELECT_PIC_KITKAT = 222;
	private static final int SELECT_PIC = 223;
	public static final String CHANGEUSERINFOMODE = "edu.jsu.newssysclient.ui.menu.ChangeUserInfoActivity.changeuserinfomode";
	public static final int USER_MODE_PASSWORD = 331;
	public static final int USER_MODE_AVATAR = 332;
	public static final int USER_MODE_SIGNATUREEMAIL = 333;
	private String mCurrentPhotoPath = null; // ͷ��

	private UserInfo userInfo;

	// ��������
	@ViewById
	LinearLayout ll_change_pwd;
	@ViewById
	EditText oldPasswd;
	@ViewById
	EditText newPasswd;
	@ViewById
	EditText newPasswd2;
	@ViewById
	Button changPwd;

	// ����ͷ��
	@ViewById
	LinearLayout ll_change_avatar;
	@ViewById
	Button takeImage;
	@ViewById
	Button findImage;
	@ViewById
	ImageView userAvatar;
	@ViewById
	Button updateAvatar;

	// ����ǩ������
	@ViewById
	LinearLayout ll_change_signature_mail;
	@ViewById
	EditText email;
	@ViewById
	EditText personNote;
	@ViewById
	Button updateUserInfo;

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		userInfo = UserInfoManager.getUserInfo();
		
		int mode = getIntent().getIntExtra(CHANGEUSERINFOMODE, USER_MODE_SIGNATUREEMAIL);
		switch (mode) {
		case USER_MODE_PASSWORD: // ��ʾ�������빦��
			ll_change_pwd.setVisibility(View.VISIBLE);
			ll_change_signature_mail.setVisibility(View.GONE);
			ll_change_avatar.setVisibility(View.GONE);
			break;
			
		case USER_MODE_AVATAR: // ��ʾ����ͷ����
			ll_change_signature_mail.setVisibility(View.GONE);
			ll_change_avatar.setVisibility(View.VISIBLE);
			ll_change_pwd.setVisibility(View.GONE);
			if (userInfo != null && mCurrentPhotoPath == null)
				ImageViewUtil.loadImage(userAvatar, URLHelper.SERVERURL+userInfo.getPtoShowPath());
			break;
			
		case USER_MODE_SIGNATUREEMAIL: // ��ʾ����ǩ�����书��
			ll_change_signature_mail.setVisibility(View.VISIBLE);
			ll_change_avatar.setVisibility(View.GONE);
			ll_change_pwd.setVisibility(View.GONE);
			email.setText(userInfo.getEmail());
			personNote.setText(userInfo.getSignature());
			break;

		default:	// Ĭ����ʾ����ǩ�����书��
			ll_change_signature_mail.setVisibility(View.VISIBLE);
			ll_change_avatar.setVisibility(View.GONE);
			ll_change_pwd.setVisibility(View.GONE);
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case SELECT_PIC: // �ɷ�ʽ��ͼ��ͼ��ѡ����Ƭ
		case SELECT_PIC_KITKAT:
			// ��Ƭ��ԭʼ��Դ��ַ
			Uri uri = data.getData();
			mCurrentPhotoPath = UserViewUtil.getPath(this, uri);
			AppLogger.i("ѡ���ͼƬ·����" + mCurrentPhotoPath);
			UserViewUtil.setPic(userAvatar, mCurrentPhotoPath);
			break;

		case REQUEST_TAKE_PHOTO: // ���ղ�����·��
			UserViewUtil.setPic(userAvatar, mCurrentPhotoPath);
			break;

		default:
			break;
		}
	}

	/**
	 * ����
	 */
	private void takePictrue() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // ����ϵͳ���
		File file = new File(Environment.getExternalStorageDirectory(),
				"image.jpg");
		mCurrentPhotoPath = file.getPath();
		AppLogger.i("ע����棬ͼƬ·����" + file.getPath());
		Uri imageUri = Uri.fromFile(file);
		// ָ����Ƭ����·����SD������image.jpgΪһ����ʱ�ļ���ÿ�����պ����ͼƬ���ᱻ�滻
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		if (intent.resolveActivity(getPackageManager()) != null) {
			startActivityForResult(intent, REQUEST_TAKE_PHOTO); // �û�����˴������ȡ
		}
	}

	/**
	 * ѡ��ͼƬ
	 */
	private void selectPhoto() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);// ACTION_OPEN_DOCUMENT
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("image/jpeg");
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
			startActivityForResult(intent, SELECT_PIC_KITKAT);
		} else {
			startActivityForResult(intent, SELECT_PIC);
		}
	}

	/**
	 * ���հ�ť����¼�
	 */
	@Click
	public void takeImage() {
		AppLogger.i("ע����棬����");
		takePictrue();
	}

	/**
	 * ����ͼƬ����¼�
	 */
	@Click
	public void findImage() {
		AppLogger.i("ע����棬���ұ���ͼƬ");
		selectPhoto();
	}

	/**
	 * ���ͼƬ�鿴ͷ��
	 */
	@Click
	public void userAvatar() {
		if (mCurrentPhotoPath != null) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(new File(mCurrentPhotoPath)), "image/*");
			startActivity(intent);
		}
	}

	/**
	 * �ύͷ�����
	 */
	@Click
	public void updateAvatar() {
		if (mCurrentPhotoPath != null) {
			//
			new MyChangAvatarTask().execute();
		} else {
			Toast.makeText(this, "��ѡ��ͷ��", 0).show();
		}
	}

	/**
	 * ����ǩ������
	 */
	@Click
	public void updateUserInfo() {
		if (verifyUserInfo()) {
			new MyUpdateUserInfoTask().execute();
		}
	}

	/**
	 * ��֤�û���д���û���Ϣ
	 * 
	 * @return
	 */
	private boolean verifyUserInfo() {
		if (!NetUtil.canConnectNet()) {
			Toast.makeText(mContext,
					StringUtil.getString(R.string.fail_connect_server), 0)
					.show();
			return false;
		}
		int time = 0;
		if (!StringUtil.isEmpty(email.getText().toString().trim())) {
			time++;
		}
		if (!StringUtil.isEmpty(personNote.getText().toString().trim())) {
			time++;
		}
		if (time == 0) {
			Toast.makeText(mContext,
					StringUtil.getString(R.string.please_write_content), 0)
					.show();
			return false;
		}

		return true;
	}

	/**
	 * ��������
	 */
	@Click
	public void changPwd() {
		if (verifyPasswd()) {
			new MyChangPwdTask().execute();
		}
	}

	/**
	 * ��֤
	 * 
	 * @return
	 */
	private boolean verifyPasswd() {
		if (!NetUtil.canConnectNet()) {
			Toast.makeText(mContext,
					StringUtil.getString(R.string.fail_connect_server), 0)
					.show();
			return false;
		}
		if (StringUtil.isEmpty(newPasswd.getText().toString())
				|| StringUtil.isEmpty(newPasswd2.getText().toString())
				|| StringUtil.isEmpty(oldPasswd.getText().toString())) {
			Toast.makeText(this,
					StringUtil.getString(R.string.password_isempty), 0).show();
			return false;
		}
		if (oldPasswd.equals(userInfo.getUserPwd())) {
			Toast.makeText(this,
					StringUtil.getString(R.string.password_iserror), 0).show();
			return false;
		}
		if (!newPasswd.getText().toString()
				.equals(newPasswd2.getText().toString())) {
			Toast.makeText(this,
					StringUtil.getString(R.string.password_twice_isdifferent),
					0).show();
			return false;
		}
		return true;
	}

	/**
	 * ��������botton״̬
	 */
	public void lockButton(boolean flag) {
		updateUserInfo.setEnabled(flag);
		updateAvatar.setEnabled(flag);
		findImage.setEnabled(flag);
		takeImage.setEnabled(flag);
		changPwd.setEnabled(flag);
	}

	/**
	 * ���������̨�߳�
	 * 
	 * @author zuo
	 * 
	 */
	class MyChangPwdTask extends AsyncTask<Void, Integer, String> {

		@Override
		protected void onPreExecute() {
			showLoadingProgressDialog("��ȴ�...");
			lockButton(false);
		}

		@Override
		protected String doInBackground(Void... params) {
			return UserHelper.changePassword(newPasswd.getText().toString());
		}

		@Override
		protected void onPostExecute(String result) {
			dismissProgressDialog();
			lockButton(true);
			if (result == null) { // û�еõ���������Ҫ�������������쳣
				Toast.makeText(mContext,
						StringUtil.getString(R.string.fail_connect_server), 0)
						.show();
			} else { // ����������ж�
				AppLogger.i("�������뷵���ַ��������" + result);
				String jsonResult = JsonUtil.getJsonResult(result);
				AppLogger.i("json result :" + jsonResult);
				if ("false".equals(jsonResult)) { // ʧ��
					Toast.makeText(mContext, "���¼���ٸ�������~", 0).show();
				} else if ("true".equals(jsonResult)) {
					// ��������
					String userPwd = changPwd.getText().toString();
					UserInfo userInfo = UserInfoManager.getUserInfo();
					userInfo.setUserPwd(userPwd);
					UserInfoManager.saveUserInfo(userInfo);

					Toast.makeText(mContext, "��������ɹ�~", 0).show();
					finish();
				}
			}
		}
	}

	/**
	 * ����ͷ���̨�߳�
	 * 
	 * @author zuo
	 * 
	 */
	class MyChangAvatarTask extends AsyncTask<Void, Integer, String> {

		@Override
		protected void onPreExecute() {
			showLoadingProgressDialog("��ȴ�...");
			lockButton(false);
		}

		@Override
		protected String doInBackground(Void... params) {
			return UserHelper.changeAvatar(mCurrentPhotoPath);
		}

		@Override
		protected void onPostExecute(String result) {
			dismissProgressDialog();
			lockButton(true);
			if (result == null) { // û�еõ���������Ҫ�������������쳣
				Toast.makeText(mContext, "�޷����ӷ�����", 0).show();
			} else { // ����������ж�
				AppLogger.i("����ͷ�񷵻��ַ��������" + result);
				String jsonResult = JsonUtil.getJsonResult(result);
				AppLogger.i("json result :" + jsonResult);
				if ("false".equals(jsonResult)) { // ʧ��
					Toast.makeText(mContext, "���¼���ٸ���ͷ��~", 0).show();
				} else if ("true".equals(jsonResult)) {
					Toast.makeText(mContext, "����ͷ��ɹ�~", 0).show();
					// ������Ϣ
					userInfo = JsonUtil.getJsonUserInfo(result);
					UserInfoManager.saveUserInfo(userInfo);
					finish();
				}
			}
		}
	}

	/**
	 * ����ǩ�������̨�߳�
	 * 
	 * @author zuo
	 * 
	 */
	class MyUpdateUserInfoTask extends AsyncTask<Void, Integer, String> {

		@Override
		protected void onPreExecute() {
			showLoadingProgressDialog("��ȴ�...");
			lockButton(false);
		}

		@Override
		protected String doInBackground(Void... params) {
			Part[] parts = {
					new StringPart("email", email.getText().toString(), "UTF-8"),
					new StringPart("signature",
							personNote.getText().toString(), "UTF-8") };
			return UserHelper.changUserInfo(parts);
		}

		@Override
		protected void onPostExecute(String result) {
			dismissProgressDialog();
			lockButton(true);
			if (result == null) { // û�еõ���������Ҫ�������������쳣
				Toast.makeText(mContext, "�޷����ӷ�����", 0).show();
			} else { // ����������ж�
				AppLogger.i("�����û���Ϣ�����ַ��������" + result);
				try {
					String jsonResult = JsonUtil.getJsonResult(result);
					AppLogger.i("json result :" + jsonResult);
					if ("false".equals(jsonResult)) { // ʧ��
						Toast.makeText(mContext, "���¼���ٸ����û���Ϣ~", 0).show();
					} else if ("true".equals(jsonResult)) {
						Toast.makeText(mContext, "�����û���Ϣ�ɹ�~", 0).show();
						// ������Ϣ
						userInfo = JsonUtil.getJsonUserInfo(result);
						UserInfoManager.saveUserInfo(userInfo);
						finish();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
