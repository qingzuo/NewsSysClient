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
 * �û�����������Ϣ����
 * @author zuo
 *
 */
public class UserCommentManager {

	/**
	 * �ӷ�������ȡ�û����۹������Ų�����
	 * @param userId
	 */
	public static void cahceUserCommentListFromeNet(String userId) {
		if (userId == null ) return ;
		new AsyncTask<String, Integer, ItemList>() {

			@Override
			protected ItemList doInBackground(String... params) {
				List<Item> commentList = UserHelper.getCommentist();
				if (commentList != null) {
					AppLogger.i("��̨�̻߳�ȡ�û����ۣ���С��" + commentList.size());
					CacheApi cache = new Cache();
					String mCommentListCacheKey = Cache.USERCOMMENTLISTBYUSERID + params[0];
					cache.put(mCommentListCacheKey, new ItemList(commentList));
				}
				return null;
			}
		}.execute(userId);
	}

	/**
	 * ��ȡ�û����۵�����
	 * @param userId
	 * @return
	 */
	public static ItemList getUserCommentListFromCache(String userId) {
		Cache cache = new Cache();
		String mCommentListCacheKey = Cache.USERCOMMENTLISTBYUSERID + userId;
		AppLogger.i("��̨�̻߳�ȡ�û����ۣ�UserID��" + userId);
		return cache.get(mCommentListCacheKey, ItemList.class);
	}
}
