package edu.jsu.newssysclient.util.local;

import java.util.List;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;

import android.os.AsyncTask;
import edu.jsu.newssysclient.bean.Item;
import edu.jsu.newssysclient.bean.ItemList;
import edu.jsu.newssysclient.dao.cache.Cache;
import edu.jsu.newssysclient.dao.cache.CacheApi;
import edu.jsu.newssysclient.debug.AppLogger;
import edu.jsu.newssysclient.util.net.UserHelper;

/**
 * 用户所有评论信息管理
 * @author zuo
 *
 */
public class UserCommentManager {

	/**
	 * 从服务器获取用户评论过的新闻并缓存
	 * @param userId
	 */
	public static void cahceUserCommentListFromeNet(String userId) {
		if (userId == null ) return ;
		new AsyncTask<String, Integer, ItemList>() {

			@Override
			protected ItemList doInBackground(String... params) {
				List<Item> commentList = UserHelper.getCommentist();
				if (commentList != null) {
					AppLogger.i("后台线程获取用户评论，大小：" + commentList.size());
					CacheApi cache = new Cache();
					String mCommentListCacheKey = Cache.USERCOMMENTLISTBYUSERID + params[0];
					cache.put(mCommentListCacheKey, new ItemList(commentList));
				}
				return null;
			}
		}.execute(userId);
	}

	/**
	 * 获取用户评论的新闻
	 * @param userId
	 * @return
	 */
	public static ItemList getUserCommentListFromCache(String userId) {
		Cache cache = new Cache();
		String mCommentListCacheKey = Cache.USERCOMMENTLISTBYUSERID + userId;
		AppLogger.i("后台线程获取用户评论，UserID：" + userId);
		return cache.get(mCommentListCacheKey, ItemList.class);
	}
}
