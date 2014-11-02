package edu.jsu.newssysclient.ui.menu;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import edu.jsu.newssysclient.R;
import edu.jsu.newssysclient.adapter.CollectListAdapter;
import edu.jsu.newssysclient.adapter.ShowNewsListAdapter;
import edu.jsu.newssysclient.bean.Item;
import edu.jsu.newssysclient.bean.ItemList;
import edu.jsu.newssysclient.bean.UserInfo;
import edu.jsu.newssysclient.dao.cache.Cache;
import edu.jsu.newssysclient.debug.AppLogger;
import edu.jsu.newssysclient.ui.main.MyBaseActivity;
import edu.jsu.newssysclient.ui.main.ShowNewsActivity;
import edu.jsu.newssysclient.util.local.UserInfoManager;
import edu.jsu.newssysclient.util.net.NetUtil;
import edu.jsu.newssysclient.util.net.UserHelper;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

/**
 * �ղؽ��棬չʾ���е�ǰ�û��ղص�����
 * δ��ɹ��ܣ�
 * 	ɾ���ղع���
 * @author zuo
 *
 */
@EActivity(R.layout.collect_layout)
public class CollectActivity extends MyBaseActivity {
	List<Item> mCollectList = new ArrayList<Item>(); // ��������
	private CollectListAdapter mAdapter;
	private Cache mCache = new Cache(); // ����
	private String mCollectCachekey;
	private UserInfo mUserInfo;

	@ViewById
	ListView lv_collect_list;
	
	@Override
	protected void onStart() {
		super.onStart();
		mUserInfo = UserInfoManager.getUserInfo();
		mCollectCachekey = Cache.USERCOLLECTLISTBYUSERID + mUserInfo.getUserID();
		ItemList cacheList = mCache.get(mCollectCachekey, ItemList.class);
		if (cacheList != null && cacheList.getItems() != null && cacheList.getItems().size() != 0){
			mCollectList.clear();
			for (Item i : cacheList.getItems()) {
				mCollectList.add(i);
			}
		}
		mAdapter = new CollectListAdapter(this, mCollectList);
		lv_collect_list.setAdapter(mAdapter);
		lv_collect_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(mContext,
						ShowNewsActivity.class);
				// ��������id
				intent.putExtra(ShowNewsActivity.SHOWNEWSACTIVITYNEWSID, mCollectList.get(position).getId());
				AppLogger.i("�ղ��б���ID��������ϸ�����ID: " + mCollectList.get(position).getId());
				startActivity(intent);
			}
		});
		
		if (verify()) {
			getCollectListFromNet();
		}
	}
	
	@Background
	void getCollectListFromNet() { 
		List<Item> mList = UserHelper.getCollectList();
		if (mList != null && mList.size() != 0) {
			// ��������
			updateUi(mList);
		}
	}
	
	@UiThread
	void updateUi(List<Item> mList) { 
		// ��������
		mCache.put(mCollectCachekey, new ItemList(mList));
		// �������
		mCollectList.clear();
		for (Item i : mList) {
			mCollectList.add(i);
		}
		// ����
		if (mAdapter != null) mAdapter.notifyDataSetChanged();
	}

	/**
	 * ��֤
	 * @return 
	 */
	private boolean verify() {
		if (!NetUtil.canConnectNet()) {
			return false;
		}
		return true;
	}
	
	
}
