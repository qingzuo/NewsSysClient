package edu.jsu.newssysclient.bean;

import java.io.Serializable;

/**
 * ��������
 * @author zuo
 *
 */
public class NewsContent extends NewsText implements Serializable{

	public NewsContent() {
		super("��������", 18, 0XFF585858, 40);
		// TODO Auto-generated constructor stub
	}

	public NewsContent(String text) {
		super(text, 18, 0XFF585858, 40);
		// TODO Auto-generated constructor stub
	}

}
