package edu.jsu.newssysclient.ui.menu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import edu.jsu.newssysclient.R;
import edu.jsu.newssysclient.URLHelper;
import edu.jsu.newssysclient.bean.UserInfo;
import edu.jsu.newssysclient.dao.cache.Cache;
import edu.jsu.newssysclient.debug.AppLogger;
import edu.jsu.newssysclient.ui.main.MyBaseActivity;
import edu.jsu.newssysclient.ui.util.UserViewUtil;
import edu.jsu.newssysclient.util.local.JsonUtil;
import edu.jsu.newssysclient.util.local.StringUtil;
import edu.jsu.newssysclient.util.local.UserCollectManager;
import edu.jsu.newssysclient.util.local.UserInfoManager;
import edu.jsu.newssysclient.util.net.UserHelper;

/**
 * 注册界面
 * @author zuo
 *
 */
@EActivity(R.layout.register_layout)
public class RegisterActivity extends MyBaseActivity {
	static final int REQUEST_TAKE_PHOTO = 1;
	private static final int SELECT_PIC_KITKAT = 10;
	private static final int SELECT_PIC = 20;
	private String mCurrentPhotoPath = null;

	@ViewById
	EditText username;
	@ViewById
	EditText passwd;
	@ViewById
	EditText passwd2;
	@ViewById
	EditText email;
	@ViewById
	EditText personNote;
	@ViewById
	ImageView userAvatar;
	@ViewById
	ImageView delete;
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		// 没有相机功能，使拍照按钮不可见
		if (!this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			//takeImage.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode != RESULT_OK) {
			return;
		}
		ContentResolver resolver = getContentResolver();
		Uri uri = null;
		switch (requestCode) {
		case SELECT_PIC:	// 旧方式从图库图库选择相片
		case SELECT_PIC_KITKAT:
            //照片的原始资源地址
            uri = data.getData();
            mCurrentPhotoPath = UserViewUtil.getPath(this, uri);
            AppLogger.i("选择的图片路径：" + mCurrentPhotoPath);
            UserViewUtil.setPic(userAvatar, mCurrentPhotoPath);
			delete.setVisibility(View.VISIBLE);
			break;
			
		case REQUEST_TAKE_PHOTO: 	// 拍照并保持路径
			AppLogger.i("注册界面，拍照图片路径：" + mCurrentPhotoPath);
			UserViewUtil.setPic(userAvatar, mCurrentPhotoPath);
			delete.setVisibility(View.VISIBLE);
			break;

		default:
			break;
		}
	}
	
	@Click
	public void delete() {
		if (mCurrentPhotoPath != null) {	//  有图片
			userAvatar.setImageResource(R.drawable.ic_add_image);
			delete.setVisibility(View.INVISIBLE);
			mCurrentPhotoPath = null;
		}
	}
	
	@Click
	public void userAvatar() {
		if (mCurrentPhotoPath != null) {	// 有图片查看图片
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(new File(mCurrentPhotoPath)), "image/*");
			startActivity(intent);
		} else {	// 获取一个图片 
			new AlertDialog.Builder(this)
			.setItems(new String[]{"拍照", "图库"}, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case 0:	// 拍照
						takePicture();
						break;
					case 1:	// 查找图库
						findePicture();
						break;

					default:
						break;
					}
				}
			})
			.show();
		}
	}

	/** 
	 * 拍照
	 */
	private void takePicture() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // 调用系统相机
		File file = new File(Environment.getExternalStorageDirectory(), "image.jpg");
		mCurrentPhotoPath = file.getPath();
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
	private void findePicture() {
		Intent intent=new Intent(Intent.ACTION_GET_CONTENT);//ACTION_OPEN_DOCUMENT  
		intent.addCategory(Intent.CATEGORY_OPENABLE);  
		intent.setType("image/jpeg");  
		if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.KITKAT){                  
		        startActivityForResult(intent, SELECT_PIC_KITKAT);    
		}else{                
		        startActivityForResult(intent, SELECT_PIC);   
		}
	}

	/**
	 * 注册
	 */
	public void register() {
		/*StringBuilder str = new StringBuilder("");
		str.append("name:" + username.getText().toString() + "\n")
				.append("pwd:" + passwd.getText().toString() + "\n")
				.append("pwd2:" + passwd2.getText().toString() + "\n")
				.append("email:" + email.getText().toString() + "\n")
				.append("person note:" + personNote.getText().toString() + "\n")
				.append("image path:" + mCurrentPhotoPath);
		Toast.makeText(this, str.toString(), 0).show();*/
		// 验证输入信息，密码，邮箱，图片地址，个性签名
		if (verifyUserInfo()) {
			// 连接服务器，解析结果
			String url = URLHelper.REGISTERURL;
			new MyRegisterTask().execute(url);
		} else {
			return ;
		}
	}
	
	/**
	 * 验证信息
	 * @return true 成功，false 失败
	 */
	private boolean verifyUserInfo() {
		if (StringUtil.isEmpty(username.getText().toString())) {
			Toast.makeText(this, "用户名不能为空", 0).show();
			// 自动地位到输入框
			
			return false;
		}
		if (StringUtil.isEmpty(passwd.getText().toString())) {
			Toast.makeText(this, "密码不能为空", 0).show();
			return false;
		}
		if (StringUtil.isEmpty(passwd2.getText().toString())) {
			Toast.makeText(this, "确认密码不能为空", 0).show();
			return false;
		}
		// 密码
		if (!passwd.getText().toString().equals(passwd2.getText().toString())) {
			Toast.makeText(this, "两次密码不同", 0).show();
			return false;
		}
		// 验证邮箱
		if (!StringUtil.isEmpty(email.getText().toString()) && !StringUtil.isEmailAddress(email.getText().toString())) {
			Toast.makeText(this, "邮箱地址错误~", 0).show();
			return false;
		}
		
		return true;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.register_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_sure:	// 提交注册信息
			register();
			break;

		default:
			break;
		}
		return true;
	}
	
	/**
	 * 提交注册信息到服务器
	 * @author zuo
	 *
	 */
	class MyRegisterTask extends AsyncTask<String, Integer, String> {

		protected void onPreExecute() {
			showLoadingProgressDialog();
		};

		@Override
		protected String doInBackground(String... params) {
			try {
				if (mCurrentPhotoPath != null) {
					Part[] parts = { new StringPart("userName", username.getText().toString(), "UTF-8"),
							new StringPart("userPwd", passwd.getText().toString(), "UTF-8"),
							new StringPart("email", email.getText().toString(), "UTF-8"),
							new FilePart("upload", new File(mCurrentPhotoPath), null, "UTF-8"),
							new StringPart("signature", personNote.getText().toString(), "UTF-8") };
					return UserHelper.postForm(params[0], parts);
				}else{
					Part[] parts = { new StringPart("userName", username.getText().toString(), "UTF-8"),
							new StringPart("userPwd", passwd.getText().toString(), "UTF-8"),
							new StringPart("email", email.getText().toString(), "UTF-8"),
							new StringPart("signature", personNote.getText().toString(), "UTF-8") };
					return UserHelper.postForm(params[0], parts);
				}
			} catch (FileNotFoundException e) {
				AppLogger.i("注册界面，连接服务器FileNotFoundException");
				e.printStackTrace();
			} catch (Exception e) {
				AppLogger.i("注册界面，连接服务器Exception");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			dismissProgressDialog();
			if (result == null) {	// 没有得到服务器需要或者连接网络异常
				Toast.makeText(RegisterActivity.this, "无法连接服务器", 0).show();
			} else {	// 解析结果并判断
				AppLogger.i("注册结果：" + result);
				String jsonResult = JsonUtil.getJsonResult(result);
				AppLogger.i("json result :" + jsonResult);
				if ("false".equals(jsonResult)) {	// 失败
					Toast.makeText(RegisterActivity.this, "用户名相同，请重试", 0).show();
					// 跳到用户名编辑框
					
				} else if ("true".equals(jsonResult)){
					Toast.makeText(RegisterActivity.this, "注册成功", 0).show();
					UserInfo userInfo = JsonUtil.getJsonUserInfo(result);
					// 保存用户信息
					UserInfoManager.saveUserInfo(userInfo);
					UserCollectManager.cacheUserCollectFromNet();
					// 关闭注册页面
					RegisterActivity.this.finish();
				}
			}
		}
	}
}
