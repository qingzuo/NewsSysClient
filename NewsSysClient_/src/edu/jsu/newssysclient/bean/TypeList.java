package edu.jsu.newssysclient.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 保存新闻类型列表
 * @author zuo
 *
 */
public class TypeList implements Serializable{

	private List<String> types;

	public TypeList() {
		super();
	}

	public TypeList(List<String> types) {
		super();
		this.types = types;
	}

	public List<String> getTypes() {
		return types;
	}

	public void setTypes(List<String> types) {
		this.types = types;
	}
}
