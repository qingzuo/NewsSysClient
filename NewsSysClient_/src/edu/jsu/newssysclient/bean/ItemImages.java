package edu.jsu.newssysclient.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * 新闻列表项中每个新闻的描述
 * 
 * @author zuo
 * 
 */

@Root
public class ItemImages extends Item {
	@Element
	private ArrayList<ImageNews> images;

	public ItemImages() {
		super();
	}

	public ItemImages(ArrayList<ImageNews> images, String id, String title,
			String imageurl, String descr, int browsetime, int commenttime) {
		super(id, title, imageurl, descr, browsetime, commenttime);
		this.images = images;
	}

	public ArrayList<ImageNews> getImages() {
		return images;
	}

	public void setImages(ArrayList<ImageNews> images) {
		this.images = images;
	}
}
