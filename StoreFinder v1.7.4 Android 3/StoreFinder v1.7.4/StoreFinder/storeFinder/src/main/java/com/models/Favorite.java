package com.models;

public class Favorite {
	
	int created_at;
	int favorite_id;
	int store_id;
	int updated_at;
	int is_deleted;

	public void setFavorite_id(int favorite_id) {
		this.favorite_id = favorite_id;
	}
	
	public int getFavorite_id() {
		return favorite_id;
	}
	
	
	public void setStore_id(int store_id) {
		this.store_id =store_id;
	}
	
	public int getStore_id() {
		return store_id;
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
