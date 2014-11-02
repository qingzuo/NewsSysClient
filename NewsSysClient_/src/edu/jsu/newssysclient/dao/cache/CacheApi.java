package edu.jsu.newssysclient.dao.cache;

import java.io.Serializable;

/**
 * 缓存接口
 * 	功能：缓存数据
 * @author zuo
 *
 */
public interface CacheApi {

	/**
	 * 保存序列号的对象
	 * @param key	标识符
	 * @param returnType	返回类型
	 * @return	返回指定类型
	 */
	public abstract <T extends Serializable> T get(String key,
			Class<T> returnType);

	/**
	 * 获取系序列化的对象
	 * @param key
	 * @param value
	 */
	public abstract void put(String key, Serializable value);

	/**
	 * 保存整数
	 * @param key
	 * @param value
	 */
	public abstract void putInt(String key, int value);

	/**
	 * 保存序列号的对象
	 * @param key	标识符
	 * @param returnType	返回类型
	 * @return	返回指定类型
	 */
	public abstract int getInt(String key);

	/**
	 * 保存序列号的对象
	 * @param key	标识符
	 * @param returnType	返回类型
	 * @return	返回指定类型
	 */
	public abstract void putBoolean(String key, boolean value);

	/**
	 * 保存序列号的对象
	 * @param key	标识符
	 * @param returnType	返回类型
	 * @return	返回指定类型
	 */
	public abstract boolean getBolean(String key);

	/**
	 * 保存序列号的对象
	 * @param key	标识符
	 * @param returnType	返回类型
	 * @return	返回指定类型
	 */
	public abstract void putString(String key, String value);

	/**
	 * 保存序列号的对象
	 * @param key	标识符
	 * @param returnType	返回类型
	 * @return	返回指定类型
	 */
	public abstract String getString(String key);

	/**
	 * 保存序列号的对象
	 * @param key	标识符
	 * @param returnType	返回类型
	 * @return	返回指定类型
	 */
	public abstract void delete(String key);

	/**
	 * 保存序列号的对象
	 * @param key	标识符
	 * @param returnType	返回类型
	 * @return	返回指定类型
	 */
	public abstract boolean exite(String key);

}