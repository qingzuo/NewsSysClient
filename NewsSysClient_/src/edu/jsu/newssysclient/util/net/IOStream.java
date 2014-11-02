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
 * io���Ĳ���
 * @author zuo
 *
 */
public class IOStream {

	/**
	 * ��ȡ��ַaddress��inputstream
	 * @param address
	 * @return
	 * @throws Exception
	 */
	public static InputStream getIOStreamFromUrl(String address) throws Exception{
		InputStream is = null;
		
		URL url = new URL(address); 
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();// ����HttpURLConnection����,���ǿ��Դ������л�ȡ��ҳ����.
		conn.setConnectTimeout(5 * 1000); // ��λ�Ǻ��룬���ó�ʱʱ��Ϊ5��
		conn.setRequestMethod("GET"); // HttpURLConnection��ͨ��HTTPЭ������path·���ģ�������Ҫ��������ʽ,���Բ����ã���ΪĬ��ΪGET
		if (conn.getResponseCode() == 200) {// �ж��������Ƿ���200�룬����ʧ��
			is = conn.getInputStream(); // ��ȡ������
			return is;
		}else {
			throw new Exception("���ӷ������쳣");
		}
		
	}
	
	public static InputStream getIOSByHttpClient(String address) throws Exception{
		HttpClient client = MyApplication.getHttpClient();
		PostMethod post = new PostMethod(address);
		client.executeMethod(post);
		return post.getResponseBodyAsStream();
	}
	
}
