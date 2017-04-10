package com.models;

import java.io.Serializable;


public class News implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6761346513269116224L;
	
	
	int created_at;
	String news_content;
	String news_title;
	String news_url;
	String photo_url;
	int news_id;
	int updated_at;
	int is_deleted;

	public void setNews_content(String news_content) {
		this.news_content = news_content;
	}
	
	public String getNews_content() {
		return news_content;
	}
	
	public void setNews_title(String news_title) {
		this.news_title = news_title;
	}
	
	public String getNews_title() {
		return news_title;
	}
	
	public void setNews_url(String news_url) {
		this.news_url = news_url;
	}
	
	public String getNews_url() {
		return news_url;
	}
	
	public void setPhoto_url(String photo_url) {
		this.photo_url = photo_url;
	}
	
	public String getPhoto_url() {
		return photo_url;
	}
	
	public void setNews_id(int news_id) {
		this.news_id = news_id;
	}
	
	public int getNews_id() {
		return news_id;
	}
	
	
	public void setCreated_at(int created_at) {
		this.created_at = created_at;
	}
	
	public int getCreated_at() {
		return created_at;
	}
	
	
	public void setUpdated_at(int updated_at) {
		this.updated_at = updated_at;
	}
	
	public int getUpdated_at() {
		return updated_at;
	}
	
	
	public void setIs_deleted(int is_deleted) {
		this.is_deleted = is_deleted;
	}
	
	public int getIs_deleted() {
		return is_deleted;
	}
}
