package edu.jsu.newssysclient.bean;

import java.io.Serializable;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 * �����б��࣬���ڴ洢���ۼ���
 * @author zuo
 *
 */
@Root(name="list")
public class FollowList implements Serializable{
	
	@ElementList(inline=true)
	private List<Follow> follows;	

	public FollowList() {
		super();
	}

	public FollowList(List<Follow> follows) {
		super();
		this.follows = follows;
	}

	public List<Follow> getFollows() {
		return follows;
	}

	public void setFollows(List<Follow> follows) {
		this.follows = follows;
	}
	
}
