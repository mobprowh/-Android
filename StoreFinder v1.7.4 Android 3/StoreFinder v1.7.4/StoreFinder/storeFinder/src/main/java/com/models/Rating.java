package com.models;

import com.fasterxml.jackson.annotation.JsonProperty;


public class Rating {
	
	
	@JsonProperty("rating")
	float rating;
	
	@JsonProperty("store_id")
	int store_id;
	
	@JsonProperty("rating_id")
	int rating_id;
	
	@JsonProperty("user_id")
	int user_id;
	
	@JsonProperty("created_at")
	int created_at;
	
	@JsonProperty("updated_at")
	int updated_at;
	
	@JsonProperty("is_deleted")
	int is_deleted;
	
	@JsonProperty("can_rate")
	int can_rate;

	public void setCan_rate(int can_rate) {
		this.can_rate = can_rate;
	}
	
	public int getCan_rate() {
		return can_rate;
	}
	
	public void setRating(float rating) {
		this.rating = rating;
	}
	
	public float getRating() {
		return rating;
	}
	
	
	public void setStore_id(int store_id) {
		this.store_id = store_id;
	}
	
	public int getStore_id() {
		return store_id;
	}
	
	
	public void setRating_id(int rating_id) {
		this.rating_id = rating_id;
	}
	
	public int getRating_id() {
		return rating_id;
	}
	
	
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	
	public int getUser_id() {
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
