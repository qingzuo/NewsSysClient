package edu.jsu.newssysclient.bean;

import java.io.Serializable;

/**
 * ͼƬ�࣬�������������е�ͼƬ
 * @author zuo
 *
 */
public class NewsImage implements Serializable {
	private String source;	// ͼƬ��ַ
	private String note;	// ע��
	private int height;	// ��
	private int width;	// ��
	public NewsImage() {
		super();
	}

	public NewsImage(String source, String note, int height, int width) {
		super();
		this.source = source;
		this.note = note;
		this.height = height;
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
}
