package edu.jsu.newssysclient.util.net;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;

import edu.jsu.newssysclient.MyApplication;

/**
 * io流的操作
 * @author zuo
 *
 */
public class IOStream {

	/**
	 * 获取地址address的inputstream
	 * @param address
	 * @return
	 * @throws Exception
	 */
	public static InputStream getIOStreamFromUrl(String address) throws Exception{
		InputStream is = null;
		
		URL url = new URL(address); 
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();// 利用HttpURLConnection对象,我们可以从网络中获取网页数据.
		conn.setConnectTimeout(5 * 1000); // 单位是毫秒，设置超时时间为5秒
		conn.setRequestMethod("GET"); // HttpURLConnection是通过HTTP协议请求path路径的，所以需要设置请求方式,可以不设置，因为默认为GET
		if (conn.getResponseCode() == 200) {// 判断请求码是否是200码，否则失败
			is = conn.getInputStream(); // 获取输入流
			return is;
		}else {
			throw new Exception("链接服务器异常");
		}
		
	}
	
	public static InputStream getIOSByHttpClient(String address) throws Exception{
		HttpClient client = MyApplication.getHttpClient();
		PostMethod post = new PostMethod(address);
		client.executeMethod(post);
		return post.getResponseBodyAsStream();
	}
	
}
