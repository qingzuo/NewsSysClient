package edu.jsu.newssysclient.bean;

import java.io.Serializable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * ����
 * @author zuo
 *
 */

@Root
public class Follow implements Serializable {

	@Element
	private String followid; 	// id
	@Element
	private String followuser;	// �û���
	@Element
	private String followuserhead;		// �û�ͷ��
	@Element
	private String followtime;		// ����ʱ��
	@Element
	private String followcontent;		// �������� 
	
	public Follow() {
		super();
	}

	public Follow(String followid, String followuser, String followuserhead,
			String followtime, String followcontent) {
		super();
		this.followid = followid;
		this.followuser = followuser;
		this.followuserhead = followuserhead;
		this.followtime = followtime;
		this.followcontent = followcontent;
	}

	public String getFollowid() {
		return followid;
	}

	public void setFollowid(String followid) {
		this.followid = followid;
	}

	public String getFollowuser() {
		return followuser;
	}

	public void setFollowuser(String followuser) {
		this.followuser = followuser;
	}

	public String getFollowuserhead() {
		return followuserhead;
	}

	public void setFollowuserhead(String followuserhead) {
		this.followuserhead = followuserhead;
	}

	public String getFollowtime() {
		return followtime;
	}

	public void setFollowtime(String followtime) {
		this.followtime = followtime;
	}

	public String getFollowcontent() {
		return followcontent;
	}

	public void setFollowcontent(String followcontent) {
		this.followcontent = followcontent;
	}
	
}
