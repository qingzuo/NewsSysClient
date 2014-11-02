package edu.jsu.newssysclient.bean;

import java.io.Serializable;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 * 新闻集合类
 * @author zuo
 *
 */
@Root(name="all")
public class ItemList implements Serializable {

	@ElementList(inline=true)
	private List<Item> items;

	public ItemList() {
		super();
	}

	public ItemList(List<Item> items) {
		super();
		this.items = items;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}
	
}
