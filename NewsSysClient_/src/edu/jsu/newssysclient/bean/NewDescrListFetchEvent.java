package edu.jsu.newssysclient.bean;

import java.io.Serializable;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 * 新闻简介集合，用户显示在新闻列表界面
 * @author zuo
 *
 */
public class NewDescrListFetchEvent implements Serializable{
	ItemList list;	// 列表集合

	public NewDescrListFetchEvent(ItemList list) {
		this.list = list;
	}

	public NewDescrListFetchEvent() {
	}

	public ItemList getList() {
		return list;
	}

	public void setList(ItemList list) {
		this.list = list;
	}
	
}
