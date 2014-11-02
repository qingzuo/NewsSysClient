package edu.jsu.newssysclient.util.net;

import edu.jsu.newssysclient.bean.FollowList;
import edu.jsu.newssysclient.bean.ItemList;

/**
 * 访问服务器获取新闻列表和新闻评论的接口
 * @author zuo
 *
 */
public interface MyApi {

	/**
	 * 获取新闻列表的接口
	 * @param type	新闻类型
	 * @param page	查询页数
	 * @return	包含新闻列表的ItemList对象
	 */
	public abstract ItemList getNewsDescrList(String type, int page);

	/**
	 * 获取新闻评论列表的接口
	 * @param newsId	新闻ID
	 * @return	包含评论列表的FollowList对象
	 */
	public abstract FollowList getNewsCommentList(String newsId);

}