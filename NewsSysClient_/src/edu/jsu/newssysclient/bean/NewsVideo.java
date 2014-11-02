package edu.jsu.newssysclient.bean;

import java.io.Serializable;

/**
 * ���ŵ���Ƶ��
 * @author zuo
 *
 */
public class NewsVideo implements Serializable {
	private String screenshot;		// ��ͼ��ַ
	private String source;			// ��Ƶ��ַ
	private String note;			// ע��
	public NewsVideo() {
		super();
	}
	public NewsVideo(String screenshot, String source, String note) {
		super();
		this.screenshot = screenshot;
		this.source = source;
		this.note = note;
	}
	public String getScreenshot() {
		return screenshot;
	}
	public void setScreenshot(String screenshot) {
		this.screenshot = screenshot;
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
