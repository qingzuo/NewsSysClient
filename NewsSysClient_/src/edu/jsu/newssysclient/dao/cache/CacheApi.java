package edu.jsu.newssysclient.dao.cache;

import java.io.Serializable;

/**
 * ����ӿ�
 * 	���ܣ���������
 * @author zuo
 *
 */
public interface CacheApi {

	/**
	 * �������кŵĶ���
	 * @param key	��ʶ��
	 * @param returnType	��������
	 * @return	����ָ������
	 */
	public abstract <T extends Serializable> T get(String key,
			Class<T> returnType);

	/**
	 * ��ȡϵ���л��Ķ���
	 * @param key
	 * @param value
	 */
	public abstract void put(String key, Serializable value);

	/**
	 * ��������
	 * @param key
	 * @param value
	 */
	public abstract void putInt(String key, int value);

	/**
	 * �������кŵĶ���
	 * @param key	��ʶ��
	 * @param returnType	��������
	 * @return	����ָ������
	 */
	public abstract int getInt(String key);

	/**
	 * �������кŵĶ���
	 * @param key	��ʶ��
	 * @param returnType	��������
	 * @return	����ָ������
	 */
	public abstract void putBoolean(String key, boolean value);

	/**
	 * �������кŵĶ���
	 * @param key	��ʶ��
	 * @param returnType	��������
	 * @return	����ָ������
	 */
	public abstract boolean getBolean(String key);

	/**
	 * �������кŵĶ���
	 * @param key	��ʶ��
	 * @param returnType	��������
	 * @return	����ָ������
	 */
	public abstract void putString(String key, String value);

	/**
	 * �������кŵĶ���
	 * @param key	��ʶ��
	 * @param returnType	��������
	 * @return	����ָ������
	 */
	public abstract String getString(String key);

	/**
	 * �������кŵĶ���
	 * @param key	��ʶ��
	 * @param returnType	��������
	 * @return	����ָ������
	 */
	public abstract void delete(String key);

	/**
	 * �������кŵĶ���
	 * @param key	��ʶ��
	 * @param returnType	��������
	 * @return	����ָ������
	 */
	public abstract boolean exite(String key);

}