package edu.jsu.newssysclient.bean;

import java.io.Serializable;

/**
 * 新闻时间来源
 * @author zuo
 *
 */
public class NewsTimeSource extends NewsText implements Serializable{

	public NewsTimeSource() {
		super("时间来源", 14, 0XFF888888, 5);
		// TODO Auto-generated constructor stub
	}
	
	public NewsTimeSource(String text) {
		super(text, 14, 0XFF888888, 5);
		// TODO Auto-generated constructor stub
	}
	
}
