package edu.jsu.newssysclient.bean;

import java.io.Serializable;

/**
 * �û���Ϣ��
 * @author zuo
 *
 */
public class UserInfo implements Serializable{
	private long userID;
	private String userName;	// �û���
	private String userPwd;		// �û�����
	private String email;		// ����
	private String ptoShowPath;	// ͷ���ַ
	private String signature;	// ǩ��
	
	public UserInfo() {
		super();
	}
	
	public UserInfo(long userID, String userName, String userPwd, String email,
			String ptoShowPath, String signature) {
		super();
		this.userID = userID;
		this.userName = userName;
		this.userPwd = userPwd;
		this.email = email;
		this.ptoShowPath = ptoShowPath;
		this.signature = signature;
	}
	
	public long getUserID() {
		return userID;
	}
	public void setUserID(long userID) {
		this.userID = userID;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPwd() {
		return userPwd;
	}
	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPtoShowPath() {
		return ptoShowPath;
	}
	public void setPtoShowPath(String ptoShowPath) {
		this.ptoShowPath = ptoShowPath;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	

}
