package edu.jsu.newssysclient.bean;

import java.io.Serializable;

/**
 * ����ʱ����Դ
 * @author zuo
 *
 */
public class NewsTimeSource extends NewsText implements Serializable{

	public NewsTimeSource() {
		super("ʱ����Դ", 14, 0XFF888888, 5);
		// TODO Auto-generated constructor stub
	}
	
	public NewsTimeSource(String text) {
		super(text, 14, 0XFF888888, 5);
		// TODO Auto-generated constructor stub
	}
	
}
