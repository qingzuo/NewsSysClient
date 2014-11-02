package edu.jsu.newssysclient.util.net;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.EBean;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.protocol.HTTP;

import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;

import android.net.Uri;
import edu.jsu.newssysclient.MyApplication;
import edu.jsu.newssysclient.URLHelper;
import edu.jsu.newssysclient.bean.Item;
import edu.jsu.newssysclient.debug.AppLogger;

/**
 * ���û���ص�����ͨѶ����½��ע�ᣬ�޸�
 * @author zuo
 *
 */
@EBean
public class UserHelper {
	
	/**
	 * Part[] parts = { new StringPart("userName", username.getText().toString()),
							new StringPart("userPwd", passwd.getText().toString()),
							new StringPart("email", email.getText().toString()),
							new FilePart("upload", new File(mCurrentPhotoPath)),
							new StringPart("signature", personNote.getText().toString()) };
	 * 
	 */
	
	/**
	 * ���һ������
	 * @param content
	 * @param newsId
	 * @return
	 */
	public static String addComment(String content, String newsId) {
		// ����ת��
		Part[] parts = { new StringPart("newsID", newsId, "UTF-8"),
				new StringPart("content", content, "UTF-8")};
		return postForm(URLHelper.ADDCOMMENTURL, parts);
	}
	
	/**
	 * ��ȡ��ǰ�û����е��ղ��б�
	 * @return
	 */
	public static List<Item> getCollectList() {
		try {
			String url = URLHelper.GETCOLLECTLISTURL;
			return XmlHelper.getNewsDescrList(IOStream.getIOSByHttpClient(url));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * ��ȡ��ǰ�û����е������б�
	 * @return
	 */
	public static List<Item> getCommentist() {
		try {
			String url = URLHelper.GETCOMMENTLISTURL;
			return XmlHelper.getNewsDescrList(IOStream.getIOSByHttpClient(url));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * �����û���Ϣ
	 * @return
	 */
	public static String changUserInfo(Part[] parts) {
		// ����ת��
		return postForm(URLHelper.UPDATEUSERINFOURL, parts);
	}
	
	/**
	 * �����û�ͷ��
	 * @param avatarPath
	 * @return
	 */
	public static String changeAvatar(String avatarPath) {
		try {
			Part[] parts = { new FilePart("upload", new File(avatarPath), null, "UTF-8")};
			return postForm(URLHelper.UPDATEEHEADURL, parts);
		} catch (FileNotFoundException e) {
			AppLogger.i("�����û�ͷ���ļ�������");
		}
		return null;
	}
	
	/**
	 * ɾ���ղ�
	 * @param newsId
	 * @return
	 */
	public static String delCollect(String newsId) {
		Part[] parts = { new StringPart("NID", newsId, "UTF-8")};
		return postForm(URLHelper.DELETECOLLECTURL, parts);
	}
	
	/**
	 * ����ղ�
	 * @param newsId
	 * @return
	 */
	public static String addCollect(String newsId) {
		Part[] parts = { new StringPart("NID", newsId, "UTF-8")};
		return postForm(URLHelper.ADDCOLLECTURL, parts);
	}
	
	/**
	 * ��¼����
	 * @param name
	 * @param passwd
	 * @return	���������ص��ַ���
	 */
	public static String login(String name, String passwd) {
		Part[] parts = { new StringPart("userName", name, "UTF-8"),
				new StringPart("userPwd", passwd, "UTF-8")};
		return postForm(URLHelper.LOGINURL, parts);
	}
	
	/**
	 * ��������
	 * @param passwd
	 * @return
	 */
	public static String changePassword(String passwd) {
		Part[] parts = { new StringPart("userPwd", passwd, "UTF-8")};
		return postForm(URLHelper.UPDATEPASSWORDURL, parts);
	}
	
	/**
	 * ע���û�
	 * @param url	��������ַ
	 * @param parts	����
	 * @return	�����������ַ��������
	 */
	public static String postForm(String url, Part[] parts) {
		PostMethod post = new PostMethod(url);
		post.getParams().setContentCharset("UTF-8");
		if (parts != null) post.setRequestEntity(new MultipartRequestEntity(parts, post.getParams()));
		
		HttpClient client = MyApplication.getHttpClient();
		
		try {
			client.executeMethod(post);
			int status = post.getStatusLine().getStatusCode();
			if (status == 200) {
//				return new String(post.getResponseBodyAsString().getBytes("ISO-8859-1"), "UTF-8");
				return post.getResponseBodyAsString();
			}
		} catch (IOException e) {
			AppLogger.i("UploadForm�ϴ�����IOException");
		} finally {
//			post.abort();
		}
		return null;
	}
}
