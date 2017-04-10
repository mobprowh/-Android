package com.models;

public class DataResponse {

	private Status status;

	private User user_info;
	
	private User photo_user_info;
	

	public DataResponse() {
		
	}
	
	public void setStatus(Status status) {
	    this.status = status;
	}
	
	public Status getStatus() {
	    return status;
	}
	
	public void setUser_info(User user_info) {
		this.user_info = user_info;
	}
	
	public User getUser_info() {
	    return user_info;
	}
	
	public void setPhoto_user_info(User photo_user_info) {
		this.photo_user_info = photo_user_info;
	}
	
	public User getPhoto_user_info() {
	    return photo_user_info;
	}
	
	
}
