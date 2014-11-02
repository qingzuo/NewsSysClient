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
 * 跟用户相关的网络通讯，登陆，注册，修改
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
	 * 添加一条评论
	 * @param content
	 * @param newsId
	 * @return
	 */
	public static String addComment(String content, String newsId) {
		// 编码转换
		Part[] parts = { new StringPart("newsID", newsId, "UTF-8"),
				new StringPart("content", content, "UTF-8")};
		return postForm(URLHelper.ADDCOMMENTURL, parts);
	}
	
	/**
	 * 获取当前用户所有的收藏列表
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
	 * 获取当前用户所有的评论列表
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
	 * 更改用户信息
	 * @return
	 */
	public static String changUserInfo(Part[] parts) {
		// 编码转换
		return postForm(URLHelper.UPDATEUSERINFOURL, parts);
	}
	
	/**
	 * 更改用户头像
	 * @param avatarPath
	 * @return
	 */
	public static String changeAvatar(String avatarPath) {
		try {
			Part[] parts = { new FilePart("upload", new File(avatarPath), null, "UTF-8")};
			return postForm(URLHelper.UPDATEEHEADURL, parts);
		} catch (FileNotFoundException e) {
			AppLogger.i("更改用户头像，文件不存在");
		}
		return null;
	}
	
	/**
	 * 删除收藏
	 * @param newsId
	 * @return
	 */
	public static String delCollect(String newsId) {
		Part[] parts = { new StringPart("NID", newsId, "UTF-8")};
		return postForm(URLHelper.DELETECOLLECTURL, parts);
	}
	
	/**
	 * 添加收藏
	 * @param newsId
	 * @return
	 */
	public static String addCollect(String newsId) {
		Part[] parts = { new StringPart("NID", newsId, "UTF-8")};
		return postForm(URLHelper.ADDCOLLECTURL, parts);
	}
	
	/**
	 * 登录方法
	 * @param name
	 * @param passwd
	 * @return	服务器返回的字符串
	 */
	public static String login(String name, String passwd) {
		Part[] parts = { new StringPart("userName", name, "UTF-8"),
				new StringPart("userPwd", passwd, "UTF-8")};
		return postForm(URLHelper.LOGINURL, parts);
	}
	
	/**
	 * 更改密码
	 * @param passwd
	 * @return
	 */
	public static String changePassword(String passwd) {
		Part[] parts = { new StringPart("userPwd", passwd, "UTF-8")};
		return postForm(URLHelper.UPDATEPASSWORDURL, parts);
	}
	
	/**
	 * 注册用户
	 * @param url	服务器地址
	 * @param parts	参数
	 * @return	服务器返回字符串结果；
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
			AppLogger.i("UploadForm上传数据IOException");
		} finally {
//			post.abort();
		}
		return null;
	}
}
