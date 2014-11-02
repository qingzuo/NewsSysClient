package edu.jsu.newssysclient.bean;

import java.io.Serializable;

/**
 * 新闻链接
 * @author zuo
 *
 */
public class NewsLink extends NewsText implements Serializable {

	public NewsLink() {
		super("新闻链接", 18, 0XFF0000FF, 40);
	}

	public NewsLink(String text) {
		super(text, 18, 0XFF0000FF, 40);
	}
	
}
