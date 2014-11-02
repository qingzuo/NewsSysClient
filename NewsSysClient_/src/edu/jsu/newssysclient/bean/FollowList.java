package edu.jsu.newssysclient.bean;

import java.io.Serializable;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 * 评论列表类，用于存储评论集合
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
