package edu.jsu.newssysclient.util.local;

import org.json.JSONException;
import org.json.JSONObject;

import edu.jsu.newssysclient.bean.UserInfo;

/**
 * Json�����࣬���ڽ���json����
 * @author zuo
 *
 */
public class JsonUtil {

	/**
	 * ��ȡJson�ַ����ڽ��
	 * @param jsonString
	 * @return
	 */
	public static String getJsonResult(String jsonString) {
		try {
			JSONObject jo = new JSONObject(jsonString);
			return jo.getString("result");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "false";
	}
	
	/**
	 * ����json�ַ�������û���Ϣ
	 * @param jsonString
	 * @return
	 */
	public static UserInfo getJsonUserInfo(String jsonString) {
		try {
			JSONObject userJo = new JSONObject(jsonString).getJSONObject("user");
			UserInfo userInfo = new UserInfo();
			
			userInfo.setUserID(userJo.getLong("userID"));
			userInfo.setEmail(userJo.getString("email"));
			userInfo.setPtoShowPath(userJo.getString("ptoShowPath"));
			userInfo.setSignature(userJo.getString("signature"));
			userInfo.setUserName(userJo.getString("userName"));
			userInfo.setUserPwd(userJo.getString("userPwd"));
			
			return userInfo;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
