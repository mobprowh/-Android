package com.models;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Review implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3049347291793495849L;
	
	

	@JsonProperty("first_name")
	String first_name;
	
	@JsonProperty("last_name")
	String last_name;
	
	String review;
	
	int review_id;
	
	@JsonProperty("store_id")
	int store_id;
	
	@JsonProperty("user_id")
	int user_id;
	
	int created_at;
	
	@JsonProperty("updated_at")
	int updated_at;
	
	@JsonProperty("is_deleted")
	int is_deleted;
	
	@JsonProperty("full_name")
	String full_name;
	
	@JsonProperty("thumb_url")
	String thumb_url;
	
	@JsonProperty("photo_url")
	String photo_url;
	
	@JsonProperty("username")
	String username;

	public void setPhoto_url(String photo_url) {
		this.photo_url = photo_url;
	}
	
	public String getPhoto_url() {
		return photo_url;
	}
	
	public void setThumb_url(String thumb_url) {
		this.thumb_url = thumb_url;
	}
	
	public String getThumb_url() {
		return thumb_url;
	}
	
	
	public void setFull_name(String full_name) {
		this.full_name = full_name;
	}
	
	public String getFull_name() {
		return full_name;
	}
	
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	
	public String getFirst_name() {
		return first_name;
	}
	
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	
	public String getLast_name() {
		return last_name;
	}
	
	public void setReview(String review) {
		this.review = review;
	}
	
	public String getReview() {
		return review;
	}
	
	public void setReview_id(int review_id) {
		this.review_id = review_id;
	}
	
	public int getReview_id() {
		return review_id;
	}
	
	public void setStore_id(int store_id) {
		this.store_id = store_id;
	}
	
	public int getStore_id() {
		return store_id;
	}
	
	public void setuser_id(int user_id) {
		this.user_id = user_id;
	}
	
	public int getuser_id() {
		return user_id;
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
