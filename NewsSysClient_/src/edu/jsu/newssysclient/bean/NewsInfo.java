package edu.jsu.newssysclient.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * �����࣬����һ������������Ϣ������id�����⣬���ݣ����ӡ�ͼƬ��ʱ����Դ����Ƶ����id�ⶼ˳���װ��List��
 * @author zuo
 *
 */
public class NewsInfo implements Serializable {
	private String id;	// ����id
	private List<Object> content = new ArrayList<Object>();	// ����˳�򱣴����ŵ���Ϣ 

	public NewsInfo() {
		super();
	} 

	public NewsInfo(List<Object> content) {
		super();
		this.content = content;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Object> getContent() {
		return content;
	}

	public void setContent(ArrayList<Object> content) {
		this.content = content;
	}
}
