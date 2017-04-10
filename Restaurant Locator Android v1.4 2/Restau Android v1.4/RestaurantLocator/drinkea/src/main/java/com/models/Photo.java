package com.models;

import java.io.Serializable;

public class Photo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6422572753453920743L;
	
	public String created_at;
	public int photo_id;
	public String photo_url;
	public int restaurant_id;
	public String thumb_url;
	
}
