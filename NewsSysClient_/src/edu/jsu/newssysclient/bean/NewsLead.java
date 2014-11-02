package edu.jsu.newssysclient.bean;

import java.io.Serializable;

/**
 * 新闻导语
 * @author zuo
 *
 */
public class NewsLead extends NewsText implements Serializable{

	public NewsLead() {
		super("新闻导语", 16, 0XFF282828, 40);
		// TODO Auto-generated constructor stub
	}

	public NewsLead(String text) {
		super(text, 16, 0XFF282828, 40);
		// TODO Auto-generated constructor stub
	}
	
}
