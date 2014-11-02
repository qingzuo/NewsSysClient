package edu.jsu.newssysclient.bean;

import java.io.Serializable;

/**
 * 图片类，用来保存新闻中的图片
 * @author zuo
 *
 */
public class NewsImage implements Serializable {
	private String source;	// 图片地址
	private String note;	// 注释
	private int height;	// 宽
	private int width;	// 高
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
