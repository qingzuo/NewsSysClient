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
 * 用户新闻收藏信息管理
 * @author zuo
 *
 */
public class UserCollectManager {
	
	/**
	 * 缓存用户的收藏信息
	 */
	public static void cacheUserCollectFromNet() {
		// 登陆用户
		new AsyncTask<Void, Integer, ItemList>() {

			@Override
			protected ItemList doInBackground(Void... params) {
				// 网络获取数据
				List<Item> collectList = UserHelper.getCollectList();
				if (collectList != null) {	// 有数据则缓存
					AppLogger.i("缓存用户收藏新闻，大小：" + collectList.size());
					CacheApi cache = new Cache();
					UserInfo userInfo = UserInfoManager.getUserInfo();
					if (userInfo != null) {
						String mCollectCachekey = Cache.USERCOLLECTLISTBYUSERID + userInfo.getUserID();
						// 缓存收藏数据
						cache.put(mCollectCachekey, new ItemList(collectList));
					}else {	// 没有拿到用户信息不进行后面代码
						return null;
					}
					// 添加收藏标记到缓存中
					String mCollectFlagCachekey = null;
					int COLLECT = 1;	// 已经收藏
					for (Item item : collectList) {
						mCollectFlagCachekey = Cache.USERCOLLECTFLAGBYNEWSIDANDUSERID + "-newsid" + item.getId() + "-userid" + userInfo.getUserID();
						cache.putInt(mCollectFlagCachekey, COLLECT);
					}
					// 缓存用户评论列表
					UserCommentManager.cahceUserCommentListFromeNet("" + userInfo.getUserID());
				}
				return null;
			}
		}.execute();
	}
	
	/**
	 * 获取用户缓存的收藏新闻集合
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
				AppLogger.i("获取用户缓存的收藏新闻集合，大小：" + itemList.getItems().size());
			}
		}
		
		return itemList;
	}
}
