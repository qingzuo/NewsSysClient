package edu.jsu.newssysclient.bean;

import java.io.Serializable;

/**
 * ��������
 * @author zuo
 *
 */
public class NewsLink extends NewsText implements Serializable {

	public NewsLink() {
		super("��������", 18, 0XFF0000FF, 40);
	}

	public NewsLink(String text) {
		super(text, 18, 0XFF0000FF, 40);
	}
	
}
