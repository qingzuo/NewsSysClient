package edu.jsu.newssysclient.util.local;

import edu.jsu.newssysclient.MyApplication;

/**
 * 字符串工具
 * @author zuo
 *
 */
public class StringUtil {

	/**
	 * 判断是否为空
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (str == null || str.length() == 0 || "NULL".equals(str) || "null".equals(str)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 获取字符串
	 * @param id
	 * @return
	 */
	public static String getString(int id) {
		return MyApplication.getInstance().getString(id);
	}
	
	/**
	 * 判断是否为邮箱地址
	 * @param emailAdd
	 * @return
	 */
	public static boolean isEmailAddress(String emailAdd) {
		if (emailAdd.matches("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$")) {
			return true;
		}
		return false;
	}
}
