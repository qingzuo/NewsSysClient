package edu.jsu.newssysclient.util.net;

import java.util.List;

import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Rest;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import edu.jsu.newssysclient.URLHelper;
import edu.jsu.newssysclient.bean.FollowList;
import edu.jsu.newssysclient.bean.Item;
import edu.jsu.newssysclient.bean.ItemList;
import edu.jsu.newssysclient.debug.AppLogger;

/**
 * 访问服务器接口
 * @author zuo
 *
 */
public class NewsSysApi implements MyApi {

	/**
	 * type:
	 * page:
	 */
	@Override
	public ItemList getNewsDescrList(String type, int page){
		try {
//			type = "1";
			AppLogger.v(URLHelper.NEWSLIST + "page="+page+"&typeid="+type);
//			URLHelper.NEWSLIST = "http://172.18.125.209:8080/news/news_androidList.action?";
			return new ItemList(XmlHelper.getNewsDescrList(IOStream.getIOStreamFromUrl(URLHelper.NEWSLIST + "page="+page+"&typeid="+type)));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public ItemList getNewsDescrList(String url){
		try {
//			type = "1";
			AppLogger.v(url);
			return new ItemList(XmlHelper.getNewsDescrList(IOStream.getIOStreamFromUrl(url)));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.jsu.newssysclient.dao.net.MyApi#getNewsCommentList(java.lang.String)
	 */
	@Override
	public FollowList getNewsCommentList(String id){
//		return getXmlDate(URLHelper.NEWSCOMMENTS + id, FollowList.class);
		try {
			return new FollowList(XmlHelper.getCommentList(IOStream.getIOStreamFromUrl(URLHelper.NEWSCOMMENTS + id)));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String getString(String url){
		RestTemplate restTemplate = new RestTemplate();

		// 添加字符串消息转换器
		restTemplate.getMessageConverters().add(new SimpleXmlHttpMessageConverter());

		return ""; //restTemplate.getForEntity(url, String.class);
	}

	private <T> T getXmlDate(String url, Class<T> class1) {
		RestTemplate restTemplate = new RestTemplate();

		// 添加字符串消息转换器
		restTemplate.getMessageConverters().add(new SimpleXmlHttpMessageConverter());

		return restTemplate.getForObject(url, class1);
	}
}
