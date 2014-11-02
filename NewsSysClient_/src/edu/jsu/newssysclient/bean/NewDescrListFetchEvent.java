package edu.jsu.newssysclient.bean;

import java.io.Serializable;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 * ���ż�鼯�ϣ��û���ʾ�������б����
 * @author zuo
 *
 */
public class NewDescrListFetchEvent implements Serializable{
	ItemList list;	// �б���

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
