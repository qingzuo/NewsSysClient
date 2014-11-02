package edu.jsu.newssysclient.dao.cache;

import java.io.Serializable;

import org.androidannotations.annotations.EBean;

import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import edu.jsu.newssysclient.MyApplication;
import edu.jsu.newssysclient.debug.AppLogger;

/**
 * 持久化的缓存系统
 * 
 * @author zuo
 * 
 */
@EBean
public class Cache implements CacheApi {
	public static final String NEWSLISTBYTYPEID = "edu.jsu.newssysclient.dao.cache.cache.newslist";
	public static final String NEWSINFOBYNEWSID = "edu.jsu.newssysclient.dao.cache.cache.newsinfo:";	// NEWSINFOBYID+ID
	public static final String NEWSCOMMENTLISTBYNEWSID = "edu.jsu.newssysclient.dao.cache.cache.commentlist:";	// COMMENTLISTBYID+ID
	public static final String NEWSIMAGENEWSLISTBYNEWSID = "edu.jsu.newssysclient.dao.cache.keystring.imagenewslistbynewsid";
	// User key
	public static final String USERINFO = "edu.jsu.newssysclient.dao.cache.cache.userinfo";
	public static final String USERAUTOLOGINFLAGBYUSERID = "edu.jsu.newssysclient.dao.cache.keystring.autologinflagbyuserid";
	public static final String USERREMEMBERPWDFLAGBYUSERID = "edu.jsu.newssysclient.dao.cache.keystring.autologinflagbyuserid";
	// User collect key
	public static final String USERCOLLECTLISTBYUSERID = "edu.jsu.newssysclient.dao.cache.cache.collectlistbyid";
	public static final String USERCOLLECTFLAGBYNEWSIDANDUSERID = "edu.jsu.newssysclient.dao.cache.cache.collectflagbynewsidanduserid";
	// User Comment key
	public static final String USERCOMMENTLISTBYUSERID = "edu.jsu.newssysclient.dao.cache.cache.commentlistbyid";

	/**
	 * 保存序列号的对象
	 * @param key	标识符
	 * @param returnType	返回类型
	 * @return	返回指定类型
	 */
	@Override
	public <T extends Serializable> T get(String key, Class<T> returnType) {
		T res = null;
		try {
			DB db = DBFactory.open(MyApplication.getInstance());
			// 
			res = db.get(key, returnType);

			db.close();
		} catch (SnappydbException e) {
			e.printStackTrace();
		}
		
		return res;
	}
	

	/**
	 * 保存序列号的对象
	 * @param key	标识符
	 * @param returnType	返回类型
	 * @return	返回指定类型
	 */
	@Override
	public void put(String key, Serializable value) {
		if (value == null) return;
		try {
			DB db = DBFactory.open(MyApplication.getInstance());
			// 
			db.put(key, value);

			db.close();
		} catch (SnappydbException e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * 保存序列号的对象
	 * @param key	标识符
	 * @param returnType	返回类型
	 * @return	返回指定类型
	 */
	@Override
	public void putInt(String key, int value) {
		if (key == null) {
			AppLogger.i("缓存类，保存整数，key为空");
			return ;
		}
		try {
			DB db = DBFactory.open(MyApplication.getInstance());
			// 
			db.putInt(key, value);

			db.close();
		} catch (SnappydbException e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * 保存序列号的对象
	 * @param key	标识符
	 * @param returnType	返回类型
	 * @return	返回指定类型
	 */
	@Override
	public int getInt(String key) {
		int val = 0;
		try {
			DB db = DBFactory.open(MyApplication.getInstance());
			// 
			val = db.getInt(key);

			db.close();
		} catch (SnappydbException e) {
			e.printStackTrace();
		}
		return val;
	}
	

	/**
	 * 保存序列号的对象
	 * @param key	标识符
	 * @param returnType	返回类型
	 * @return	返回指定类型
	 */
	@Override
	public void putBoolean(String key, boolean value) {
		try {
			DB db = DBFactory.open(MyApplication.getInstance());
			// 
			db.putBoolean(key, value);

			db.close();
		} catch (SnappydbException e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * 保存序列号的对象
	 * @param key	标识符
	 * @param returnType	返回类型
	 * @return	返回指定类型
	 */
	@Override
	public boolean getBolean(String key) {
		boolean val = false;
		try {
			DB db = DBFactory.open(MyApplication.getInstance());
			// 
			val = db.getBoolean(key);

			db.close();
		} catch (SnappydbException e) {
			e.printStackTrace();
		}
		return val;
	}
	

	/**
	 * 保存序列号的对象
	 * @param key	标识符
	 * @param returnType	返回类型
	 * @return	返回指定类型
	 */
	@Override
	public void putString(String key, String value) {
		try {
			DB db = DBFactory.open(MyApplication.getInstance());
			// 
			db.put(key, value);

			db.close();
		} catch (SnappydbException e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * 保存序列号的对象
	 * @param key	标识符
	 * @param returnType	返回类型
	 * @return	返回指定类型
	 */
	@Override
	public String getString(String key) {
		String val = null;
		try {
			DB db = DBFactory.open(MyApplication.getInstance());
			// 
			val = db.get(key);

			db.close();
		} catch (SnappydbException e) {
			e.printStackTrace();
		}
		return val;
	}
	

	/**
	 * 保存序列号的对象
	 * @param key	标识符
	 * @param returnType	返回类型
	 * @return	返回指定类型
	 */
	@Override
	public void delete(String key) {
		try {
			DB db = DBFactory.open(MyApplication.getInstance());
			// 
			db.del(key);

			db.close();
		} catch (SnappydbException e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * 保存序列号的对象
	 * @param key	标识符
	 * @param returnType	返回类型
	 * @return	返回指定类型
	 */
	@Override
	public boolean exite(String key) {
		boolean val = false;
		try {
			DB db = DBFactory.open(MyApplication.getInstance());
			// 
			val = db.exists(key);

			db.close();
		} catch (SnappydbException e) {
			e.printStackTrace();
		}
		return val;
	}
}
