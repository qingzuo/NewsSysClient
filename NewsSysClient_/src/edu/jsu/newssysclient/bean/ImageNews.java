package edu.jsu.newssysclient.bean;

import java.io.Serializable;

/**
 * Õº∆¨–¬Œ≈
 * @author zuo
 *
 */
public class ImageNews implements Serializable{
	private String imageNewsId;	// 
	private String imageNewsTitle;
	private String imageNewsUrl;
	private String imageNewsDescr;		// Õº∆¨√Ë ˆ
	
	public ImageNews() {
		super();
	}
	
	public ImageNews(String imageNewsId, String imageNewsTitle,
			String imageNewsUrl, String imageNewsDescr) {
		super();
		this.imageNewsId = imageNewsId;
		this.imageNewsTitle = imageNewsTitle;
		this.imageNewsUrl = imageNewsUrl;
		this.imageNewsDescr = imageNewsDescr;
	}

	public String getImageNewsId() {
		return imageNewsId;
	}

	public void setImageNewsId(String imageNewsId) {
		this.imageNewsId = imageNewsId;
	}

	public String getImageNewsTitle() {
		return imageNewsTitle;
	}

	public void setImageNewsTitle(String imageNewsTitle) {
		this.imageNewsTitle = imageNewsTitle;
	}

	public String getImageNewsUrl() {
		return imageNewsUrl;
	}

	public void setImageNewsUrl(String imageNewsUrl) {
		this.imageNewsUrl = imageNewsUrl;
	}

	public String getImageNewsDescr() {
		return imageNewsDescr;
	}

	public void setImageNewsDescr(String imageNewsDescr) {
		this.imageNewsDescr = imageNewsDescr;
	}
	
	
}
