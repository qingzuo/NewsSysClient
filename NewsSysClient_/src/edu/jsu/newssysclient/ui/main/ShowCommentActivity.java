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
 * 时间：
 * 2014年6月2日11:39:06
 * 功能：
 * 1、显示评论，传入新闻id，根据id查询数据并显示；done
 * 2、提交评论，提交后显示自己的评论在第一条，并上传数据到服务器；
 * @author zuo
 *
 */
public class ShowCommentActivity extends MyBaseActivity {
	
	private String mNewsID;
	private Cache cache = new Cache();
	private List<Follow> datas = new ArrayList<Follow>();
	private ShowCommentListAdapter mAdapter;
	private ImageLoader imageLoader;
	
	// 视图控件
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
		
		// 获取新闻id
		if (getIntent() != null) {
			mNewsID = getIntent().getStringExtra(ShowNewsActivity.SHOWNEWSACTIVITYNEWSID);
		}
		AppLogger.i("评论界面得到的新闻ID: " + mNewsID);
		
		setContentView(R.layout.show_comment_layout);
		
		mWriteComment = (ImageButton) findViewById(R.id.ib_comment_send);
		mCommentContent = (EditText) findViewById(R.id.et_comment_content);
		// 设置点击事件
		mWriteComment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 验证用户
				final UserInfo userInfo = UserInfoManager.getUserInfo();
				if (userInfo == null) {
					Toast.makeText(mContext, "请先登录~", 0).show();
					return ;
				}
				// 得到内容
				final String content = mCommentContent.getText().toString().trim();
				if (StringUtil.isEmpty(content)) {
					Toast.makeText(mContext, "评论内容是空的", 0).show();
					return ;
				}
				// 封装数据
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				String time = format.format(new Date(System.currentTimeMillis()));
				Follow f = new Follow();
				f.setFollowcontent(content);
				f.setFollowuser(userInfo.getUserName());
				f.setFollowtime(time);
				f.setFollowuserhead(userInfo.getPtoShowPath());
				// 更新界面
				datas.add(0, f);
				if (mAdapter != null) mAdapter.notifyDataSetChanged();
				// 上传评论到服务器
				new AsyncTask<Void, Integer, String> (){

					@Override
					protected String doInBackground(Void... params) {
						UserHelper.login(userInfo.getUserName(), userInfo.getUserPwd());
						return UserHelper.addComment(content, mNewsID);
					}
					
					@Override
					protected void onPostExecute(String result) {
						if (result == null) {
							AppLogger.i("新闻详细界面，添加评论，无法得到服务器响应");
						}else {
							JSONObject jo;
							try {
								jo = new JSONObject(result);
								String jsonRes = jo.getString("result");
								AppLogger.i("新闻详细界面，添加评论结果：" + jsonRes);
								if ("true".equals(jsonRes)) {
									Toast.makeText(mContext, "发表成功", 0).show();
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
		
		// 获取更新数据
		if (NetUtil.canConnectNet()) {
			// 从网络获取数据
			task.execute("");
		} else {
			getDataFromCache();
		}
	}
	
	// 没有联网并没有缓存的时候提示用户没有联网，提示完后如果用户开启网络再次从服务器拿数据，并更新界面
	
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
	 * 从缓存拿数据
	 */
	private void getDataFromCache() {
		AppLogger.i("获取缓存数据");
		FollowList list = cache.get(Cache.NEWSCOMMENTLISTBYNEWSID + mNewsID, FollowList.class);
		if (list != null && list.getFollows() != null && list.getFollows().size() != 0) {
			// 添加数据
			for (Follow f : list.getFollows()) {
				datas.add(f);
			}
			// 更新界面
			updateUI();
		}
	}
	
	/**
	 * 数据改变更新界面
	 */
	private void updateUI() {
		if (mAdapter != null) {
			mAdapter.notifyDataSetChanged();
		}
	}
	
	/**
	 * 服务器拿数据线程，如果没有拿到，再次拿缓存数据
	 */
	AsyncTask<String, Integer, FollowList> task = new AsyncTask<String, Integer, FollowList>(){

		@Override
		protected FollowList doInBackground(String... params) {
			// 获取数据
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
			// 判断是否有数据
			if (apiList != null && apiList.getFollows() != null && apiList.getFollows().size() != 0) {
				AppLogger.i("网络获取评论列表成功");
				// 添加数据
				for (Follow f : apiList.getFollows()){
					datas.add(f);
				}
				// 缓存数据
				cache.put(Cache.NEWSCOMMENTLISTBYNEWSID + mNewsID, apiList);
				// 更新界面
				updateUI();
			} else {
				AppLogger.i("网络获取评论列表失败");
				// 拿缓存
				getDataFromCache();
			}
		};
	};
	
}
