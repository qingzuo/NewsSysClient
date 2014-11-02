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
 * 新闻列表 ，对于不同的新闻类型，查询不同的数据并显示
 * 显示特定类型的新闻Fragment
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
	private List<Item> newsList = new ArrayList<Item>(); // 新闻数据
	private List<Item> galleryList = new ArrayList<Item>(); // 头部滑动新闻
	private Cache cache = new Cache();; // 缓存
	private String cacheKey;
	private int page = 1; // 分页查询，默认第一页
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
		// 获取数据
		getDate();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LinearLayout v = (LinearLayout) View.inflate(mActivity,
				R.layout.show_news_list_layout, null);

		// 刷新控件的设置
		mPullRefreshListView = (PullToRefreshListView) v
				.findViewById(R.id.lv_refresh_all_new_listview);
		mPullRefreshListView.setMode(Mode.BOTH);
		mPullRefreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Item item = newsList.get(arg2 - 2);
				Intent intent = null;
				if (item instanceof ItemImages) { // 图片新闻
					ItemImages itemImages = (ItemImages) item;
					intent = new Intent(mActivity, GalleryUrlActivity.class);
					intent.putExtra(ShowNewsActivity.SHOWNEWSACTIVITYNEWSID,
							itemImages.getId());
					// 缓存数据据
					cache.put(
							Cache.NEWSIMAGENEWSLISTBYNEWSID
									+ itemImages.getId(), itemImages);
					startActivity(intent);
				} else { // 普通新闻
					IntentUtil.go2ShowNewsActivity(mActivity, item.getId());
				}
			}
		});

		// 列表末尾添加数据
		mPullRefreshListView
				.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

					@Override
					public void onLastItemVisible() {
						Toast.makeText(mActivity, "End of List!",
								Toast.LENGTH_SHORT).show();
						Toast.makeText(mActivity, "底部添加新睡觉", 0).show();
						// 刷新底部
						new OldDataTask().execute();
					}
				});

		// 列表开头添加数据
		mPullRefreshListView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {

					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						long nowTime = System.currentTimeMillis();
						long minute = (nowTime - refreshTime) / (60 * 1000);
						AppLogger.i("刷新数据：" + minute);
						// mPullRefreshListView.getLoadingLayoutProxy().setLastUpdatedLabel(""
						// + minute + "分钟以前刷新");
						// 判断是否不久缓存过
						if (!isRefreshing) {
							isRefreshing = true;
							if (mPullRefreshListView.isHeaderShown()) { // 头部刷新
								mPullRefreshListView.getLoadingLayoutProxy()
										.setRefreshingLabel("正在加载");
								mPullRefreshListView.getLoadingLayoutProxy()
										.setPullLabel("上拉加载更多");
								mPullRefreshListView.getLoadingLayoutProxy()
										.setReleaseLabel("释放开始加载");

								new UpdateTask().execute();
							} else if (mPullRefreshListView.isFooterShown()) { // 底部刷新
								mPullRefreshListView.getLoadingLayoutProxy()
										.setRefreshingLabel("正在刷新");
								mPullRefreshListView.getLoadingLayoutProxy()
										.setPullLabel("下拉刷新");
								mPullRefreshListView.getLoadingLayoutProxy()
										.setReleaseLabel("释放开始刷新");
								new OldDataTask().execute();
							}
						} else {
							loadComplete();
						}
					}
				});

		ListView actualListView = mPullRefreshListView.getRefreshableView();

		// 设置头部图片滚动
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
		viewFlow.setSelection(circleSize * 1000); // 设置初始位置
		viewFlow.startAutoFlowTimer(); // 启动自动播放
		viewFlow.setParent(head);

		// 添加到ListView头部
		if (head != null) {
			actualListView.addHeaderView(head);
		}

		// 创建adapter
		mAdapter = new ShowNewsListAdapter(mActivity, newsList, galleryList);
		actualListView.setAdapter(mAdapter);

		boolean pauseOnScroll = false; //
		boolean pauseOnFling = true; // 快速滑动时暂停加载图片
		PauseOnScrollListener listener = new PauseOnScrollListener(
				mAdapter.getImageLoader(), pauseOnScroll, pauseOnFling);
		actualListView.setOnScrollListener(listener);
		

		return v;
	}

	/**
	 * 刷新完毕
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
	 * 获取数据
	 */
	public void getDate() {
		// 获取缓存数据
		getCacheData();
		// 后台线程获取最新数据
		new UpdateTask().execute();
	}

	/**
	 * 获取缓存数据
	 */
	private void getCacheData() {
		ItemList cacheList = cache.get(cacheKey, ItemList.class);
		if (existData(cacheList)) {
			newsList.clear();
			galleryList.clear();

			// 分割数据
			splitData2TwoList(cacheList.getItems());

			if (mAdapter != null && cacheList.getItems().size() != 0) {
				mAdapter.notifyDataSetChanged();
			}
			AppLogger.i(mType + "获取缓存新闻列表数据，数据大小: "
					+ cacheList.getItems().size());
		}
	}

	/**
	 * 分割数据向两个数据集合填充数据
	 * 
	 * @param mList
	 */
	private void splitData2TwoList(List<Item> mList) {
		for (int i = 0, t = 0; t < mList.size(); t++) {
			// AppLogger.i(t + "这是图片地址：" + mList.get(t).getImageurl());
			// 选择最前面三个的有图片的新闻，这里可能没有考虑到图片新闻
			// if (i < 3 && (!StringUtil.isEmpty(mList.get(t).getImageurl()) ||
			// mList.get(t) instanceof ItemImages)) {
			if (i < 3 && !StringUtil.isEmpty(mList.get(t).getImageurl())) {
				galleryList.add(mList.get(t));
				i++;
			} else {
				newsList.add(mList.get(t));
			}
		}
		// 要判断是否有数据
		/*
		 * if (newsList.size() > 0){ newsList.add(0, newsList.get(0)); }
		 */
	}

	/**
	 * 更新数据的后台线程
	 * 
	 * @author zuo
	 * 
	 */
	private class UpdateTask extends AsyncTask<Void, Void, ItemList> {

		@Override
		protected ItemList doInBackground(Void... params) {
			ItemList apiList = null;
			try {
				// 查询最新数据并添加到数据集合中
				MyApi api = new NewsSysApi();
				apiList = api.getNewsDescrList(mType, 1);
			} catch (Exception e) {
			}
			return apiList;
		}

		@Override
		protected void onPostExecute(ItemList result) {
			if (existData(result)) {
				AppLogger.i(mType + "获取最新新闻列表数据，数据大小: "
						+ result.getItems().size());
				// 添加数据
				newsList.clear();
				galleryList.clear();
				// 分割数据
				splitData2TwoList(result.getItems());

				// 缓存最新数据
				cache.put(cacheKey, result);
				refreshTime = System.currentTimeMillis();
				
			} else {
				AppLogger.i(mType + "获取最新新闻列表数据失败或者没有新数据");
				// if (newsList != null && newsList.size() !=
				// 0)Toast.makeText(activity,
				// activity.getString(R.string.noHaveNews), 0).show();
			}
			loadComplete();
		}
	}

	/**
	 * 更新数据的后台线程
	 * 
	 * @author zuo
	 * 
	 */
	private class OldDataTask extends AsyncTask<Void, Void, ItemList> {

		@Override
		protected ItemList doInBackground(Void... params) {
			ItemList apiList = null;
			try {
				page++; // 查询下一页
				// 查询最新数据并添加到数据集合中
				MyApi api = new NewsSysApi();
				apiList = api.getNewsDescrList(mType, page);
			} catch (Exception e) {
			}
			return apiList;
		}

		@Override
		protected void onPostExecute(ItemList result) {
			if (existData(result)) {
				AppLogger.i(mType + "获取以前新闻列表数据，数据大小: "
						+ result.getItems().size());
				// 末尾添加数据
				for (Item i : result.getItems()) {
					newsList.add(i);
				}
			} else {
				page--; // 查询失败，恢复原来的页号
				AppLogger.i(mType + "获取以前新闻列表数据失败");
				Toast.makeText(mActivity,
						mActivity.getString(R.string.noHaveNews), 0).show();
			}
			loadComplete();
		}
	}

	/**
	 * 判断ItemList对象中是否有数据
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
