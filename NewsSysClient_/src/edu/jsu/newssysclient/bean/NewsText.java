package edu.jsu.newssysclient.bean;

import java.io.Serializable;

/**
 * �ı��࣬�Ǳ��⡢ʱ�䡢�������ݵĸ���
 * @author zuo
 *
 */
public class NewsText implements Serializable {
	private String text;		// �ı�����
	private float size;			// �ı���С
	private int color;			// �ı���ɫ
	private int paddingtop;		// �������ؼ����ϼ��
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
