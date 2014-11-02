package edu.jsu.newssysclient.bean;

import java.io.Serializable;

/**
 * ���ű���
 * @author zuo
 *
 */
public class NewsTitle extends NewsText implements Serializable{
	private String text;		// �ı�����
	
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
