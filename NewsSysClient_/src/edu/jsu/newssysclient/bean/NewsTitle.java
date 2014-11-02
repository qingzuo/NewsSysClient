package edu.jsu.newssysclient.bean;

import java.io.Serializable;

/**
 * 新闻标题
 * @author zuo
 *
 */
public class NewsTitle extends NewsText implements Serializable{
	private String text;		// 文本内容
	
	public NewsTitle() {
		super();
	}

	public NewsTitle(String text) {
		super();
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
}
