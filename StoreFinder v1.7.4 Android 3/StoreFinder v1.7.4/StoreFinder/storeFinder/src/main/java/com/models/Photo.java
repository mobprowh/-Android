package com.models;

import java.io.Serializable;

public class Photo implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2942626033481733817L;
	
	int created_at;
	int photo_id;
	String photo_url;
	int store_id;
	String thumb_url;
	int updated_at;
	int is_deleted;

	public void setPhoto_id(int photo_id) {
		this.photo_id = photo_id;
	}
	
	public int getPhoto_id() {
		return photo_id;
	}
	
	public void setPhoto_url(String photo_url) {
		this.photo_url = photo_url;
	}
	
	public String getPhoto_url() {
		return photo_url;
	}
	
	
	public void setStore_id(int store_id) {
		this.store_id = store_id;
	}
	
	public int getStore_id() {
		return store_id;
	}
	
	
	public void setThumb_url(String thumb_url) {
		this.thumb_url = thumb_url;
	}
	
	public String getThumb_url() {
		return thumb_url;
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
