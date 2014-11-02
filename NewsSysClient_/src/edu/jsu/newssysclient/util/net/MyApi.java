package edu.jsu.newssysclient.util.net;

import edu.jsu.newssysclient.bean.FollowList;
import edu.jsu.newssysclient.bean.ItemList;

/**
 * ���ʷ�������ȡ�����б���������۵Ľӿ�
 * @author zuo
 *
 */
public interface MyApi {

	/**
	 * ��ȡ�����б�Ľӿ�
	 * @param type	��������
	 * @param page	��ѯҳ��
	 * @return	���������б��ItemList����
	 */
	public abstract ItemList getNewsDescrList(String type, int page);

	/**
	 * ��ȡ���������б�Ľӿ�
	 * @param newsId	����ID
	 * @return	���������б��FollowList����
	 */
	public abstract FollowList getNewsCommentList(String newsId);

}