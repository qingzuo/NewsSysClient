package edu.jsu.newssysclient.bean;

import java.io.Serializable;

/**
 * ���ŵ���
 * @author zuo
 *
 */
public class NewsLead extends NewsText implements Serializable{

	public NewsLead() {
		super("���ŵ���", 16, 0XFF282828, 40);
		// TODO Auto-generated constructor stub
	}

	public NewsLead(String text) {
		super(text, 16, 0XFF282828, 40);
		// TODO Auto-generated constructor stub
	}
	
}
