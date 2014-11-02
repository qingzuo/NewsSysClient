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
 * ��xml�ļ��õ���������
 * 
 * @author zuo
 * 
 */
public class XmlHelper {

	/**
	 * �������ż���
	 * 
	 * @param is
	 *            ���ӷ�������InputStream
	 * @return �����������ŵ�List����
	 * @throws Exception
	 */
	public static List<NewsInfo> getNewsInfos(InputStream is) throws Exception {
		List<NewsInfo> newsInfos = null; // �������е�����
		NewsInfo news = null; // �����࣬�������漸����
		NewsTitle title = null; // ����
		NewsTimeSource timeSource = null; // ʱ����Դ
		NewsLink link = null; // ����
		NewsLead lead = null; // ����
		NewsImage image = null; // ͼƬ
		NewsVideo video = null; // ��Ƶ
		NewsContent content = null; // ��������
		Follow follow = null; // ���Ÿ���

		// ��android.util.Xml����һ��XmlPullParserʵ��
		XmlPullParser parser = Xml.newPullParser();
		// ���������� ��ָ�����뷽ʽ
		parser.setInput(is, "UTF-8");
		// ������һ���¼�
		int eventType = parser.getEventType();

		while (eventType != XmlPullParser.END_DOCUMENT) {

			switch (eventType) {
			// �жϵ�ǰ�¼��Ƿ�Ϊ�ĵ���ʼ�¼�
			case XmlPullParser.START_DOCUMENT:
				newsInfos = new ArrayList<NewsInfo>(); // ��ʼ������
				break;

			// �жϵ�ǰ�¼��Ƿ�Ϊ��ǩԪ�ؿ�ʼ�¼�
			case XmlPullParser.START_TAG:
				if (parser.getName().equals("news")) {
					news = new NewsInfo(); // ��ʼ��һ������
				} else if (parser.getName().equals("id")) {
					eventType = parser.next();
//					news.setId(parser.getText().trim());
					if (parser.getText() == null) {
						news.setId("1");
					} else {
						news.setId(parser.getText().trim());
						Log.w("���ԣ�", parser.getText().trim());
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
						Log.w("���ԣ�", parser.getText().trim());
					}
				} else if (parser.getName().equals("timesource")) {
					timeSource = new NewsTimeSource();
				} else if (parser.getName().equals("timesourcecontent")) {
					eventType = parser.next();
//					timeSource.setText(parser.getText().trim());
					if (parser.getText() == null) {
						timeSource.setText("�ո�   ��Դ��������");
					} else {
						timeSource.setText(parser.getText().trim());
						Log.w("���ԣ�", parser.getText().trim());
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
						Log.w("���ԣ�", parser.getText().trim());
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
						Log.w("���ԣ�", parser.getText().trim());
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
						Log.w("���ԣ�", parser.getText().trim());
					}
//					image.setSource("http://pic.baike.soso.com/p/20111220/20111220184359-500271905.jpg");
				} else if (parser.getName().equals("imagenote")) {
					eventType = parser.next();
//					image.setNote(parser.getText().trim());
					if (parser.getText() == null) {
						image.setNote("");
					} else {
						image.setNote(parser.getText().trim());
						Log.w("���ԣ�", parser.getText().trim());
					}
//					image.setNote("NULL");
				}else if (parser.getName().equals("imageheight")) {
					eventType = parser.next();
//					image.setHeight(Integer.parseInt(parser.getText().trim()));
					if (parser.getText() == null) {
						image.setHeight(150);
					} else {
						image.setHeight(Integer.parseInt(parser.getText().trim()));
						Log.w("���ԣ�", parser.getText().trim());
					}
				}else if (parser.getName().equals("imagewidth")) {
					eventType = parser.next();
//					image.setWidth(Integer.parseInt(parser.getText().trim()));
					if (parser.getText() == null) {
						image.setWidth(250);
					} else {
						image.setWidth(Integer.parseInt(parser.getText().trim()));
						Log.w("���ԣ�", parser.getText().trim());
					}
				} else if (parser.getName().equals("video")) {
					video = new NewsVideo();
				} else if (parser.getName().equals("source")) {
					eventType = parser.next();
//					video.setSource(parser.getText().trim());
					if (parser.getText() == null) {
						video.setSource("������");
					} else {
						video.setSource(parser.getText().trim());
						Log.w("���ԣ�", parser.getText().trim());
					}
				} else if (parser.getName().equals("screenshot")) {
					eventType = parser.next();
//					video.setScreenshot(parser.getText().trim());
					if (parser.getText() == null) {
						video.setScreenshot("");
					} else {
						video.setScreenshot(parser.getText().trim());
						Log.w("���ԣ�", parser.getText().trim());
					}
				} else if (parser.getName().equals("videonote")) {
					eventType = parser.next();
//					video.setNote(parser.getText().trim());
					if (parser.getText() == null) {
						video.setNote("");
					} else {
						video.setNote(parser.getText().trim());
						Log.w("���ԣ�", parser.getText().trim());
					}
				} else if (parser.getName().equals("newstext")) {
					content = new NewsContent();
				} else if (parser.getName().equals("newscontent")) {
					eventType = parser.next();
//					content.setText(parser.getText().trim());
					if (parser.getText() == null) {
						content.setText("��");
					} else {
						content.setText(parser.getText().trim());
						Log.w("���ԣ�", parser.getText().trim());
					}
				} else if (parser.getName().equals("follow")) {
					follow = new Follow();
				} else if (parser.getName().equals("followuser")) {
					eventType = parser.next();
//					follow.setFollowuser(parser.getText().trim());
					if (parser.getText() == null) {
						follow.setFollowuser("Android�û�");
					} else {
						follow.setFollowuser(parser.getText().trim());
						Log.w("���ԣ�", parser.getText().trim());
					}
				} else if (parser.getName().equals("followtime")) {
					eventType = parser.next();
//					follow.setFollowtime(parser.getText().trim());
					if (parser.getText() == null) {
						follow.setFollowtime("�ո�");
					} else {
						follow.setFollowtime(parser.getText().trim());
						Log.w("���ԣ�", parser.getText().trim());
					}
				} else if (parser.getName().equals("followcontent")) {
					eventType = parser.next();
//					follow.setFollowcontent(parser.getText().trim());
					if (parser.getText() == null) {
						follow.setFollowcontent("");
					} else {
						follow.setFollowcontent(parser.getText().trim());
						Log.w("���ԣ�", parser.getText().trim());
					}
				}
				break;

			// �жϵ�ǰ�¼��Ƿ�Ϊ��ǩԪ�ؽ����¼�
			case XmlPullParser.END_TAG:
				if (parser.getName().equals("news")) { // �жϽ�����ǩԪ���Ƿ���news
					newsInfos.add(news);
					news = null;
				} else if (parser.getName().equals("title")) { // �жϽ�����ǩԪ���Ƿ���title
					news.getContent().add(title);
					title = null;
				} else if (parser.getName().equals("timesource")) { // �жϽ�����ǩԪ���Ƿ���title
					news.getContent().add(timeSource);
					timeSource = null;
				} else if (parser.getName().equals("link")) { // �жϽ�����ǩԪ���Ƿ���title
					news.getContent().add(link);
					link = null;
				} else if (parser.getName().equals("lead")) { // �жϽ�����ǩԪ���Ƿ���title
					news.getContent().add(lead);
					lead = null;
				} else if (parser.getName().equals("image")) { // �жϽ�����ǩԪ���Ƿ���title
					news.getContent().add(image);
					image = null;
				} else if (parser.getName().equals("video")) { // �жϽ�����ǩԪ���Ƿ���title
					news.getContent().add(video);
					video = null;
				} else if (parser.getName().equals("newstext")) { // �жϽ�����ǩԪ���Ƿ���title
					news.getContent().add(content);
					content = null;
				} else if (parser.getName().equals("follow")) { // �жϽ�����ǩԪ���Ƿ���title
					news.getContent().add(follow);
					follow = null;
				}
				break;
			}
			// ������һ��Ԫ�ز�������Ӧ�¼�
			eventType = parser.next();
		}

		return newsInfos;
	}
	
	/**
	 * ��xml�ļ��л�ȡ�����б�
	 * @param is	xml�ļ���
	 * @return		����NewsDescr��List
	 * @throws Exception
	 */
	public static List<Item> getNewsDescrList(InputStream is)
			throws Exception {
		List<Item> newList = null;
		Item descr = null;
		ItemImages itemImages = null;
		ImageNews imageNews = null;

		// ��android.util.Xml����һ��XmlPullParserʵ��
		XmlPullParser parser = Xml.newPullParser();
		// ���������� ��ָ�����뷽ʽ
		parser.setInput(is, "UTF-8");
		// ������һ���¼�
		int eventType = parser.getEventType();

		while (eventType != XmlPullParser.END_DOCUMENT) {

			switch (eventType) {
			// �жϵ�ǰ�¼��Ƿ�Ϊ�ĵ���ʼ�¼�
			case XmlPullParser.START_DOCUMENT:
				newList = new ArrayList<Item>();
				break;

			// �жϵ�ǰ�¼��Ƿ�Ϊ��ǩԪ�ؿ�ʼ�¼�
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
						Log.w("���ԣ�", parser.getText().trim());
					}
				} else if (parser.getName().equals("title")) {
					eventType = parser.next();
					if (parser != null && parser.getText() != null && descr != null) {
						descr.setTitle(parser.getText().trim());
						Log.w("���ԣ�", parser.getText().trim());
					} else {
						descr.setTitle("�ޱ���");
					}
				} else if (parser.getName().equals("imageurl")) {
					eventType = parser.next();
					descr.setImageurl(parser.getText().trim());
					if (parser.getText() == null) {
						descr.setImageurl("");
					} else {
						descr.setImageurl(parser.getText().trim());
						Log.w("���ԣ�", parser.getText().trim());
					}
				} else if (parser.getName().equals("descr")) {
					eventType = parser.next();
//					descr.setDescr(parser.getText().trim());
					if (parser.getText() == null) {
						descr.setDescr("");
					} else {
						descr.setDescr(parser.getText().trim());
						Log.w("���ԣ�", parser.getText().trim());
					}
				} else if (parser.getName().equals("browsetime")) {
					eventType = parser.next();
//					descr.setBrowsetime(Integer.parseInt(parser.getText().trim()));
					if (parser.getText() == null) {
						descr.setBrowsetime(0);
					} else {
						descr.setBrowsetime(Integer.parseInt(parser.getText().trim()));
						Log.w("���ԣ�", parser.getText().trim());
					}
				} else if (parser.getName().equals("commenttime")) {
					eventType = parser.next();
//					descr.setCommenttime(Integer.parseInt(parser.getText().trim()));
					if (parser.getText() == null) {
						descr.setCommenttime(0);
					} else {
						descr.setCommenttime(Integer.parseInt(parser.getText().trim()));
						Log.w("���ԣ�", parser.getText().trim());
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
						Log.w("���ԣ�", parser.getText().trim());
					}
				} else if (parser.getName().equals("ItemImagestitle")) {
					eventType = parser.next();
//					itemImages.setTitle(parser.getText().trim());
					if (parser.getText() == null) {
						itemImages.setTitle("");
					} else {
						itemImages.setTitle(parser.getText().trim());
						Log.w("���ԣ�", parser.getText().trim());
					}
				} else if (parser.getName().equals("ItemImagesimageurl")) {
					eventType = parser.next();
//					itemImages.setImageurl(parser.getText().trim());
					if (parser.getText() == null) {
						itemImages.setImageurl("");
					} else {
						itemImages.setImageurl(parser.getText().trim());
						Log.w("���ԣ�", parser.getText().trim());
					}
				} else if (parser.getName().equals("ItemImagesdescr")) {
					eventType = parser.next();
//					itemImages.setDescr(parser.getText().trim());
					if (parser.getText() == null) {
						itemImages.setDescr("");
					} else {
						itemImages.setDescr(parser.getText().trim());
						Log.w("���ԣ�", parser.getText().trim());
					}
				} else if (parser.getName().equals("ItemImagesbrowsetime")) {
					eventType = parser.next();
//					itemImages.setBrowsetime(Integer.parseInt(parser.getText().trim()));
					if (parser.getText() == null) {
						itemImages.setBrowsetime(0);
					} else {
						itemImages.setBrowsetime(Integer.parseInt(parser.getText().trim()));
						Log.w("���ԣ�", parser.getText().trim());
					}
				} else if (parser.getName().equals("ItemImagescommenttime")) {
					eventType = parser.next();
//					itemImages.setCommenttime(Integer.parseInt(parser.getText().trim()));
					if (parser.getText() == null) {
						itemImages.setCommenttime(0);
					} else {
						itemImages.setCommenttime(Integer.parseInt(parser.getText().trim()));
						Log.w("���ԣ�", parser.getText().trim());
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
						Log.w("���ԣ�", parser.getText().trim());
					}
				} else if (parser.getName().equals("imageNewsTitle")) {
					eventType = parser.next();
//					imageNews.setImageNewsTitle(parser.getText().trim());
					if (parser.getText() == null) {
						imageNews.setImageNewsTitle("");
					} else {
						imageNews.setImageNewsTitle(parser.getText().trim());
						Log.w("���ԣ�", parser.getText().trim());
					}
				} else if (parser.getName().equals("imageNewsUrl")) {
					eventType = parser.next();
//					imageNews.setImageNewsUrl(parser.getText().trim());
					if (parser.getText() == null) {
						imageNews.setImageNewsUrl("");
					} else {
						imageNews.setImageNewsUrl(parser.getText().trim());
						Log.w("���ԣ�", parser.getText().trim());
					}
				} else if (parser.getName().equals("imageNewsDescr")) {
					eventType = parser.next();
					if (parser.getText() == null) {
						imageNews.setImageNewsDescr("");
					} else {
						imageNews.setImageNewsDescr(parser.getText().trim());
						Log.w("���ԣ�", parser.getText().trim());
					}
				} 
				
				break;

			// �жϵ�ǰ�¼��Ƿ�Ϊ��ǩԪ�ؽ����¼�
			case XmlPullParser.END_TAG:
				if (parser.getName().equals("item")) { // �жϽ�����ǩԪ��
					newList.add(descr);
					descr = null;
				} else if (parser.getName().equals("ItemImages")) { // �жϽ�����ǩԪ��
					newList.add(itemImages);
					itemImages = null;
				} else if (parser.getName().equals("ImageNews")) { // �жϽ�����ǩԪ��
					if (itemImages.getImages() == null) {
						itemImages.setImages(new ArrayList<ImageNews>());
					}
					itemImages.getImages().add(imageNews);
					imageNews = null;
				}
				break;
			}
			// ������һ��Ԫ�ز�������Ӧ�¼�
			eventType = parser.next();
		}

		return newList;
	}
	
	/**
	 * ��xml�ļ��л�ȡ�����б�
	 * @param is	xml�ļ���
	 * @return		����NewsDescr��List
	 * @throws Exception
	 */
	public static List<Follow> getCommentList(InputStream is)
			throws Exception {
		List<Follow> list = null;
		Follow follow = null;

		// ��android.util.Xml����һ��XmlPullParserʵ��
		XmlPullParser parser = Xml.newPullParser();
		// ���������� ��ָ�����뷽ʽ
		parser.setInput(is, "UTF-8");
		// ������һ���¼�
		int eventType = parser.getEventType();

		while (eventType != XmlPullParser.END_DOCUMENT) {

			switch (eventType) {
			// �жϵ�ǰ�¼��Ƿ�Ϊ�ĵ���ʼ�¼�
			case XmlPullParser.START_DOCUMENT:
				list = new ArrayList<Follow>();
				break;

			// �жϵ�ǰ�¼��Ƿ�Ϊ��ǩԪ�ؿ�ʼ�¼�
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
						Log.w("���ԣ�", parser.getText().trim());
					}
				} else if (parser.getName().equals("followuser")) {
					eventType = parser.next();
//					follow.setFollowuser(parser.getText().trim());
					if (parser.getText() == null) {
						follow.setFollowuser("Android�û�");
					} else {
						follow.setFollowuser(parser.getText().trim());
						Log.w("���ԣ�", parser.getText().trim());
					}
				} else if (parser.getName().equals("followuserhead")) {
					eventType = parser.next();
//					follow.setFollowuserhead(parser.getText().trim());
					if (parser.getText() == null) {
						follow.setFollowuserhead("");
					} else {
						follow.setFollowuserhead(parser.getText().trim());
						Log.w("���ԣ�", parser.getText().trim());
					}
				} else if (parser.getName().equals("followtime")) {
					eventType = parser.next();
//					follow.setFollowtime(parser.getText().trim());
					if (parser.getText() == null) {
						follow.setFollowtime("�ո�");
					} else {
						follow.setFollowtime(parser.getText().trim());
						Log.w("���ԣ�", parser.getText().trim());
					}
				} else if (parser.getName().equals("followcontent")) {
					eventType = parser.next();
//					follow.setFollowcontent(parser.getText().trim());
					if (parser.getText() == null) {
						follow.setFollowcontent("��");
					} else {
						follow.setFollowcontent(parser.getText().trim());
						Log.w("���ԣ�", parser.getText().trim());
					}
				} 
				break;

			// �жϵ�ǰ�¼��Ƿ�Ϊ��ǩԪ�ؽ����¼�
			case XmlPullParser.END_TAG:
				if (parser.getName().equals("follow")) { // �жϽ�����ǩԪ��
					list.add(follow);
					follow = null;
				}
				break;
			}
			// ������һ��Ԫ�ز�������Ӧ�¼�
			eventType = parser.next();
		}

		return list;
	}
}
