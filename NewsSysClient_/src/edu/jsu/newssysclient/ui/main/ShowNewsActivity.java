package edu.jsu.newssysclient.ui.main;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import de.greenrobot.event.EventBus;
import edu.jsu.newssysclient.MyApplication;
import edu.jsu.newssysclient.R;
import edu.jsu.newssysclient.R.id;
import edu.jsu.newssysclient.R.layout;
import edu.jsu.newssysclient.R.menu;
import edu.jsu.newssysclient.R.string;
import edu.jsu.newssysclient.URLHelper;
import edu.jsu.newssysclient.bean.Follow;
import edu.jsu.newssysclient.bean.NewsContent;
import edu.jsu.newssysclient.bean.NewsImage;
import edu.jsu.newssysclient.bean.NewsInfo;
import edu.jsu.newssysclient.bean.NewsLead;
import edu.jsu.newssysclient.bean.NewsLink;
import edu.jsu.newssysclient.bean.NewsText;
import edu.jsu.newssysclient.bean.NewsTimeSource;
import edu.jsu.newssysclient.bean.NewsTitle;
import edu.jsu.newssysclient.bean.NewsVideo;
import edu.jsu.newssysclient.bean.UserInfo;
import edu.jsu.newssysclient.dao.cache.Cache;
import edu.jsu.newssysclient.debug.AppLogger;
import edu.jsu.newssysclient.ui.menu.LoginActivity_;
import edu.jsu.newssysclient.ui.util.ViewUtil;
import edu.jsu.newssysclient.util.local.IntentUtil;
import edu.jsu.newssysclient.util.local.JsonUtil;
import edu.jsu.newssysclient.util.local.StringUtil;
import edu.jsu.newssysclient.util.local.UserInfoManager;
import edu.jsu.newssysclient.util.net.IOStream;
import edu.jsu.newssysclient.util.net.NetUtil;
import edu.jsu.newssysclient.util.net.UserHelper;
import edu.jsu.newssysclient.util.net.XmlHelper;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.LeadingMarginSpan.Standard;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ������ϸ��Ϣ����
 * ��Ҫ���ܣ�
 * 		��ʾ���ţ��ӷ�������ȡxml���ݽ����󲼾ֽ���
 * �û����¼���ܣ�
 * 		�ղع��ܣ�debug���������ղ�״̬
 * 		���۹��ܣ�
 * ����Ҫ��¼���ܣ�
 * 		�����ܣ�
 * @author zuo
 *
 */
public class ShowNewsActivity extends MyBaseActivity implements OnClickListener {
	public static final String SHOWNEWSACTIVITYNEWSID = "edu.jsu.newssysclient.ui.main.ShowANewsActivity.newsid";
	private NewsInfo mNewsInfo;	// ������ϸ��Ϣ
	private String mNewsId;	// ����id
	private UserInfo userInfo;	// �û���Ϣ
	
	// �������
	private Cache cache = new Cache();
	private String mNewsInfoCacheKey;
	private String mCollectFlagCachekey;	// �����ղ���Ϣ��key
	private int COLLECT = 1;	// �Ѿ��ղ�
	private int NOCOLLECT = 0;	// δ�ղ�
	private int collectState = NOCOLLECT;

	// ����Ԫ��
	private LinearLayout myView;	// ����������ϸ����
	private LinearLayout newsBox;	// ����������ſؼ�������
	private LinearLayout bottomComment;		// �ײ����۵Ľ���
	private TextView commentUserName;
	private TextView commentTime;
	private TextView commentContent;
	private Button commentMore;
	private EditText mCommentContent;
	private Button mWriteComment;
	
	TextView mNewsContent;	// ������������
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null){
			mNewsId = savedInstanceState.getString(SHOWNEWSACTIVITYNEWSID);
		}
		
		// ��ȡ����id
		if (getIntent() != null) {
			mNewsId = getIntent().getStringExtra(SHOWNEWSACTIVITYNEWSID);
		}
		AppLogger.i("������ϸ��Ϣ����õ�����ID: " + mNewsId);
		userInfo = UserInfoManager.getUserInfo();
		// ��ʼ������key
		mNewsInfoCacheKey = Cache.NEWSINFOBYNEWSID+mNewsId;
		if (userInfo != null) {
			mCollectFlagCachekey = Cache.USERCOLLECTFLAGBYNEWSIDANDUSERID + "-newsid" + mNewsId + "-userid" + userInfo.getUserID();
		}
		
		// ��ʼ������
		setContentView(R.layout.show_news_layout);
		initView();
		
		// ��ȡnet����
		getDataFromNet();
		// ��ȡ����
		getDataFromCache();
	}

	/**
	 * ��ȡ���ݣ��������ȡ
	 */
	private void getDataFromNet() {
		// ��ȡ��������
		new GetNewsFromeNetTask().execute(URLHelper.ANEWSURL + mNewsId);
	}

	/**
	 * �����ȡ����
	 */
	private void getDataFromCache() {
		NewsInfo tt = cache.get(mNewsInfoCacheKey, NewsInfo.class);
		if (tt != null) {
			mNewsInfo = tt;
			AppLogger.i("������ϸ���棬ʹ�û�������");
			// ʹ�û�����½���
			update(mNewsInfo);
			dismissProgressDialog();
		}
	}

	/**
	 * ��ʼ���ؼ�
	 */
	private void initView() {
		myView = (LinearLayout) findViewById(R.id.ll_show_news);
		//myView.setVisibility(View.GONE);
		newsBox = (LinearLayout) findViewById(R.id.news_box);
		bottomComment = (LinearLayout) findViewById(R.id.ll_news_bottom);
		bottomComment.setVisibility(View.GONE);
		commentUserName = (TextView) findViewById(R.id.tv_follow_username);
		commentTime = (TextView) findViewById(R.id.tv_follow_time);
		commentContent = (TextView) findViewById(R.id.tv_follow_content);
		commentMore = (Button) findViewById(R.id.bt_follow_showmore);
		mCommentContent = (EditText) findViewById(R.id.et_comment_content);
		mWriteComment = (Button) findViewById(R.id.ib_comment_send);
		
		mWriteComment.setOnClickListener(this);
		commentMore.setOnClickListener(this);
	}
	
	/**
	 * ��̨�������
	 * @author zuo
	 *
	 */
	class AddCommentToServer extends AsyncTask<Void, Integer, String>{
		private Context context;
		private String content;
		private String newsId;
		
		public AddCommentToServer(Context mContext, String content, String mNewsId){
			this.context = mContext;
			this.content = content;
			this.newsId = mNewsId;
		}
		
		protected void onPreExecute() {
			lockButtom();
		};

		@Override
		protected String doInBackground(Void... params) {
			// UserHelper.login(userInfo.getUserName(), userInfo.getUserPwd());
			return UserHelper.addComment(this.content, this.newsId);
		}
		
		@Override
		protected void onPostExecute(String result) {
			unLockButtom();
			if (result == null) {
				Toast.makeText(this.context, StringUtil.getString(R.string.fail_connect_server), 0).show();
				AppLogger.i("������ϸ���棬������ۣ��޷��õ���������Ӧ");
			}else {
				String jsonRes = JsonUtil.getJsonResult(result);
				AppLogger.i("������ϸ���棬������۽����" + jsonRes);
				if ("true".equals(jsonRes)) {
					// ���½���
					fillCommentView(userInfo.getUserName(), "�ո�", content);
					Toast.makeText(this.context, "����ɹ�", 0).show();
					mCommentContent.setText("");
				} else {
					// �����Ǽǽ���
					IntentUtil.go2Login(this.context);
				}
			}
		}
		
	}
	
	/**
	 * ������ť
	 */
	protected void unLockButtom() {
		mWriteComment.setEnabled(true);
		commentMore.setEnabled(true);
	}
	
	/**
	 * ������ť
	 */
	protected void lockButtom() {
		mWriteComment.setEnabled(false);
		commentMore.setEnabled(false);
	}


	/**
	 * �첽��ȡ�������������ݣ����½���
	 * @author zuo
	 *
	 */
	class GetNewsFromeNetTask extends AsyncTask<String, Integer, NewsInfo>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			showLoadingProgressDialog("���Ե�...");
		}
		@Override
		protected NewsInfo doInBackground(String... params) {
			// �õ�����������������
			try {
				//AppLogger.i("������ϸ������ʷ�������ַ��" + params[0]);
				return XmlHelper.getNewsInfos(IOStream.getIOStreamFromUrl(params[0])).get(0);
//				return XmlHelper.getNewsInfos(IOStream.getIOStreamFromUrl("http://192.168.1.102:8080/news_b/data/news_v4.xml")).get(0);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				AppLogger.i("������ϸ���棺���ӷ������������������쳣");
				dismissProgressDialog();
				return null;
			}
		}

		@Override
		protected void onPostExecute(NewsInfo result) {
			dismissProgressDialog();
			if (result != null) {
				AppLogger.i("������ϸ���棬��ȡ���������ݳɹ�");
				mNewsInfo = result;
				cache.put(mNewsInfoCacheKey, result);
				// �������Ž���
				update(result);
			} else {
				// ���Ի�ȡ��������
				getDataFromCache();
				AppLogger.i("������ϸ���棬��ȡ����������ʧ��");
			}
		}
		
	}

	/**
	 * ������������NewsInfo����������ϸ����
	 * @param info	�������ŵ���������
	 */
	private void update(NewsInfo info) {
		if (info == null) return ;
		ViewUtil viewUtil = new ViewUtil(this);
		// �Ƴ�֮ǰ�Ľ�������
		newsBox.removeAllViews();
		// ˳��ȡ���ݲ���
		for (Object o : info.getContent()){
			if (o instanceof NewsTitle){	// ����
				newsBox.addView(viewUtil.addView( (NewsTitle) o));
			} else if (o instanceof NewsTimeSource) {	// ʱ����Դ
				newsBox.addView(viewUtil.addView((NewsTimeSource) o));
			}  else if (o instanceof NewsLead) {	// ����
				newsBox.addView(viewUtil.addView((NewsLead) o));
			}else if (o instanceof NewsLink) {	// ����
				newsBox.addView(viewUtil.addView((NewsLink) o));
			} else if (o instanceof NewsImage) {	// ͼƬ
				newsBox.addView(viewUtil.addView((NewsImage) o));
			} else if (o instanceof NewsVideo) {	// ��Ƶ
				newsBox.addView(viewUtil.addView((NewsVideo) o));
			} else if (o instanceof NewsContent) {	// ��������
				mNewsContent = (TextView) viewUtil.addView((NewsContent) o);
				newsBox.addView(mNewsContent);
			} else if(o instanceof Follow) {
				Follow follow = (Follow)o;
				fillCommentView(follow.getFollowuser(), follow.getFollowtime(), follow.getFollowcontent());
			}
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putString(SHOWNEWSACTIVITYNEWSID, mNewsId);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		if (mCollectFlagCachekey != null) {
			collectState = cache.getInt(mCollectFlagCachekey);
			if (collectState == COLLECT) {	// ���ղأ��ı���Ϊ��ɫ
				menu.getItem(0).setIcon(R.drawable.ic_collect_heart_red);
			}
		}
		return true;
	}
	
	/**
	 * ȡ���ղ�
	 */
	private void deleteCollect() {
		// д�߼�
		if (userInfo != null && NetUtil.canConnectNet()) {
			new MyDeleteCollectTask().execute();
//			new MyAddCollectTask().execute();
		}
	}
	
	/**
	 * ����ղ�
	 */
	private void addCollect() {
		// �Ѿ���¼�����ղ�
		if (userInfo != null && NetUtil.canConnectNet()) {
			new MyAddCollectTask().execute();
		}
	}

	/**
	 * ����ղغ�̨�߳�
	 * @author zuo
	 *
	 */
	class MyAddCollectTask extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			UserHelper.login(userInfo.getUserName(), userInfo.getUserPwd());
			return UserHelper.addCollect(mNewsId);
		}
		
		@Override
		protected void onPostExecute(String result) {
			if (result == null) {
				Toast.makeText(mContext, "�޷����ӷ�����", 0).show();
			} else {
				AppLogger.i("����ղط����ַ��������" + result); 
				try {
					JSONObject jo = new JSONObject(result);
					String jsonResult = jo.getString("result");
					if ("false".equals(jsonResult)) {	// ʧ��
						Toast.makeText(mContext, "���¼~", 0).show();
					} else if ("true".equals(jsonResult)){
						Toast.makeText(mContext, "�ղ����ųɹ�", 0).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					AppLogger.i("����ղأ�JSONException");
					e.printStackTrace();
				}
			}
		}
	}

	/*
	 * ɾ���ղغ�̨�߳�
	 */
	class MyDeleteCollectTask extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			UserHelper.login(userInfo.getUserName(), userInfo.getUserPwd());
			return UserHelper.delCollect(mNewsId);
		}
		
		@Override
		protected void onPostExecute(String result) {
			if (result == null) {
				Toast.makeText(mContext, "�޷����ӷ�����", 0).show();
			} else {
				AppLogger.i("ɾ���ղط����ַ��������" + result); 
				try {
					JSONObject jo = new JSONObject(result);
					String jsonResult = jo.getString("result");
					if ("false".equals(jsonResult)) {	// ʧ��
						Toast.makeText(mContext, "���¼~", 0).show();
					} else if ("true".equals(jsonResult)){
						Toast.makeText(mContext, "ɾ���ղسɹ�", 0).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					AppLogger.i("ɾ���ղأ�JSONException");
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_collect:	// ����ղ�
			// ����¼
			if (!verify()) {
				return true;
			}
			if (collectState == COLLECT) {	// �Ѿ��ղ��ˣ��ٴα��������Ϊδ�ղ�
				item.setIcon(R.drawable.ic_action_favorite);
				cache.putInt(mCollectFlagCachekey, NOCOLLECT);
				collectState = NOCOLLECT;
				// ȡ���ղ������ύ��������
				deleteCollect();
			} else {
				item.setIcon(R.drawable.ic_collect_heart_red);
				cache.putInt(mCollectFlagCachekey, COLLECT);
				collectState = COLLECT;
				// ����ղ������ύ��������
				addCollect();
			}
			break;
		case R.id.action_share:	// ����
			Intent intent=new Intent(Intent.ACTION_SEND);   
	        intent.setType("text/plain");  
	        intent.putExtra(Intent.EXTRA_SUBJECT, "����");   
	        intent.putExtra(Intent.EXTRA_TEXT, "�Ƽ�һ��Ӧ�ø��㣬�������ţ����Է���鿴����!");  
	        intent.putExtra(Intent.EXTRA_TITLE, "������");  
	        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
	        startActivity(Intent.createChooser(intent, "��ѡ��"));   
			break;
		case R.id.action_fontsize1:	// �����С����
			// ��ʾ����ѡ�������С
			mNewsContent.setTextSize(14);
			break;
		case R.id.action_fontsize2:	// �����С����
			// ��ʾ����ѡ�������С
			mNewsContent.setTextSize(18);
			break;
		case R.id.action_fontsize3:	// �����С����
			// ��ʾ����ѡ�������С
			mNewsContent.setTextSize(22);
			break;
		case R.id.action_fontsize4:	// �����С����
			// ��ʾ����ѡ�������С
			mNewsContent.setTextSize(26);
			break;

		default:
			break;
		}
		
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_comment_send:	// �������
		{
			AppLogger.i("���۰�ť�������");
			// ��֤
			if (!verify()) {
				return ;
			}
			// ��ȡ���������
			String content = mCommentContent.getText().toString().trim();
			if (StringUtil.isEmpty(content)) {
				Toast.makeText(mContext, StringUtil.getString(R.string.comment_isempty), 0).show();
				return ;
			}
			// �ϴ����۵�������
			new AddCommentToServer(mContext, content, mNewsId).execute();
		}
			break;
			
		case R.id.bt_follow_showmore:	// �鿴��������
		{
			Intent intent = new Intent(ShowNewsActivity.this, ShowCommentActivity.class);
			intent.putExtra(SHOWNEWSACTIVITYNEWSID, mNewsId);
			startActivity(intent);
		}
			break;

		default:
			break;
		}
	}

	/**
	 * ������۽���
	 * @param name
	 * @param time
	 * @param content
	 */
	private void fillCommentView(String name, String time, String content) {
		commentUserName.setText(name);
		/*if (!StringUtil.isEmpty(time)) {
			commentTime.setText(time);
		} else {
			commentTime.setText("�ո�");
		}*/
		commentTime.setText(time);
		commentContent.setText(content);
		bottomComment.setVisibility(View.VISIBLE);
	}

	/**
	 * ��֤�Ƿ���������û��Ƿ��Ѿ���¼
	 * @return
	 */
	private boolean verify() {
		// �ж������Ƿ�����
		if(!NetUtil.canConnectNet()) {
			Toast.makeText(mContext, StringUtil.getString(R.string.fail_connect_server), 0).show();
			return false;
		}
		// ����û��Ƿ��¼
		userInfo = UserInfoManager.getUserInfo();
		if (userInfo == null) {
			// ������¼����
			IntentUtil.go2Login(mContext);
			return false;
		}
		return true;
	}

}
