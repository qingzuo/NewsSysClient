package edu.jsu.newssysclient.bean;

import java.io.Serializable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * �����б�����ÿ�����ŵ�����
 * @author zuo
 *
 */

@Root
public class Item implements Serializable{
	@Element
	private String id;
	@Element
	private String title;
	@Element
	private String imageurl;
	@Element
	private String descr;		// ��������
	@Element
	private int browsetime;		// �������
	@Element
	private int commenttime;	// ���۴���

	public Item() {
		super();
	}
	
	public Item(String id, String title, String imageurl, String descr,
			int browsetime, int commenttime) {
		super();
		this.id = id;
		this.title = title;
		this.imageurl = imageurl;
		this.descr = descr;
		this.browsetime = browsetime;
		this.commenttime = commenttime;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImageurl() {
		return imageurl;
	}
	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	public int getBrowsetime() {
		return browsetime;
	}
	public void setBrowsetime(int browsetime) {
		this.browsetime = browsetime;
	}
	public int getCommenttime() {
		return commenttime;
	}
	public void setCommenttime(int commenttime) {
		this.commenttime = commenttime;
	}
	
}
