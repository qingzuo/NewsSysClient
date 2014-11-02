package edu.jsu.newssysclient;

/**
 * ��ַ������
 * @author zuo
 *
 */
public class URLHelper {
	public static String SERVERURL = "http://bike2.jsp.jspee.cn/news/";	// ��ί�����ip
//	public static String SERVERURL = "http://192.168.43.194:8080/news/";	// ������ip
//	public static String SERVERURL = "http://bikeshen.jsp.jspee.cn/news/"; // �����Ϸ�����ip
	
	// �������url
	public static String ANEWSURL = "news_androidSee.action?newsID=";
	public static String NEWSLIST = "news_androidList.action?";
	public static String NEWSCOMMENTS = "review_androidCommanList.action?page=1&newsID=";
	
	// �û����url
	public static String REGISTERURL = "user_androidAdd.action"; 
	public static String LOGINURL = "user_androidLogin.action";
	// �����û���Ϣurl
	public static String UPDATEPASSWORDURL = "user_androidChangePWD.action";
	public static String UPDATEEHEADURL = "user_androidChangeHead.action";
	public static String UPDATEUSERINFOURL = "user_androidEdit.action";
	// �ղ�url��
	public static String ADDCOLLECTURL = "col_androidAdd.action";
	public static String DELETECOLLECTURL = "col_androidDelete.action";
	public static String GETCOLLECTLISTURL = "col_andoridList.action";
	// ������ۣ�eg�� "http://localhost:8080/news/review_androidAdd.action?content=abc&newsID=2"
	public static String ADDCOMMENTURL = "review_androidAdd";
	public static String GETCOMMENTLISTURL = "review_androidMyList";
	
	public static void initAll(String serverip) {
		URLHelper.SERVERURL = serverip;
		ANEWSURL = URLHelper.SERVERURL + "news_androidSee.action?newsID=";
		NEWSLIST = URLHelper.SERVERURL + "news_androidList.action?";
		NEWSCOMMENTS = URLHelper.SERVERURL + "review_androidCommanList.action?page=1&newsID=";
		
		// �û����url
		REGISTERURL = URLHelper.SERVERURL + "user_androidAdd.action"; 
		LOGINURL = URLHelper.SERVERURL + "user_androidLogin.action";
		// �����û���Ϣurl
		UPDATEPASSWORDURL = URLHelper.SERVERURL + "user_androidChangePWD.action";
		UPDATEEHEADURL = URLHelper.SERVERURL + "user_androidChangeHead.action";
		UPDATEUSERINFOURL = URLHelper.SERVERURL + "user_androidEdit.action";
		// �ղ�url��
		ADDCOLLECTURL = URLHelper.SERVERURL + "col_androidAdd.action";
		DELETECOLLECTURL = URLHelper.SERVERURL + "col_androidDelete.action";
		GETCOLLECTLISTURL = URLHelper.SERVERURL + "col_andoridList.action";
		// ������ۣ�eg�� "http://localhost:8080/news/review_androidAdd.action?content=abc&newsID=2"
		ADDCOMMENTURL = URLHelper.SERVERURL + "review_androidAdd";
		GETCOMMENTLISTURL = URLHelper.SERVERURL + "review_androidMyList";
	}
	
	/*private static final String SERVERURL = "http://192.168.1.110:8080/news_b/";
	
	public static String ANEWSURL = "data/data_v3.xml";
	public static String NEWSLIST = "data/news_androidList_action.xml";
	public static String NEWSCOMMENTS = "data/commentlist.xml";*/
}
