package edu.jsu.newssysclient.ui.menu;

import java.io.File;
import java.util.List;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.json.JSONObject;

import edu.jsu.newssysclient.R;
import edu.jsu.newssysclient.URLHelper;
import edu.jsu.newssysclient.bean.Item;
import edu.jsu.newssysclient.bean.ItemList;
import edu.jsu.newssysclient.bean.UserInfo;
import edu.jsu.newssysclient.dao.cache.Cache;
import edu.jsu.newssysclient.dao.cache.CacheApi;
import edu.jsu.newssysclient.debug.AppLogger;
import edu.jsu.newssysclient.ui.main.AllTypeNewsActivity;
import edu.jsu.newssysclient.ui.main.MyBaseActivity;
import edu.jsu.newssysclient.ui.util.ImageViewUtil;
import edu.jsu.newssysclient.ui.util.UserViewUtil;
import edu.jsu.newssysclient.util.local.IntentUtil;
import edu.jsu.newssysclient.util.local.StringUtil;
import edu.jsu.newssysclient.util.local.UserCollectManager;
import edu.jsu.newssysclient.util.local.UserCommentManager;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * �û���Ϣ����
 * 
 * @author zuo
 * 
 */
@EActivity(R.layout.show_userinfo_layout2)
public class ShowUserInfoActivity extends MyBaseActivity {
	private UserInfo mUserInfo;
	private CacheApi mCache = new Cache(); // ����
	private String mCollectCachekey;

	@ViewById
	ImageView userAvatar;
	@ViewById
	TextView mName;
	@ViewById
	TextView userSignature;
	@ViewById(R.id.tv_user_email)
	TextView mEmail;
	@ViewById(R.id.tv_user_comment_time)
	TextView mCommentTime;
	@ViewById(R.id.tv_user_collect_time)
	TextView mCollectTime;

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		mUserInfo = UserInfoManager.getUserInfo();
		if (mUserInfo != null) {
			mName.setText(mUserInfo.getUserName());
			// ����ͷ��
			ImageViewUtil.loadImage(userAvatar,
					URLHelper.SERVERURL + mUserInfo.getPtoShowPath());
			// �����ǩ��
			if (mUserInfo.getSignature() != null)
				userSignature.setText(mUserInfo.getSignature());
			if (mUserInfo.getEmail() != null)
				mEmail.setText(mUserInfo.getEmail());
			// ��ȡ�û��ղ���������
			//mCollectCachekey = Cache.USERCOLLECTLISTBYUSERID + mUserInfo.getUserID();
//			ItemList cacheList = mCache.get(mCollectCachekey, ItemList.class);
			ItemList cacheList = UserCollectManager.getUserCollectFromCache();
			if (cacheList != null && cacheList.getItems() != null) {
				mCollectTime.setText(cacheList.getItems().size() + "���ղ�");
			}
			// ��ȡ������������
			ItemList itemList = UserCommentManager.getUserCommentListFromCache(""+mUserInfo.getUserID());
			if (itemList != null && itemList.getItems() != null) {
				mCommentTime.setText(itemList.getItems().size() + "������");
			}
		}
	}

	/**
	 * ��������û�ǩ��������Ϣ����
	 */
	@Click
	public void mChangeInfo() {
		if (verify()) {
			IntentUtil.go2ChangeUserInfo(mContext,
					ChangeUserInfoActivity.USER_MODE_SIGNATUREEMAIL);
		}
	}

	private boolean verify() {
		if (!NetUtil.canConnectNet()) {
			Toast.makeText(mContext,
					StringUtil.getString(R.string.fail_connect_server), 0)
					.show();
			return false;
		}
		return true;
	}

	/**
	 * ��������û�ͷ�����
	 */
	@Click
	public void mChangeAvatar() {
		if (verify()) {
			IntentUtil.go2ChangeUserInfo(mContext,
					ChangeUserInfoActivity.USER_MODE_AVATAR);
		}
	}

	/**
	 * ��������û��������
	 */
	@Click
	public void mChangePwd() {
		if (verify()) {
			IntentUtil.go2ChangeUserInfo(mContext,
					ChangeUserInfoActivity.USER_MODE_PASSWORD);
		}
	}

	/**
	 * �û�ע��
	 */
	@Click
	public void mLogout() {
		Toast.makeText(this, "ע���ɹ�", 0).show();
		// ɾ���û���Ϣ
		UserInfoManager.delUserInfo(mUserInfo.getUserID());
		// ��������
		IntentUtil.go2AllTypeNews(mContext);
	}
}
