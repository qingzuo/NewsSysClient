package edu.jsu.newssysclient.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 新闻类，保存一个新闻所有信息，包括id、标题，内容，链接、图片、时间来源、视频，除id外都顺序封装在List内
 * @author zuo
 *
 */
public class NewsInfo implements Serializable {
	private String id;	// 新闻id
	private List<Object> content = new ArrayList<Object>();	// 按照顺序保存新闻的信息 

	public NewsInfo() {
		super();
	} 

	public NewsInfo(List<Object> content) {
		super();
		this.content = content;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Object> getContent() {
		return content;
	}

	public void setContent(ArrayList<Object> content) {
		this.content = content;
	}
}
