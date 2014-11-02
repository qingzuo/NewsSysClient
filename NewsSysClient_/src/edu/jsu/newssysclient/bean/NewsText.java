package edu.jsu.newssysclient.bean;

import java.io.Serializable;

/**
 * 文本类，是标题、时间、新闻内容的父类
 * @author zuo
 *
 */
public class NewsText implements Serializable {
	private String text;		// 文本内容
	private float size;			// 文本大小
	private int color;			// 文本颜色
	private int paddingtop;		// 与其他控件的上间距
	public NewsText() {
		super();
	}
	public NewsText(String text, float size, int color, int paddingtop) {
		super();
		this.text = text;
		this.size = size;
		this.color = color;
		this.paddingtop = paddingtop;
	}
	public int getPaddingtop() {
		return paddingtop;
	}
	public void setPaddingtop(int paddingtop) {
		this.paddingtop = paddingtop;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public float getSize() {
		return size;
	}
	public void setSize(float size) {
		this.size = size;
	}
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	
}
