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
 * ע�����
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
		// û��������ܣ�ʹ���հ�ť���ɼ�
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
		case SELECT_PIC:	// �ɷ�ʽ��ͼ��ͼ��ѡ����Ƭ
		case SELECT_PIC_KITKAT:
            //��Ƭ��ԭʼ��Դ��ַ
            uri = data.getData();
            mCurrentPhotoPath = UserViewUtil.getPath(this, uri);
            AppLogger.i("ѡ���ͼƬ·����" + mCurrentPhotoPath);
            UserViewUtil.setPic(userAvatar, mCurrentPhotoPath);
			delete.setVisibility(View.VISIBLE);
			break;
			
		case REQUEST_TAKE_PHOTO: 	// ���ղ�����·��
			AppLogger.i("ע����棬����ͼƬ·����" + mCurrentPhotoPath);
			UserViewUtil.setPic(userAvatar, mCurrentPhotoPath);
			delete.setVisibility(View.VISIBLE);
			break;

		default:
			break;
		}
	}
	
	@Click
	public void delete() {
		if (mCurrentPhotoPath != null) {	//  ��ͼƬ
			userAvatar.setImageResource(R.drawable.ic_add_image);
			delete.setVisibility(View.INVISIBLE);
			mCurrentPhotoPath = null;
		}
	}
	
	@Click
	public void userAvatar() {
		if (mCurrentPhotoPath != null) {	// ��ͼƬ�鿴ͼƬ
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(new File(mCurrentPhotoPath)), "image/*");
			startActivity(intent);
		} else {	// ��ȡһ��ͼƬ 
			new AlertDialog.Builder(this)
			.setItems(new String[]{"����", "ͼ��"}, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case 0:	// ����
						takePicture();
						break;
					case 1:	// ����ͼ��
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
	 * ����
	 */
	private void takePicture() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // ����ϵͳ���
		File file = new File(Environment.getExternalStorageDirectory(), "image.jpg");
		mCurrentPhotoPath = file.getPath();
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
	 * ע��
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
		// ��֤������Ϣ�����룬���䣬ͼƬ��ַ������ǩ��
		if (verifyUserInfo()) {
			// ���ӷ��������������
			String url = URLHelper.REGISTERURL;
			new MyRegisterTask().execute(url);
		} else {
			return ;
		}
	}
	
	/**
	 * ��֤��Ϣ
	 * @return true �ɹ���false ʧ��
	 */
	private boolean verifyUserInfo() {
		if (StringUtil.isEmpty(username.getText().toString())) {
			Toast.makeText(this, "�û�������Ϊ��", 0).show();
			// �Զ���λ�������
			
			return false;
		}
		if (StringUtil.isEmpty(passwd.getText().toString())) {
			Toast.makeText(this, "���벻��Ϊ��", 0).show();
			return false;
		}
		if (StringUtil.isEmpty(passwd2.getText().toString())) {
			Toast.makeText(this, "ȷ�����벻��Ϊ��", 0).show();
			return false;
		}
		// ����
		if (!passwd.getText().toString().equals(passwd2.getText().toString())) {
			Toast.makeText(this, "�������벻ͬ", 0).show();
			return false;
		}
		// ��֤����
		if (!StringUtil.isEmpty(email.getText().toString()) && !StringUtil.isEmailAddress(email.getText().toString())) {
			Toast.makeText(this, "�����ַ����~", 0).show();
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
		case R.id.action_sure:	// �ύע����Ϣ
			register();
			break;

		default:
			break;
		}
		return true;
	}
	
	/**
	 * �ύע����Ϣ��������
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
				AppLogger.i("ע����棬���ӷ�����FileNotFoundException");
				e.printStackTrace();
			} catch (Exception e) {
				AppLogger.i("ע����棬���ӷ�����Exception");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			dismissProgressDialog();
			if (result == null) {	// û�еõ���������Ҫ�������������쳣
				Toast.makeText(RegisterActivity.this, "�޷����ӷ�����", 0).show();
			} else {	// ����������ж�
				AppLogger.i("ע������" + result);
				String jsonResult = JsonUtil.getJsonResult(result);
				AppLogger.i("json result :" + jsonResult);
				if ("false".equals(jsonResult)) {	// ʧ��
					Toast.makeText(RegisterActivity.this, "�û�����ͬ��������", 0).show();
					// �����û����༭��
					
				} else if ("true".equals(jsonResult)){
					Toast.makeText(RegisterActivity.this, "ע��ɹ�", 0).show();
					UserInfo userInfo = JsonUtil.getJsonUserInfo(result);
					// �����û���Ϣ
					UserInfoManager.saveUserInfo(userInfo);
					UserCollectManager.cacheUserCollectFromNet();
					// �ر�ע��ҳ��
					RegisterActivity.this.finish();
				}
			}
		}
	}
}
