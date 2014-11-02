package edu.jsu.newssysclient.util.net;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.R.integer;
import android.util.Log;
import android.util.Xml;
import edu.jsu.newssysclient.bean.Follow;
import edu.jsu.newssysclient.bean.ImageNews;
import edu.jsu.newssysclient.bean.Item;
import edu.jsu.newssysclient.bean.ItemImages;
import edu.jsu.newssysclient.bean.NewsContent;
import edu.jsu.newssysclient.bean.NewsImage;
import edu.jsu.newssysclient.bean.NewsInfo;
import edu.jsu.newssysclient.bean.NewsLead;
import edu.jsu.newssysclient.bean.NewsLink;
import edu.jsu.newssysclient.bean.NewsText;
import edu.jsu.newssysclient.bean.NewsTimeSource;
import edu.jsu.newssysclient.bean.NewsTitle;
import edu.jsu.newssysclient.bean.NewsVideo;

/**
 * 从xml文件得到新闻数据
 * 
 * @author zuo
 * 
 */
public class XmlHelper {

	/**
	 * 返回新闻集合
	 * 
	 * @param is
	 *            连接服务器的InputStream
	 * @return 包含所有新闻的List集合
	 * @throws Exception
	 */
	public static List<NewsInfo> getNewsInfos(InputStream is) throws Exception {
		List<NewsInfo> newsInfos = null; // 保存所有的新闻
		NewsInfo news = null; // 新闻类，包括下面几个类
		NewsTitle title = null; // 标题
		NewsTimeSource timeSource = null; // 时间来源
		NewsLink link = null; // 链接
		NewsLead lead = null; // 导语
		NewsImage image = null; // 图片
		NewsVideo video = null; // 视频
		NewsContent content = null; // 新闻内容
		Follow follow = null; // 新闻跟帖

		// 由android.util.Xml创建一个XmlPullParser实例
		XmlPullParser parser = Xml.newPullParser();
		// 设置输入流 并指明编码方式
		parser.setInput(is, "UTF-8");
		// 产生第一个事件
		int eventType = parser.getEventType();

		while (eventType != XmlPullParser.END_DOCUMENT) {

			switch (eventType) {
			// 判断当前事件是否为文档开始事件
			case XmlPullParser.START_DOCUMENT:
				newsInfos = new ArrayList<NewsInfo>(); // 初始化集合
				break;

			// 判断当前事件是否为标签元素开始事件
			case XmlPullParser.START_TAG:
				if (parser.getName().equals("news")) {
					news = new NewsInfo(); // 初始化一个新闻
				} else if (parser.getName().equals("id")) {
					eventType = parser.next();
//					news.setId(parser.getText().trim());
					if (parser.getText() == null) {
						news.setId("1");
					} else {
						news.setId(parser.getText().trim());
						Log.w("测试：", parser.getText().trim());
					}
				} else if (parser.getName().equals("title")) {
					title = new NewsTitle();
				} else if (parser.getName().equals("titlecontent")) {
					eventType = parser.next();
//					title.setText(parser.getText().trim());
					if (parser.getText() == null) {
						title.setText("");
					} else {
						title.setText(parser.getText().trim());
						Log.w("测试：", parser.getText().trim());
					}
				} else if (parser.getName().equals("timesource")) {
					timeSource = new NewsTimeSource();
				} else if (parser.getName().equals("timesourcecontent")) {
					eventType = parser.next();
//					timeSource.setText(parser.getText().trim());
					if (parser.getText() == null) {
						timeSource.setText("刚刚   来源：悦新闻");
					} else {
						timeSource.setText(parser.getText().trim());
						Log.w("测试：", parser.getText().trim());
					}
				} else if (parser.getName().equals("link")) {
					link = new NewsLink();
				} else if (parser.getName().equals("linkcontent")) {
					eventType = parser.next();
//					link.setText(parser.getText().trim());
					if (parser.getText() == null) {
						link.setText("");
					} else {
						link.setText(parser.getText().trim());
						Log.w("测试：", parser.getText().trim());
					}
				} else if (parser.getName().equals("lead")) {
					lead = new NewsLead();
				} else if (parser.getName().equals("leadcontent")) {
					eventType = parser.next();
//					lead.setText(parser.getText().trim());
					if (parser.getText() == null) {
						lead.setText("");
					} else {
						lead.setText(parser.getText().trim());
						Log.w("测试：", parser.getText().trim());
					}
				} else if (parser.getName().equals("image")) {
					image = new NewsImage();
				} else if (parser.getName().equals("imageurl")) {
					eventType = parser.next();
//					image.setSource(parser.getText().trim());
					if (parser.getText() == null) {
						image.setSource("");
					} else {
						image.setSource(parser.getText().trim());
						Log.w("测试：", parser.getText().trim());
					}
//					image.setSource("http://pic.baike.soso.com/p/20111220/20111220184359-500271905.jpg");
				} else if (parser.getName().equals("imagenote")) {
					eventType = parser.next();
//					image.setNote(parser.getText().trim());
					if (parser.getText() == null) {
						image.setNote("");
					} else {
						image.setNote(parser.getText().trim());
						Log.w("测试：", parser.getText().trim());
					}
//					image.setNote("NULL");
				}else if (parser.getName().equals("imageheight")) {
					eventType = parser.next();
//					image.setHeight(Integer.parseInt(parser.getText().trim()));
					if (parser.getText() == null) {
						image.setHeight(150);
					} else {
						image.setHeight(Integer.parseInt(parser.getText().trim()));
						Log.w("测试：", parser.getText().trim());
					}
				}else if (parser.getName().equals("imagewidth")) {
					eventType = parser.next();
//					image.setWidth(Integer.parseInt(parser.getText().trim()));
					if (parser.getText() == null) {
						image.setWidth(250);
					} else {
						image.setWidth(Integer.parseInt(parser.getText().trim()));
						Log.w("测试：", parser.getText().trim());
					}
				} else if (parser.getName().equals("video")) {
					video = new NewsVideo();
				} else if (parser.getName().equals("source")) {
					eventType = parser.next();
//					video.setSource(parser.getText().trim());
					if (parser.getText() == null) {
						video.setSource("悦新闻");
					} else {
						video.setSource(parser.getText().trim());
						Log.w("测试：", parser.getText().trim());
					}
				} else if (parser.getName().equals("screenshot")) {
					eventType = parser.next();
//					video.setScreenshot(parser.getText().trim());
					if (parser.getText() == null) {
						video.setScreenshot("");
					} else {
						video.setScreenshot(parser.getText().trim());
						Log.w("测试：", parser.getText().trim());
					}
				} else if (parser.getName().equals("videonote")) {
					eventType = parser.next();
//					video.setNote(parser.getText().trim());
					if (parser.getText() == null) {
						video.setNote("");
					} else {
						video.setNote(parser.getText().trim());
						Log.w("测试：", parser.getText().trim());
					}
				} else if (parser.getName().equals("newstext")) {
					content = new NewsContent();
				} else if (parser.getName().equals("newscontent")) {
					eventType = parser.next();
//					content.setText(parser.getText().trim());
					if (parser.getText() == null) {
						content.setText("无");
					} else {
						content.setText(parser.getText().trim());
						Log.w("测试：", parser.getText().trim());
					}
				} else if (parser.getName().equals("follow")) {
					follow = new Follow();
				} else if (parser.getName().equals("followuser")) {
					eventType = parser.next();
//					follow.setFollowuser(parser.getText().trim());
					if (parser.getText() == null) {
						follow.setFollowuser("Android用户");
					} else {
						follow.setFollowuser(parser.getText().trim());
						Log.w("测试：", parser.getText().trim());
					}
				} else if (parser.getName().equals("followtime")) {
					eventType = parser.next();
//					follow.setFollowtime(parser.getText().trim());
					if (parser.getText() == null) {
						follow.setFollowtime("刚刚");
					} else {
						follow.setFollowtime(parser.getText().trim());
						Log.w("测试：", parser.getText().trim());
					}
				} else if (parser.getName().equals("followcontent")) {
					eventType = parser.next();
//					follow.setFollowcontent(parser.getText().trim());
					if (parser.getText() == null) {
						follow.setFollowcontent("");
					} else {
						follow.setFollowcontent(parser.getText().trim());
						Log.w("测试：", parser.getText().trim());
					}
				}
				break;

			// 判断当前事件是否为标签元素结束事件
			case XmlPullParser.END_TAG:
				if (parser.getName().equals("news")) { // 判断结束标签元素是否是news
					newsInfos.add(news);
					news = null;
				} else if (parser.getName().equals("title")) { // 判断结束标签元素是否是title
					news.getContent().add(title);
					title = null;
				} else if (parser.getName().equals("timesource")) { // 判断结束标签元素是否是title
					news.getContent().add(timeSource);
					timeSource = null;
				} else if (parser.getName().equals("link")) { // 判断结束标签元素是否是title
					news.getContent().add(link);
					link = null;
				} else if (parser.getName().equals("lead")) { // 判断结束标签元素是否是title
					news.getContent().add(lead);
					lead = null;
				} else if (parser.getName().equals("image")) { // 判断结束标签元素是否是title
					news.getContent().add(image);
					image = null;
				} else if (parser.getName().equals("video")) { // 判断结束标签元素是否是title
					news.getContent().add(video);
					video = null;
				} else if (parser.getName().equals("newstext")) { // 判断结束标签元素是否是title
					news.getContent().add(content);
					content = null;
				} else if (parser.getName().equals("follow")) { // 判断结束标签元素是否是title
					news.getContent().add(follow);
					follow = null;
				}
				break;
			}
			// 进入下一个元素并触发相应事件
			eventType = parser.next();
		}

		return newsInfos;
	}
	
	/**
	 * 从xml文件中获取新闻列表
	 * @param is	xml文件流
	 * @return		包含NewsDescr的List
	 * @throws Exception
	 */
	public static List<Item> getNewsDescrList(InputStream is)
			throws Exception {
		List<Item> newList = null;
		Item descr = null;
		ItemImages itemImages = null;
		ImageNews imageNews = null;

		// 由android.util.Xml创建一个XmlPullParser实例
		XmlPullParser parser = Xml.newPullParser();
		// 设置输入流 并指明编码方式
		parser.setInput(is, "UTF-8");
		// 产生第一个事件
		int eventType = parser.getEventType();

		while (eventType != XmlPullParser.END_DOCUMENT) {

			switch (eventType) {
			// 判断当前事件是否为文档开始事件
			case XmlPullParser.START_DOCUMENT:
				newList = new ArrayList<Item>();
				break;

			// 判断当前事件是否为标签元素开始事件
			case XmlPullParser.START_TAG:
				if (parser.getName().equals("item")) {
					descr = new Item();
				} else if (parser.getName().equals("id")) {
					eventType = parser.next();
//					descr.setId(parser.getText().trim());
					if (parser.getText() == null) {
						descr.setId("1");
					} else {
						descr.setId(parser.getText().trim());
						Log.w("测试：", parser.getText().trim());
					}
				} else if (parser.getName().equals("title")) {
					eventType = parser.next();
					if (parser != null && parser.getText() != null && descr != null) {
						descr.setTitle(parser.getText().trim());
						Log.w("测试：", parser.getText().trim());
					} else {
						descr.setTitle("无标题");
					}
				} else if (parser.getName().equals("imageurl")) {
					eventType = parser.next();
					descr.setImageurl(parser.getText().trim());
					if (parser.getText() == null) {
						descr.setImageurl("");
					} else {
						descr.setImageurl(parser.getText().trim());
						Log.w("测试：", parser.getText().trim());
					}
				} else if (parser.getName().equals("descr")) {
					eventType = parser.next();
//					descr.setDescr(parser.getText().trim());
					if (parser.getText() == null) {
						descr.setDescr("");
					} else {
						descr.setDescr(parser.getText().trim());
						Log.w("测试：", parser.getText().trim());
					}
				} else if (parser.getName().equals("browsetime")) {
					eventType = parser.next();
//					descr.setBrowsetime(Integer.parseInt(parser.getText().trim()));
					if (parser.getText() == null) {
						descr.setBrowsetime(0);
					} else {
						descr.setBrowsetime(Integer.parseInt(parser.getText().trim()));
						Log.w("测试：", parser.getText().trim());
					}
				} else if (parser.getName().equals("commenttime")) {
					eventType = parser.next();
//					descr.setCommenttime(Integer.parseInt(parser.getText().trim()));
					if (parser.getText() == null) {
						descr.setCommenttime(0);
					} else {
						descr.setCommenttime(Integer.parseInt(parser.getText().trim()));
						Log.w("测试：", parser.getText().trim());
					}
				} else if (parser.getName().equals("ItemImages")) {
					itemImages = new ItemImages();
				} else if (parser.getName().equals("ItemImagesid")) {
					eventType = parser.next();
//					itemImages.setId(parser.getText().trim());
					if (parser.getText() == null) {
						itemImages.setId("1");
					} else {
						itemImages.setId(parser.getText().trim());
						Log.w("测试：", parser.getText().trim());
					}
				} else if (parser.getName().equals("ItemImagestitle")) {
					eventType = parser.next();
//					itemImages.setTitle(parser.getText().trim());
					if (parser.getText() == null) {
						itemImages.setTitle("");
					} else {
						itemImages.setTitle(parser.getText().trim());
						Log.w("测试：", parser.getText().trim());
					}
				} else if (parser.getName().equals("ItemImagesimageurl")) {
					eventType = parser.next();
//					itemImages.setImageurl(parser.getText().trim());
					if (parser.getText() == null) {
						itemImages.setImageurl("");
					} else {
						itemImages.setImageurl(parser.getText().trim());
						Log.w("测试：", parser.getText().trim());
					}
				} else if (parser.getName().equals("ItemImagesdescr")) {
					eventType = parser.next();
//					itemImages.setDescr(parser.getText().trim());
					if (parser.getText() == null) {
						itemImages.setDescr("");
					} else {
						itemImages.setDescr(parser.getText().trim());
						Log.w("测试：", parser.getText().trim());
					}
				} else if (parser.getName().equals("ItemImagesbrowsetime")) {
					eventType = parser.next();
//					itemImages.setBrowsetime(Integer.parseInt(parser.getText().trim()));
					if (parser.getText() == null) {
						itemImages.setBrowsetime(0);
					} else {
						itemImages.setBrowsetime(Integer.parseInt(parser.getText().trim()));
						Log.w("测试：", parser.getText().trim());
					}
				} else if (parser.getName().equals("ItemImagescommenttime")) {
					eventType = parser.next();
//					itemImages.setCommenttime(Integer.parseInt(parser.getText().trim()));
					if (parser.getText() == null) {
						itemImages.setCommenttime(0);
					} else {
						itemImages.setCommenttime(Integer.parseInt(parser.getText().trim()));
						Log.w("测试：", parser.getText().trim());
					}
				} else if (parser.getName().equals("ImageNews")) {
					imageNews = new ImageNews();
				} else if (parser.getName().equals("imageNewsId")) {
					eventType = parser.next();
//					imageNews.setImageNewsId(parser.getText().trim());
					if (parser.getText() == null) {
						imageNews.setImageNewsId("");
					} else {
						imageNews.setImageNewsId(parser.getText().trim());
						Log.w("测试：", parser.getText().trim());
					}
				} else if (parser.getName().equals("imageNewsTitle")) {
					eventType = parser.next();
//					imageNews.setImageNewsTitle(parser.getText().trim());
					if (parser.getText() == null) {
						imageNews.setImageNewsTitle("");
					} else {
						imageNews.setImageNewsTitle(parser.getText().trim());
						Log.w("测试：", parser.getText().trim());
					}
				} else if (parser.getName().equals("imageNewsUrl")) {
					eventType = parser.next();
//					imageNews.setImageNewsUrl(parser.getText().trim());
					if (parser.getText() == null) {
						imageNews.setImageNewsUrl("");
					} else {
						imageNews.setImageNewsUrl(parser.getText().trim());
						Log.w("测试：", parser.getText().trim());
					}
				} else if (parser.getName().equals("imageNewsDescr")) {
					eventType = parser.next();
					if (parser.getText() == null) {
						imageNews.setImageNewsDescr("");
					} else {
						imageNews.setImageNewsDescr(parser.getText().trim());
						Log.w("测试：", parser.getText().trim());
					}
				} 
				
				break;

			// 判断当前事件是否为标签元素结束事件
			case XmlPullParser.END_TAG:
				if (parser.getName().equals("item")) { // 判断结束标签元素
					newList.add(descr);
					descr = null;
				} else if (parser.getName().equals("ItemImages")) { // 判断结束标签元素
					newList.add(itemImages);
					itemImages = null;
				} else if (parser.getName().equals("ImageNews")) { // 判断结束标签元素
					if (itemImages.getImages() == null) {
						itemImages.setImages(new ArrayList<ImageNews>());
					}
					itemImages.getImages().add(imageNews);
					imageNews = null;
				}
				break;
			}
			// 进入下一个元素并触发相应事件
			eventType = parser.next();
		}

		return newList;
	}
	
	/**
	 * 从xml文件中获取评论列表
	 * @param is	xml文件流
	 * @return		包含NewsDescr的List
	 * @throws Exception
	 */
	public static List<Follow> getCommentList(InputStream is)
			throws Exception {
		List<Follow> list = null;
		Follow follow = null;

		// 由android.util.Xml创建一个XmlPullParser实例
		XmlPullParser parser = Xml.newPullParser();
		// 设置输入流 并指明编码方式
		parser.setInput(is, "UTF-8");
		// 产生第一个事件
		int eventType = parser.getEventType();

		while (eventType != XmlPullParser.END_DOCUMENT) {

			switch (eventType) {
			// 判断当前事件是否为文档开始事件
			case XmlPullParser.START_DOCUMENT:
				list = new ArrayList<Follow>();
				break;

			// 判断当前事件是否为标签元素开始事件
			case XmlPullParser.START_TAG:
				if (parser.getName().equals("follow")) {
					follow = new Follow();
				} else if (parser.getName().equals("followid")) {
					eventType = parser.next();
//					follow.setFollowid(parser.getText().trim());
					if (parser.getText() == null) {
						follow.setFollowid("1");
					} else {
						follow.setFollowid(parser.getText().trim());
						Log.w("测试：", parser.getText().trim());
					}
				} else if (parser.getName().equals("followuser")) {
					eventType = parser.next();
//					follow.setFollowuser(parser.getText().trim());
					if (parser.getText() == null) {
						follow.setFollowuser("Android用户");
					} else {
						follow.setFollowuser(parser.getText().trim());
						Log.w("测试：", parser.getText().trim());
					}
				} else if (parser.getName().equals("followuserhead")) {
					eventType = parser.next();
//					follow.setFollowuserhead(parser.getText().trim());
					if (parser.getText() == null) {
						follow.setFollowuserhead("");
					} else {
						follow.setFollowuserhead(parser.getText().trim());
						Log.w("测试：", parser.getText().trim());
					}
				} else if (parser.getName().equals("followtime")) {
					eventType = parser.next();
//					follow.setFollowtime(parser.getText().trim());
					if (parser.getText() == null) {
						follow.setFollowtime("刚刚");
					} else {
						follow.setFollowtime(parser.getText().trim());
						Log.w("测试：", parser.getText().trim());
					}
				} else if (parser.getName().equals("followcontent")) {
					eventType = parser.next();
//					follow.setFollowcontent(parser.getText().trim());
					if (parser.getText() == null) {
						follow.setFollowcontent("好");
					} else {
						follow.setFollowcontent(parser.getText().trim());
						Log.w("测试：", parser.getText().trim());
					}
				} 
				break;

			// 判断当前事件是否为标签元素结束事件
			case XmlPullParser.END_TAG:
				if (parser.getName().equals("follow")) { // 判断结束标签元素
					list.add(follow);
					follow = null;
				}
				break;
			}
			// 进入下一个元素并触发相应事件
			eventType = parser.next();
		}

		return list;
	}
}
