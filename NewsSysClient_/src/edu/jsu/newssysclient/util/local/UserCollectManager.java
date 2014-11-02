package edu.jsu.newssysclient.util.local;

import java.util.List;

import android.os.AsyncTask;
import edu.jsu.newssysclient.bean.Item;
import edu.jsu.newssysclient.bean.ItemList;
import edu.jsu.newssysclient.bean.UserInfo;
import edu.jsu.newssysclient.dao.cache.Cache;
import edu.jsu.newssysclient.dao.cache.CacheApi;
import edu.jsu.newssysclient.debug.AppLogger;
import edu.jsu.newssysclient.util.net.UserHelper;

/**
 * �û������ղ���Ϣ����
 * @author zuo
 *
 */
public class UserCollectManager {
	
	/**
	 * �����û����ղ���Ϣ
	 */
	public static void cacheUserCollectFromNet() {
		// ��½�û�
		new AsyncTask<Void, Integer, ItemList>() {

			@Override
			protected ItemList doInBackground(Void... params) {
				// �����ȡ����
				List<Item> collectList = UserHelper.getCollectList();
				if (collectList != null) {	// �������򻺴�
					AppLogger.i("�����û��ղ����ţ���С��" + collectList.size());
					CacheApi cache = new Cache();
					UserInfo userInfo = UserInfoManager.getUserInfo();
					if (userInfo != null) {
						String mCollectCachekey = Cache.USERCOLLECTLISTBYUSERID + userInfo.getUserID();
						// �����ղ�����
						cache.put(mCollectCachekey, new ItemList(collectList));
					}else {	// û���õ��û���Ϣ�����к������
						return null;
					}
					// ����ղر�ǵ�������
					String mCollectFlagCachekey = null;
					int COLLECT = 1;	// �Ѿ��ղ�
					for (Item item : collectList) {
						mCollectFlagCachekey = Cache.USERCOLLECTFLAGBYNEWSIDANDUSERID + "-newsid" + item.getId() + "-userid" + userInfo.getUserID();
						cache.putInt(mCollectFlagCachekey, COLLECT);
					}
					// �����û������б�
					UserCommentManager.cahceUserCommentListFromeNet("" + userInfo.getUserID());
				}
				return null;
			}
		}.execute();
	}
	
	/**
	 * ��ȡ�û�������ղ����ż���
	 * @return ItemList
	 */
	public static ItemList getUserCollectFromCache(){
		ItemList itemList = null;
		
		UserInfo userInfo = UserInfoManager.getUserInfo();
		if (userInfo != null) {
			Cache cache = new Cache();
			String mCollectCachekey = Cache.USERCOLLECTLISTBYUSERID + userInfo.getUserID();
			itemList = cache.get(mCollectCachekey, ItemList.class);
			if (itemList != null && itemList.getItems() != null) {
				AppLogger.i("��ȡ�û�������ղ����ż��ϣ���С��" + itemList.getItems().size());
			}
		}
		
		return itemList;
	}
}
