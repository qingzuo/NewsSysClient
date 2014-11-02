package edu.jsu.newssysclient.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;

import edu.jsu.newssysclient.R;
import edu.jsu.newssysclient.adapter.ViewHolder;
import edu.jsu.newssysclient.bean.VideoNews;
import edu.jsu.newssysclient.dao.cache.Cache;
import edu.jsu.newssysclient.debug.AppLogger;
import edu.jsu.newssysclient.ui.main.MyBaseActivity;
import edu.jsu.newssysclient.ui.main.VideoPlayActivity;
import edu.jsu.newssysclient.util.json.ParseVideoNewsUtil;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.TaskStackBuilder;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ��Ƶ�б�Fragment
 * @author zuo
 *
 */
public class VideoNewsListFragment extends Fragment {
	private Activity mActivity;
	ListView mListView;
	MyVideoNewsAdapter mAdapter;
	private PullToRefreshListView mPullRefreshListView;
	String mType;
	List<VideoNews> mData = new ArrayList<VideoNews>(); 
	private ProgressDialog progressDialog;

	/**
	 * �ṩʵ��������
	 * @param type
	 * @return
	 */
	public static VideoNewsListFragment newInstance(String type) {
		VideoNewsListFragment fragment = new VideoNewsListFragment();
		fragment.mType = type;

		return fragment;
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		this.mActivity = activity;
		super.onAttach(activity);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// ��ȡ����
		getData();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LinearLayout v = (LinearLayout) View.inflate(mActivity, R.layout.show_news_list_layout,
				null);
		
		mAdapter = new MyVideoNewsAdapter(mActivity);
		// ˢ�¿ؼ�������
		mPullRefreshListView = (PullToRefreshListView) v
				.findViewById(R.id.lv_refresh_all_new_listview);
		mPullRefreshListView.setMode(Mode.BOTH);
		mPullRefreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(mActivity, VideoPlayActivity.class);
				VideoNews v = mData.get(position-1);
				if (v.getMp4_url()!=null) {
					intent.putExtra(VideoPlayActivity.VIDEOURL, v.getMp4_url());
				}
				mActivity.startActivity(intent);
			}
		});
		mPullRefreshListView
		.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				mPullRefreshListView.onRefreshComplete();
				Toast.makeText(mActivity, "û����������~", 0).show();
			}
		});
		// �б�ͷ�������
		mPullRefreshListView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {

					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						mPullRefreshListView.onRefreshComplete();
						Toast.makeText(mActivity, "û����������~", 0).show();
					}
				});
		
		ListView actualListView = mPullRefreshListView.getRefreshableView();
		actualListView.setAdapter(mAdapter);
		
		return v;
	}

	/**
	 * ��ȡ��Ƶ����
	 */
	private void getData() {
		String url = null;
		if (mType == "����") {
			url = "http://c.m.163.com/nc/video/list/V9LG4B3A0/n/0-10.html";
		} else if (mType == "����") {
			url = "http://c.m.163.com/nc/video/list/V9LG4CHOR/n/0-10.html";
		} else if (mType == "��Ц") {
			url = "http://c.m.163.com/nc/video/list/V9LG4E6VR/n/0-10.html";
		} else if (mType == "��Ʒ") {
			url = "http://c.m.163.com/nc/video/list/00850FRB/n/0-10.html";
		}
		new AsyncTask<String, Integer, Boolean>() {
			protected void onPreExecute() {
				showLoadingProgressDialog("���Ե�...");
			};

			@Override
			protected Boolean doInBackground(String... params) {
				AppLogger.i("��̨��ȡ����");
				try {
					List<VideoNews> t = ParseVideoNewsUtil.getVideoNewsList(params[0]);
					if (t!=null && t.size() != 0) {
						for (VideoNews v : t) {
							mData.add(v);
						}
						return true;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return false;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				dismissProgressDialog();
				if (result == true) {
					mAdapter.notifyDataSetChanged();
					AppLogger.i("�����ݣ�" + mData.size());
				} else {
					Toast.makeText(mActivity, "û������", 0).show();
				}
			}
		}.execute(url);
	}

	/**
	 * ��ʾ�ȴ�����
	 * @param message	�Զ�����ʾ��Ϣ
	 */
	public void showLoadingProgressDialog(String message) {
		if (this.progressDialog == null) {
			this.progressDialog = new ProgressDialog(mActivity);
			this.progressDialog.setIndeterminate(true);
		}

		this.progressDialog.setMessage(message);
		this.progressDialog.show();
	}

	/**
	 * �رյȴ�����
	 */
	public void dismissProgressDialog() {
		if (this.progressDialog != null) {
			this.progressDialog.dismiss();
		}
	}
	
	/**
	 * ��Ƶ�б�������
	 * @author zuo
	 *
	 */
	class MyVideoNewsAdapter extends BaseAdapter{
		Context mContext;
		
		public MyVideoNewsAdapter(Context context) {
			mContext = context;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mData.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(mContext, R.layout.item_videnews, null);
			}
			
			// ��ȡ�ؼ�
			ImageView image = ViewHolder.get(convertView, R.id.image);
			TextView title = ViewHolder.get(convertView, R.id.title);
			TextView follow = ViewHolder.get(convertView, R.id.follow);
			
			// ���ÿؼ�ֵ
			VideoNews v = mData.get(position);
			ImageLoader.getInstance().displayImage(v.getCover(), image);
			title.setText(v.getTitle());
			follow.setText(v.getReplyCount()+"����");
			
			return convertView;
		}
		
	}
}
