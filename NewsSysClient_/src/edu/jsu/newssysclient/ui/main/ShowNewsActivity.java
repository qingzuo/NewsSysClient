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
 * 新闻详细信息界面
 * 主要功能：
 * 		显示新闻，从服务器读取xml数据解析后布局界面
 * 用户需登录功能：
 * 		收藏功能：debug本机保存收藏状态
 * 		评论功能：
 * 不需要登录功能：
 * 		分享功能；
 * @author zuo
 *
 */
public class ShowNewsActivity extends MyBaseActivity implements OnClickListener {
	public static final String SHOWNEWSACTIVITYNEWSID = "edu.jsu.newssysclient.ui.main.ShowANewsActivity.newsid";
	private NewsInfo mNewsInfo;	// 新闻详细信息
	private String mNewsId;	// 新闻id
	private UserInfo userInfo;	// 用户信息
	
	// 缓存相关
	private Cache cache = new Cache();
	private String mNewsInfoCacheKey;
	private String mCollectFlagCachekey;	// 保存收藏信息的key
	private int COLLECT = 1;	// 已经收藏
	private int NOCOLLECT = 0;	// 未收藏
	private int collectState = NOCOLLECT;

	// 界面元素
	private LinearLayout myView;	// 整个新闻详细界面
	private LinearLayout newsBox;	// 存放所以新闻控件的容器
	private LinearLayout bottomComment;		// 底部评论的界面
	private TextView commentUserName;
	private TextView commentTime;
	private TextView commentContent;
	private Button commentMore;
	private EditText mCommentContent;
	private Button mWriteComment;
	
	TextView mNewsContent;	// 新闻主体内容
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null){
			mNewsId = savedInstanceState.getString(SHOWNEWSACTIVITYNEWSID);
		}
		
		// 获取新闻id
		if (getIntent() != null) {
			mNewsId = getIntent().getStringExtra(SHOWNEWSACTIVITYNEWSID);
		}
		AppLogger.i("新闻详细信息界面得到新闻ID: " + mNewsId);
		userInfo = UserInfoManager.getUserInfo();
		// 初始化缓存key
		mNewsInfoCacheKey = Cache.NEWSINFOBYNEWSID+mNewsId;
		if (userInfo != null) {
			mCollectFlagCachekey = Cache.USERCOLLECTFLAGBYNEWSIDANDUSERID + "-newsid" + mNewsId + "-userid" + userInfo.getUserID();
		}
		
		// 初始化界面
		setContentView(R.layout.show_news_layout);
		initView();
		
		// 获取net数据
		getDataFromNet();
		// 获取缓存
		getDataFromCache();
	}

	/**
	 * 获取数据，从网络获取
	 */
	private void getDataFromNet() {
		// 获取网络数据
		new GetNewsFromeNetTask().execute(URLHelper.ANEWSURL + mNewsId);
	}

	/**
	 * 缓存获取数据
	 */
	private void getDataFromCache() {
		NewsInfo tt = cache.get(mNewsInfoCacheKey, NewsInfo.class);
		if (tt != null) {
			mNewsInfo = tt;
			AppLogger.i("新闻详细界面，使用缓存数据");
			// 使用缓存更新界面
			update(mNewsInfo);
			dismissProgressDialog();
		}
	}

	/**
	 * 初始化控件
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
	 * 后台添加评论
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
				AppLogger.i("新闻详细界面，添加评论，无法得到服务器响应");
			}else {
				String jsonRes = JsonUtil.getJsonResult(result);
				AppLogger.i("新闻详细界面，添加评论结果：" + jsonRes);
				if ("true".equals(jsonRes)) {
					// 更新界面
					fillCommentView(userInfo.getUserName(), "刚刚", content);
					Toast.makeText(this.context, "发表成功", 0).show();
					mCommentContent.setText("");
				} else {
					// 跳到登记界面
					IntentUtil.go2Login(this.context);
				}
			}
		}
		
	}
	
	/**
	 * 解锁按钮
	 */
	protected void unLockButtom() {
		mWriteComment.setEnabled(true);
		commentMore.setEnabled(true);
	}
	
	/**
	 * 锁定按钮
	 */
	protected void lockButtom() {
		mWriteComment.setEnabled(false);
		commentMore.setEnabled(false);
	}


	/**
	 * 异步获取服务器新闻数据，更新界面
	 * @author zuo
	 *
	 */
	class GetNewsFromeNetTask extends AsyncTask<String, Integer, NewsInfo>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			showLoadingProgressDialog("请稍等...");
		}
		@Override
		protected NewsInfo doInBackground(String... params) {
			// 得到服务器的新闻数据
			try {
				//AppLogger.i("新闻详细界面访问服务器地址：" + params[0]);
				return XmlHelper.getNewsInfos(IOStream.getIOStreamFromUrl(params[0])).get(0);
//				return XmlHelper.getNewsInfos(IOStream.getIOStreamFromUrl("http://192.168.1.102:8080/news_b/data/news_v4.xml")).get(0);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				AppLogger.i("新闻详细界面：连接服务器下载新闻数据异常");
				dismissProgressDialog();
				return null;
			}
		}

		@Override
		protected void onPostExecute(NewsInfo result) {
			dismissProgressDialog();
			if (result != null) {
				AppLogger.i("新闻详细界面，获取服务器数据成功");
				mNewsInfo = result;
				cache.put(mNewsInfoCacheKey, result);
				// 更新新闻界面
				update(result);
			} else {
				// 尝试获取缓存数据
				getDataFromCache();
				AppLogger.i("新闻详细界面，获取服务器数据失败");
			}
		}
		
	}

	/**
	 * 根据新闻数据NewsInfo布局新闻详细界面
	 * @param info	保存新闻的所有数据
	 */
	private void update(NewsInfo info) {
		if (info == null) return ;
		ViewUtil viewUtil = new ViewUtil(this);
		// 移除之前的界面内容
		newsBox.removeAllViews();
		// 顺序取数据布局
		for (Object o : info.getContent()){
			if (o instanceof NewsTitle){	// 标题
				newsBox.addView(viewUtil.addView( (NewsTitle) o));
			} else if (o instanceof NewsTimeSource) {	// 时间来源
				newsBox.addView(viewUtil.addView((NewsTimeSource) o));
			}  else if (o instanceof NewsLead) {	// 导语
				newsBox.addView(viewUtil.addView((NewsLead) o));
			}else if (o instanceof NewsLink) {	// 链接
				newsBox.addView(viewUtil.addView((NewsLink) o));
			} else if (o instanceof NewsImage) {	// 图片
				newsBox.addView(viewUtil.addView((NewsImage) o));
			} else if (o instanceof NewsVideo) {	// 视频
				newsBox.addView(viewUtil.addView((NewsVideo) o));
			} else if (o instanceof NewsContent) {	// 新闻内容
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
			if (collectState == COLLECT) {	// 有收藏，改变心为红色
				menu.getItem(0).setIcon(R.drawable.ic_collect_heart_red);
			}
		}
		return true;
	}
	
	/**
	 * 取消收藏
	 */
	private void deleteCollect() {
		// 写逻辑
		if (userInfo != null && NetUtil.canConnectNet()) {
			new MyDeleteCollectTask().execute();
//			new MyAddCollectTask().execute();
		}
	}
	
	/**
	 * 添加收藏
	 */
	private void addCollect() {
		// 已经登录进行收藏
		if (userInfo != null && NetUtil.canConnectNet()) {
			new MyAddCollectTask().execute();
		}
	}

	/**
	 * 添加收藏后台线程
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
				Toast.makeText(mContext, "无法连接服务器", 0).show();
			} else {
				AppLogger.i("添加收藏返回字符串结果：" + result); 
				try {
					JSONObject jo = new JSONObject(result);
					String jsonResult = jo.getString("result");
					if ("false".equals(jsonResult)) {	// 失败
						Toast.makeText(mContext, "请登录~", 0).show();
					} else if ("true".equals(jsonResult)){
						Toast.makeText(mContext, "收藏新闻成功", 0).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					AppLogger.i("添加收藏，JSONException");
					e.printStackTrace();
				}
			}
		}
	}

	/*
	 * 删除收藏后台线程
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
				Toast.makeText(mContext, "无法连接服务器", 0).show();
			} else {
				AppLogger.i("删除收藏返回字符串结果：" + result); 
				try {
					JSONObject jo = new JSONObject(result);
					String jsonResult = jo.getString("result");
					if ("false".equals(jsonResult)) {	// 失败
						Toast.makeText(mContext, "请登录~", 0).show();
					} else if ("true".equals(jsonResult)){
						Toast.makeText(mContext, "删除收藏成功", 0).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					AppLogger.i("删除收藏，JSONException");
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_collect:	// 点击收藏
			// 检查登录
			if (!verify()) {
				return true;
			}
			if (collectState == COLLECT) {	// 已经收藏了，再次被点击设置为未收藏
				item.setIcon(R.drawable.ic_action_favorite);
				cache.putInt(mCollectFlagCachekey, NOCOLLECT);
				collectState = NOCOLLECT;
				// 取消收藏数据提交到服务器
				deleteCollect();
			} else {
				item.setIcon(R.drawable.ic_collect_heart_red);
				cache.putInt(mCollectFlagCachekey, COLLECT);
				collectState = COLLECT;
				// 添加收藏数据提交到服务器
				addCollect();
			}
			break;
		case R.id.action_share:	// 分享
			Intent intent=new Intent(Intent.ACTION_SEND);   
	        intent.setType("text/plain");  
	        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");   
	        intent.putExtra(Intent.EXTRA_TEXT, "推荐一个应用给你，叫悦新闻，可以方便查看新闻!");  
	        intent.putExtra(Intent.EXTRA_TITLE, "悦新闻");  
	        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
	        startActivity(Intent.createChooser(intent, "请选择"));   
			break;
		case R.id.action_fontsize1:	// 字体大小设置
			// 显示界面选择字体大小
			mNewsContent.setTextSize(14);
			break;
		case R.id.action_fontsize2:	// 字体大小设置
			// 显示界面选择字体大小
			mNewsContent.setTextSize(18);
			break;
		case R.id.action_fontsize3:	// 字体大小设置
			// 显示界面选择字体大小
			mNewsContent.setTextSize(22);
			break;
		case R.id.action_fontsize4:	// 字体大小设置
			// 显示界面选择字体大小
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
		case R.id.ib_comment_send:	// 添加评论
		{
			AppLogger.i("评论按钮被点击了");
			// 验证
			if (!verify()) {
				return ;
			}
			// 获取并检查内容
			String content = mCommentContent.getText().toString().trim();
			if (StringUtil.isEmpty(content)) {
				Toast.makeText(mContext, StringUtil.getString(R.string.comment_isempty), 0).show();
				return ;
			}
			// 上传评论到服务器
			new AddCommentToServer(mContext, content, mNewsId).execute();
		}
			break;
			
		case R.id.bt_follow_showmore:	// 查看更多评论
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
	 * 填充评论界面
	 * @param name
	 * @param time
	 * @param content
	 */
	private void fillCommentView(String name, String time, String content) {
		commentUserName.setText(name);
		/*if (!StringUtil.isEmpty(time)) {
			commentTime.setText(time);
		} else {
			commentTime.setText("刚刚");
		}*/
		commentTime.setText(time);
		commentContent.setText(content);
		bottomComment.setVisibility(View.VISIBLE);
	}

	/**
	 * 验证是否有网络和用户是否已经登录
	 * @return
	 */
	private boolean verify() {
		// 判断网络是否连接
		if(!NetUtil.canConnectNet()) {
			Toast.makeText(mContext, StringUtil.getString(R.string.fail_connect_server), 0).show();
			return false;
		}
		// 检查用户是否登录
		userInfo = UserInfoManager.getUserInfo();
		if (userInfo == null) {
			// 跳到登录界面
			IntentUtil.go2Login(mContext);
			return false;
		}
		return true;
	}

}
