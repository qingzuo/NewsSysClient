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
 * 用户信息更改界面 更改用户头像： 更改用户密码： 更改用户个性签名，邮箱
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
	private String mCurrentPhotoPath = null; // 头像

	private UserInfo userInfo;

	// 更改密码
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

	// 更改头像
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

	// 更改签名邮箱
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
		case USER_MODE_PASSWORD: // 显示更改密码功能
			ll_change_pwd.setVisibility(View.VISIBLE);
			ll_change_signature_mail.setVisibility(View.GONE);
			ll_change_avatar.setVisibility(View.GONE);
			break;
			
		case USER_MODE_AVATAR: // 显示更改头像功能
			ll_change_signature_mail.setVisibility(View.GONE);
			ll_change_avatar.setVisibility(View.VISIBLE);
			ll_change_pwd.setVisibility(View.GONE);
			if (userInfo != null && mCurrentPhotoPath == null)
				ImageViewUtil.loadImage(userAvatar, URLHelper.SERVERURL+userInfo.getPtoShowPath());
			break;
			
		case USER_MODE_SIGNATUREEMAIL: // 显示更改签名邮箱功能
			ll_change_signature_mail.setVisibility(View.VISIBLE);
			ll_change_avatar.setVisibility(View.GONE);
			ll_change_pwd.setVisibility(View.GONE);
			email.setText(userInfo.getEmail());
			personNote.setText(userInfo.getSignature());
			break;

		default:	// 默认显示更改签名邮箱功能
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
		case SELECT_PIC: // 旧方式从图库图库选择相片
		case SELECT_PIC_KITKAT:
			// 照片的原始资源地址
			Uri uri = data.getData();
			mCurrentPhotoPath = UserViewUtil.getPath(this, uri);
			AppLogger.i("选择的图片路径：" + mCurrentPhotoPath);
			UserViewUtil.setPic(userAvatar, mCurrentPhotoPath);
			break;

		case REQUEST_TAKE_PHOTO: // 拍照并保持路径
			UserViewUtil.setPic(userAvatar, mCurrentPhotoPath);
			break;

		default:
			break;
		}
	}

	/**
	 * 拍照
	 */
	private void takePictrue() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // 调用系统相机
		File file = new File(Environment.getExternalStorageDirectory(),
				"image.jpg");
		mCurrentPhotoPath = file.getPath();
		AppLogger.i("注册界面，图片路径：" + file.getPath());
		Uri imageUri = Uri.fromFile(file);
		// 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		if (intent.resolveActivity(getPackageManager()) != null) {
			startActivityForResult(intent, REQUEST_TAKE_PHOTO); // 用户点击了从相机获取
		}
	}

	/**
	 * 选择图片
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
	 * 拍照按钮点击事件
	 */
	@Click
	public void takeImage() {
		AppLogger.i("注册界面，拍照");
		takePictrue();
	}

	/**
	 * 查找图片点击事件
	 */
	@Click
	public void findImage() {
		AppLogger.i("注册界面，查找本地图片");
		selectPhoto();
	}

	/**
	 * 点击图片查看头像
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
	 * 提交头像更改
	 */
	@Click
	public void updateAvatar() {
		if (mCurrentPhotoPath != null) {
			//
			new MyChangAvatarTask().execute();
		} else {
			Toast.makeText(this, "请选择头像", 0).show();
		}
	}

	/**
	 * 更改签名邮箱
	 */
	@Click
	public void updateUserInfo() {
		if (verifyUserInfo()) {
			new MyUpdateUserInfoTask().execute();
		}
	}

	/**
	 * 验证用户填写的用户信息
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
	 * 更改密码
	 */
	@Click
	public void changPwd() {
		if (verifyPasswd()) {
			new MyChangPwdTask().execute();
		}
	}

	/**
	 * 验证
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
	 * 控制所有botton状态
	 */
	public void lockButton(boolean flag) {
		updateUserInfo.setEnabled(flag);
		updateAvatar.setEnabled(flag);
		findImage.setEnabled(flag);
		takeImage.setEnabled(flag);
		changPwd.setEnabled(flag);
	}

	/**
	 * 更改密码后台线程
	 * 
	 * @author zuo
	 * 
	 */
	class MyChangPwdTask extends AsyncTask<Void, Integer, String> {

		@Override
		protected void onPreExecute() {
			showLoadingProgressDialog("请等待...");
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
			if (result == null) { // 没有得到服务器需要或者连接网络异常
				Toast.makeText(mContext,
						StringUtil.getString(R.string.fail_connect_server), 0)
						.show();
			} else { // 解析结果并判断
				AppLogger.i("更改密码返回字符串结果：" + result);
				String jsonResult = JsonUtil.getJsonResult(result);
				AppLogger.i("json result :" + jsonResult);
				if ("false".equals(jsonResult)) { // 失败
					Toast.makeText(mContext, "请登录，再更改密码~", 0).show();
				} else if ("true".equals(jsonResult)) {
					// 缓存密码
					String userPwd = changPwd.getText().toString();
					UserInfo userInfo = UserInfoManager.getUserInfo();
					userInfo.setUserPwd(userPwd);
					UserInfoManager.saveUserInfo(userInfo);

					Toast.makeText(mContext, "更改密码成功~", 0).show();
					finish();
				}
			}
		}
	}

	/**
	 * 更改头像后台线程
	 * 
	 * @author zuo
	 * 
	 */
	class MyChangAvatarTask extends AsyncTask<Void, Integer, String> {

		@Override
		protected void onPreExecute() {
			showLoadingProgressDialog("请等待...");
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
			if (result == null) { // 没有得到服务器需要或者连接网络异常
				Toast.makeText(mContext, "无法连接服务器", 0).show();
			} else { // 解析结果并判断
				AppLogger.i("更改头像返回字符串结果：" + result);
				String jsonResult = JsonUtil.getJsonResult(result);
				AppLogger.i("json result :" + jsonResult);
				if ("false".equals(jsonResult)) { // 失败
					Toast.makeText(mContext, "请登录，再更改头像~", 0).show();
				} else if ("true".equals(jsonResult)) {
					Toast.makeText(mContext, "更改头像成功~", 0).show();
					// 更新信息
					userInfo = JsonUtil.getJsonUserInfo(result);
					UserInfoManager.saveUserInfo(userInfo);
					finish();
				}
			}
		}
	}

	/**
	 * 更改签名邮箱后台线程
	 * 
	 * @author zuo
	 * 
	 */
	class MyUpdateUserInfoTask extends AsyncTask<Void, Integer, String> {

		@Override
		protected void onPreExecute() {
			showLoadingProgressDialog("请等待...");
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
			if (result == null) { // 没有得到服务器需要或者连接网络异常
				Toast.makeText(mContext, "无法连接服务器", 0).show();
			} else { // 解析结果并判断
				AppLogger.i("更改用户信息返回字符串结果：" + result);
				try {
					String jsonResult = JsonUtil.getJsonResult(result);
					AppLogger.i("json result :" + jsonResult);
					if ("false".equals(jsonResult)) { // 失败
						Toast.makeText(mContext, "请登录，再更改用户信息~", 0).show();
					} else if ("true".equals(jsonResult)) {
						Toast.makeText(mContext, "更改用户信息成功~", 0).show();
						// 更新信息
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
