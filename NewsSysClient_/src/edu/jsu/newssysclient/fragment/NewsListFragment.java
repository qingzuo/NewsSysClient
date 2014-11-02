package edu.jsu.newssysclient.fragment;

import java.util.ArrayList;
import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;

import edu.jsu.newssysclient.R;
import edu.jsu.newssysclient.URLHelper;
import edu.jsu.newssysclient.adapter.MyGalleryAdapter;
import edu.jsu.newssysclient.adapter.ShowNewsListAdapter;
import edu.jsu.newssysclient.bean.Item;
import edu.jsu.newssysclient.bean.ItemImages;
import edu.jsu.newssysclient.bean.ItemList;
import edu.jsu.newssysclient.dao.cache.Cache;
import edu.jsu.newssysclient.debug.AppLogger;
import edu.jsu.newssysclient.ui.main.AllTypeNewsActivity;
import edu.jsu.newssysclient.ui.main.GalleryUrlActivity;
import edu.jsu.newssysclient.ui.main.ImageNewsActivity_;
import edu.jsu.newssysclient.ui.main.ShowNewsActivity;
import edu.jsu.newssysclient.ui.widget.CircleFlowIndicator;
import edu.jsu.newssysclient.ui.widget.ImageAdapter;
import edu.jsu.newssysclient.ui.widget.MyViewFlow;
import edu.jsu.newssysclient.ui.widget.ViewFlow;
import edu.jsu.newssysclient.util.local.IntentUtil;
import edu.jsu.newssysclient.util.local.StringUtil;
import edu.jsu.newssysclient.util.net.MyApi;
import edu.jsu.newssysclient.util.net.NetUtil;
import edu.jsu.newssysclient.util.net.NewsSysApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * �����б� �����ڲ�ͬ���������ͣ���ѯ��ͬ�����ݲ���ʾ
 * ��ʾ�ض����͵�����Fragment
 * 
 * @author zuo
 * 
 */
public class NewsListFragment extends Fragment {
	private static final String KEY_CONTENT = "TestFragment:Content";
	private Activity mActivity;
	private String mType = "1";
	private PullToRefreshListView mPullRefreshListView;
	private ShowNewsListAdapter mAdapter;
	MyGalleryAdapter galleryAdapter;
	private List<Item> newsList = new ArrayList<Item>(); // ��������
	private List<Item> galleryList = new ArrayList<Item>(); // ͷ����������
	private Cache cache = new Cache();; // ����
	private String cacheKey;
	private int page = 1; // ��ҳ��ѯ��Ĭ�ϵ�һҳ
	protected boolean isRefreshing = false;
	private long refreshTime;
	ViewFlow viewFlow;

	public static NewsListFragment newInstance(String type) {
		NewsListFragment fragment = new NewsListFragment();
		fragment.mType = type;
		fragment.cacheKey = Cache.NEWSLISTBYTYPEID + type;

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

		if ((savedInstanceState != null)
				&& savedInstanceState.containsKey(KEY_CONTENT)) {
			mType = savedInstanceState.getString(KEY_CONTENT);
		}

		this.cacheKey = Cache.NEWSLISTBYTYPEID + mType;
		// ��ȡ����
		getDate();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LinearLayout v = (LinearLayout) View.inflate(mActivity,
				R.layout.show_news_list_layout, null);

		// ˢ�¿ؼ�������
		mPullRefreshListView = (PullToRefreshListView) v
				.findViewById(R.id.lv_refresh_all_new_listview);
		mPullRefreshListView.setMode(Mode.BOTH);
		mPullRefreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Item item = newsList.get(arg2 - 2);
				Intent intent = null;
				if (item instanceof ItemImages) { // ͼƬ����
					ItemImages itemImages = (ItemImages) item;
					intent = new Intent(mActivity, GalleryUrlActivity.class);
					intent.putExtra(ShowNewsActivity.SHOWNEWSACTIVITYNEWSID,
							itemImages.getId());
					// �������ݾ�
					cache.put(
							Cache.NEWSIMAGENEWSLISTBYNEWSID
									+ itemImages.getId(), itemImages);
					startActivity(intent);
				} else { // ��ͨ����
					IntentUtil.go2ShowNewsActivity(mActivity, item.getId());
				}
			}
		});

		// �б�ĩβ�������
		mPullRefreshListView
				.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

					@Override
					public void onLastItemVisible() {
						Toast.makeText(mActivity, "End of List!",
								Toast.LENGTH_SHORT).show();
						Toast.makeText(mActivity, "�ײ������˯��", 0).show();
						// ˢ�µײ�
						new OldDataTask().execute();
					}
				});

		// �б�ͷ�������
		mPullRefreshListView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {

					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						long nowTime = System.currentTimeMillis();
						long minute = (nowTime - refreshTime) / (60 * 1000);
						AppLogger.i("ˢ�����ݣ�" + minute);
						// mPullRefreshListView.getLoadingLayoutProxy().setLastUpdatedLabel(""
						// + minute + "������ǰˢ��");
						// �ж��Ƿ񲻾û����
						if (!isRefreshing) {
							isRefreshing = true;
							if (mPullRefreshListView.isHeaderShown()) { // ͷ��ˢ��
								mPullRefreshListView.getLoadingLayoutProxy()
										.setRefreshingLabel("���ڼ���");
								mPullRefreshListView.getLoadingLayoutProxy()
										.setPullLabel("�������ظ���");
								mPullRefreshListView.getLoadingLayoutProxy()
										.setReleaseLabel("�ͷſ�ʼ����");

								new UpdateTask().execute();
							} else if (mPullRefreshListView.isFooterShown()) { // �ײ�ˢ��
								mPullRefreshListView.getLoadingLayoutProxy()
										.setRefreshingLabel("����ˢ��");
								mPullRefreshListView.getLoadingLayoutProxy()
										.setPullLabel("����ˢ��");
								mPullRefreshListView.getLoadingLayoutProxy()
										.setReleaseLabel("�ͷſ�ʼˢ��");
								new OldDataTask().execute();
							}
						} else {
							loadComplete();
						}
					}
				});

		ListView actualListView = mPullRefreshListView.getRefreshableView();

		// ����ͷ��ͼƬ����
		FrameLayout head = (FrameLayout) View.inflate(mActivity,
				R.layout.newslist_top_gallery_layout, null);
		viewFlow = (ViewFlow) head.findViewById(R.id.viewflow);
		galleryAdapter = new MyGalleryAdapter(mActivity, galleryList);
		viewFlow.setAdapter(galleryAdapter, 0);
		int circleSize = galleryList.size();
		viewFlow.setmSideBuffer(circleSize); //
		CircleFlowIndicator indic = (CircleFlowIndicator) head
				.findViewById(R.id.viewflowindic);
		viewFlow.setFlowIndicator(indic);
		viewFlow.setTimeSpan(4500);
		viewFlow.setSelection(circleSize * 1000); // ���ó�ʼλ��
		viewFlow.startAutoFlowTimer(); // �����Զ�����
		viewFlow.setParent(head);

		// ��ӵ�ListViewͷ��
		if (head != null) {
			actualListView.addHeaderView(head);
		}

		// ����adapter
		mAdapter = new ShowNewsListAdapter(mActivity, newsList, galleryList);
		actualListView.setAdapter(mAdapter);

		boolean pauseOnScroll = false; //
		boolean pauseOnFling = true; // ���ٻ���ʱ��ͣ����ͼƬ
		PauseOnScrollListener listener = new PauseOnScrollListener(
				mAdapter.getImageLoader(), pauseOnScroll, pauseOnFling);
		actualListView.setOnScrollListener(listener);
		

		return v;
	}

	/**
	 * ˢ�����
	 */
	private void loadComplete() {
		if (mPullRefreshListView != null) {
			mPullRefreshListView.onRefreshComplete();
		}
		isRefreshing = false;
		if (mAdapter != null)
			mAdapter.notifyDataSetChanged();
		// if (galleryAdapter != null) galleryAdapter.notifyDataSetChanged();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		ImageLoader imageLoader = mAdapter.getImageLoader();
		imageLoader.clearMemoryCache();
	}

	/**
	 * ��ȡ����
	 */
	public void getDate() {
		// ��ȡ��������
		getCacheData();
		// ��̨�̻߳�ȡ��������
		new UpdateTask().execute();
	}

	/**
	 * ��ȡ��������
	 */
	private void getCacheData() {
		ItemList cacheList = cache.get(cacheKey, ItemList.class);
		if (existData(cacheList)) {
			newsList.clear();
			galleryList.clear();

			// �ָ�����
			splitData2TwoList(cacheList.getItems());

			if (mAdapter != null && cacheList.getItems().size() != 0) {
				mAdapter.notifyDataSetChanged();
			}
			AppLogger.i(mType + "��ȡ���������б����ݣ����ݴ�С: "
					+ cacheList.getItems().size());
		}
	}

	/**
	 * �ָ��������������ݼ����������
	 * 
	 * @param mList
	 */
	private void splitData2TwoList(List<Item> mList) {
		for (int i = 0, t = 0; t < mList.size(); t++) {
			// AppLogger.i(t + "����ͼƬ��ַ��" + mList.get(t).getImageurl());
			// ѡ����ǰ����������ͼƬ�����ţ��������û�п��ǵ�ͼƬ����
			// if (i < 3 && (!StringUtil.isEmpty(mList.get(t).getImageurl()) ||
			// mList.get(t) instanceof ItemImages)) {
			if (i < 3 && !StringUtil.isEmpty(mList.get(t).getImageurl())) {
				galleryList.add(mList.get(t));
				i++;
			} else {
				newsList.add(mList.get(t));
			}
		}
		// Ҫ�ж��Ƿ�������
		/*
		 * if (newsList.size() > 0){ newsList.add(0, newsList.get(0)); }
		 */
	}

	/**
	 * �������ݵĺ�̨�߳�
	 * 
	 * @author zuo
	 * 
	 */
	private class UpdateTask extends AsyncTask<Void, Void, ItemList> {

		@Override
		protected ItemList doInBackground(Void... params) {
			ItemList apiList = null;
			try {
				// ��ѯ�������ݲ���ӵ����ݼ�����
				MyApi api = new NewsSysApi();
				apiList = api.getNewsDescrList(mType, 1);
			} catch (Exception e) {
			}
			return apiList;
		}

		@Override
		protected void onPostExecute(ItemList result) {
			if (existData(result)) {
				AppLogger.i(mType + "��ȡ���������б����ݣ����ݴ�С: "
						+ result.getItems().size());
				// �������
				newsList.clear();
				galleryList.clear();
				// �ָ�����
				splitData2TwoList(result.getItems());

				// ������������
				cache.put(cacheKey, result);
				refreshTime = System.currentTimeMillis();
				
			} else {
				AppLogger.i(mType + "��ȡ���������б�����ʧ�ܻ���û��������");
				// if (newsList != null && newsList.size() !=
				// 0)Toast.makeText(activity,
				// activity.getString(R.string.noHaveNews), 0).show();
			}
			loadComplete();
		}
	}

	/**
	 * �������ݵĺ�̨�߳�
	 * 
	 * @author zuo
	 * 
	 */
	private class OldDataTask extends AsyncTask<Void, Void, ItemList> {

		@Override
		protected ItemList doInBackground(Void... params) {
			ItemList apiList = null;
			try {
				page++; // ��ѯ��һҳ
				// ��ѯ�������ݲ���ӵ����ݼ�����
				MyApi api = new NewsSysApi();
				apiList = api.getNewsDescrList(mType, page);
			} catch (Exception e) {
			}
			return apiList;
		}

		@Override
		protected void onPostExecute(ItemList result) {
			if (existData(result)) {
				AppLogger.i(mType + "��ȡ��ǰ�����б����ݣ����ݴ�С: "
						+ result.getItems().size());
				// ĩβ�������
				for (Item i : result.getItems()) {
					newsList.add(i);
				}
			} else {
				page--; // ��ѯʧ�ܣ��ָ�ԭ����ҳ��
				AppLogger.i(mType + "��ȡ��ǰ�����б�����ʧ��");
				Toast.makeText(mActivity,
						mActivity.getString(R.string.noHaveNews), 0).show();
			}
			loadComplete();
		}
	}

	/**
	 * �ж�ItemList�������Ƿ�������
	 * 
	 * @param itemList
	 * @return
	 */
	private boolean existData(ItemList itemList) {
		if (itemList != null && itemList.getItems() != null
				&& itemList.getItems().size() != 0) {
			return true;
		}
		return false;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(KEY_CONTENT, mType);
	}
}
