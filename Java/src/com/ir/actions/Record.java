package com.ir.actions;
import java.io.Serializable;
import java.util.List;

public class Record implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private String title;
	private String link;
	private String pub_date;
	// private String dc_Source;
	// private String weblog_title;
	// private String weblog_Desc;
	// private String dc_lang;
	// private String weblog_tier;
	private List<String> category;
	private String description;
	

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getPub_date() {
		return pub_date;
	}

	public void setPub_date(String pub_date) {
		this.pub_date = pub_date;
	}

	public List<String> getCategory() {
		return category;
	}

	public void setCategory(List<String> category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


}
