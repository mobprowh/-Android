package com.models;

import java.io.Serializable;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Store extends Rating implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3855881834307278660L;
	
	@JsonProperty("category_id")
	int category_id;
	
	@JsonProperty("created_at")
	int created_at;
	
	@JsonProperty("distance")
	double distance;
	
	@JsonProperty("email")
	String email;
	
	@JsonProperty("featured")
	int featured;
	
	@JsonProperty("icon_id")
	int icon_id;
	
	@JsonProperty("lat")
	double lat;
	
	@JsonProperty("lon")
	double lon;
	
	@JsonProperty("phone_no")
	String phone_no;
	
	@JsonProperty("rating_count")
	int rating_count;
	
	@JsonProperty("rating_total")
	int rating_total;
	
	@JsonProperty("sms_no")
	String sms_no;
	
	@JsonProperty("store_address")
	String store_address;
	
	@JsonProperty("store_desc")
	String store_desc;
	
	@JsonProperty("store_id")
	int store_id;
	
	@JsonProperty("store_name")
	String store_name;
	
	@JsonProperty("updated_at")
	int updated_at;
	
	@JsonProperty("website")
	String website;
	
	@JsonProperty("is_deleted")
	int is_deleted;
	
	@JsonProperty("slug")
	String slug;

	private ArrayList<Photo> photos;

	public void setPhotos(ArrayList<Photo> photos) {
		this.photos = photos;
	}
	public ArrayList<Photo> getPhotos() {
		return photos;
	}
	
	public void setSlug(String slug) {
		this.slug = slug;
	}
	
	public String getSlug() {
		return slug;
	}
	
	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}
	
	public int getCategory_id() {
		return category_id;
	}
	
	
	
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	public double getDistance() {
		return distance;
	}
	
	
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getEmail() {
		return email;
	}
	
	
	
	public void setFeatured(int featured) {
		this.featured = featured;
	}
	
	public int getFeatured() {
		return featured;
	}
	
	
	
	public void setIcon_id(int icon_id) {
		this.icon_id = icon_id;
	}
	
	public int getIcon_id() {
		return icon_id;
	}
	
	
	
	public void setLat(double lat) {
		this.lat = lat;
	}
	
	public double getLat() {
		return lat;
	}
	
	
	
	public void setLon(double lon) {
		this.lon = lon;
	}
	
	public double getLon() {
		return lon;
	}
	
	
	
	public void setPhone_no(String phone_no) {
		this.phone_no =phone_no;
	}
	
	public String getPhone_no() {
		return phone_no;
	}
	
	
	
	public void setRating_count(int rating_count) {
		this.rating_count =rating_count;
	}
	
	public int getRating_count() {
		return rating_count;
	}
	
	
	
	public void setRating_total(int rating_total) {
		this.rating_total =rating_total;
	}
	
	public int getRating_total() {
		return rating_total;
	}
	
	
	
	public void setSms_no(String sms_no) {
		this.sms_no =sms_no;
	}
	
	public String getSms_no() {
		return sms_no;
	}
	
	
	
	public void setStore_address(String store_address) {
		this.store_address =store_address;
	}
	
	public String getStore_address() {
		return store_address;
	}
	
	
	
	public void setStore_desc(String store_desc) {
		this.store_desc =store_desc;
	}
	
	public String getStore_desc() {
		return store_desc;
	}
	
	
	
	public void setStore_id(int store_id) {
		this.store_id =store_id;
	}
	
	public int getStore_id() {
		return store_id;
	}
	
	
	
	public void setStore_name(String store_name) {
		this.store_name =store_name;
	}
	
	public String getStore_name() {
		return store_name;
	}
	
	
	
	public void setWebsite(String website) {
		this.website = website;
	}
	
	public String getWebsite() {
		return website;
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
