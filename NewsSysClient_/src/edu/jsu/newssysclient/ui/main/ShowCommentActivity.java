package edu.jsu.newssysclient.ui.main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import com.nostra13.universalimageloader.core.ImageLoader;

import edu.jsu.newssysclient.R;
import edu.jsu.newssysclient.adapter.ShowCommentListAdapter;
import edu.jsu.newssysclient.bean.Follow;
import edu.jsu.newssysclient.bean.FollowList;
import edu.jsu.newssysclient.bean.UserInfo;
import edu.jsu.newssysclient.dao.cache.Cache;
import edu.jsu.newssysclient.debug.AppLogger;
import edu.jsu.newssysclient.util.local.StringUtil;
import edu.jsu.newssysclient.util.local.UserInfoManager;
import edu.jsu.newssysclient.util.net.MyApi;
import edu.jsu.newssysclient.util.net.NetUtil;
import edu.jsu.newssysclient.util.net.NewsSysApi;
import edu.jsu.newssysclient.util.net.UserHelper;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract.Data;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

/**
 * ʱ�䣺
 * 2014��6��2��11:39:06
 * ���ܣ�
 * 1����ʾ���ۣ���������id������id��ѯ���ݲ���ʾ��done
 * 2���ύ���ۣ��ύ����ʾ�Լ��������ڵ�һ�������ϴ����ݵ���������
 * @author zuo
 *
 */
public class ShowCommentActivity extends MyBaseActivity {
	
	private String mNewsID;
	private Cache cache = new Cache();
	private List<Follow> datas = new ArrayList<Follow>();
	private ShowCommentListAdapter mAdapter;
	private ImageLoader imageLoader;
	
	// ��ͼ�ؼ�
	public ListView mList;
	private ImageButton mWriteComment;
	private EditText mCommentContent;
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			mNewsID = savedInstanceState.getString(ShowNewsActivity.SHOWNEWSACTIVITYNEWSID);
			AppLogger.i("On ShowCommentActivity News id: " + mNewsID);
		}
		
		// ��ȡ����id
		if (getIntent() != null) {
			mNewsID = getIntent().getStringExtra(ShowNewsActivity.SHOWNEWSACTIVITYNEWSID);
		}
		AppLogger.i("���۽���õ�������ID: " + mNewsID);
		
		setContentView(R.layout.show_comment_layout);
		
		mWriteComment = (ImageButton) findViewById(R.id.ib_comment_send);
		mCommentContent = (EditText) findViewById(R.id.et_comment_content);
		// ���õ���¼�
		mWriteComment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// ��֤�û�
				final UserInfo userInfo = UserInfoManager.getUserInfo();
				if (userInfo == null) {
					Toast.makeText(mContext, "���ȵ�¼~", 0).show();
					return ;
				}
				// �õ�����
				final String content = mCommentContent.getText().toString().trim();
				if (StringUtil.isEmpty(content)) {
					Toast.makeText(mContext, "���������ǿյ�", 0).show();
					return ;
				}
				// ��װ����
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				String time = format.format(new Date(System.currentTimeMillis()));
				Follow f = new Follow();
				f.setFollowcontent(content);
				f.setFollowuser(userInfo.getUserName());
				f.setFollowtime(time);
				f.setFollowuserhead(userInfo.getPtoShowPath());
				// ���½���
				datas.add(0, f);
				if (mAdapter != null) mAdapter.notifyDataSetChanged();
				// �ϴ����۵�������
				new AsyncTask<Void, Integer, String> (){

					@Override
					protected String doInBackground(Void... params) {
						UserHelper.login(userInfo.getUserName(), userInfo.getUserPwd());
						return UserHelper.addComment(content, mNewsID);
					}
					
					@Override
					protected void onPostExecute(String result) {
						if (result == null) {
							AppLogger.i("������ϸ���棬������ۣ��޷��õ���������Ӧ");
						}else {
							JSONObject jo;
							try {
								jo = new JSONObject(result);
								String jsonRes = jo.getString("result");
								AppLogger.i("������ϸ���棬������۽����" + jsonRes);
								if ("true".equals(jsonRes)) {
									Toast.makeText(mContext, "����ɹ�", 0).show();
									mCommentContent.setText("");
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}.execute();
			}
		});
		
		mList = (ListView) findViewById(R.id.lv_comment_list);
		
		mAdapter = new ShowCommentListAdapter(datas);
		imageLoader = mAdapter.getImageLoader();
		
		mList.setAdapter(mAdapter);
		
		// ��ȡ��������
		if (NetUtil.canConnectNet()) {
			// �������ȡ����
			task.execute("");
		} else {
			getDataFromCache();
		}
	}
	
	// û��������û�л����ʱ����ʾ�û�û����������ʾ�������û����������ٴδӷ����������ݣ������½���
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (imageLoader != null) {
			imageLoader.clearMemoryCache();
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putString(ShowNewsActivity.SHOWNEWSACTIVITYNEWSID, mNewsID);
	}

	/**
	 * �ӻ���������
	 */
	private void getDataFromCache() {
		AppLogger.i("��ȡ��������");
		FollowList list = cache.get(Cache.NEWSCOMMENTLISTBYNEWSID + mNewsID, FollowList.class);
		if (list != null && list.getFollows() != null && list.getFollows().size() != 0) {
			// �������
			for (Follow f : list.getFollows()) {
				datas.add(f);
			}
			// ���½���
			updateUI();
		}
	}
	
	/**
	 * ���ݸı���½���
	 */
	private void updateUI() {
		if (mAdapter != null) {
			mAdapter.notifyDataSetChanged();
		}
	}
	
	/**
	 * �������������̣߳����û���õ����ٴ��û�������
	 */
	AsyncTask<String, Integer, FollowList> task = new AsyncTask<String, Integer, FollowList>(){

		@Override
		protected FollowList doInBackground(String... params) {
			// ��ȡ����
			MyApi api = new NewsSysApi();
			FollowList list = null;
			try {
				list = api.getNewsCommentList(mNewsID);
			} catch (Exception e) {
				// TODO: handle exception
			}
			return list;
		}
		
		protected void onPostExecute(FollowList apiList) {
			// �ж��Ƿ�������
			if (apiList != null && apiList.getFollows() != null && apiList.getFollows().size() != 0) {
				AppLogger.i("�����ȡ�����б�ɹ�");
				// �������
				for (Follow f : apiList.getFollows()){
					datas.add(f);
				}
				// ��������
				cache.put(Cache.NEWSCOMMENTLISTBYNEWSID + mNewsID, apiList);
				// ���½���
				updateUI();
			} else {
				AppLogger.i("�����ȡ�����б�ʧ��");
				// �û���
				getDataFromCache();
			}
		};
	};
	
}
