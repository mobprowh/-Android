package com.models;

import java.util.ArrayList;

public class Data {
	
	private ArrayList<Store> stores;
	private ArrayList<Category> categories;
	private ArrayList<Photo> photos;
	private ArrayList<News> news;

	private float max_distance;
	private float default_distance;

	public void setDefault_distance(float default_distance) {
		this.default_distance = default_distance;
	}
	public float getDefault_distance() {
		return default_distance;
	}

	public void setMax_distance(float max_distance) {
		this.max_distance = max_distance;
	}
	public float getMax_distance() {
		return max_distance;
	}

	public void setStores(ArrayList<Store> s) {
	    stores = s;
	}
	public ArrayList<Store> getStores() {
	    return stores;
	}
	
	public void setCategories(ArrayList<Category> s) {
		categories = s;
	}
	public ArrayList<Category> getCategories() {
	    return categories;
	}

	public void setPhotos(ArrayList<Photo> photos) {
	    this.photos = photos;
	}
	public ArrayList<Photo> getPhotos() {
	    return photos;
	}

	public void setNews(ArrayList<News> news) {
		this.news= news;
	}
	public ArrayList<News> getNews() {
		return news;
	}
}
