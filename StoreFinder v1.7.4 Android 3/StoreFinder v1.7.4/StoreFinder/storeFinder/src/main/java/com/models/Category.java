package com.models;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Category implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3053478382970635522L;
	int created_at;
	String category;
	String category_icon;
	int category_id;
	int updated_at;
	int is_deleted;

	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory_icon(String category_icon) {
		this.category_icon = category_icon;
	}
	
	public String getCategory_icon() {
		return category_icon;
	}
	
	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}
	
	public int getCategory_id() {
		return category_id;
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
