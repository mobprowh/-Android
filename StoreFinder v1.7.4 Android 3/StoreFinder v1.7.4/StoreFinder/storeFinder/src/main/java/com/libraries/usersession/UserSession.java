package com.libraries.usersession;

public class UserSession {

	private int user_id;
	private String login_hash;
	private String facebook_id;
	private String twitter_id;
	private String username;
	private String full_name;
	private String thumb_url;
	private String photo_url;
	private String email;
	
	public void setEmail(String email) {
		
		this.email = email;
	}
	
	public String getEmail() {
		
		return email;
	}
	
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
	
	public void setUser_id(int user_id) {
		
		this.user_id = user_id;
	}
	
	public int getUser_id() {
		
		return user_id;
	}
	
	
	public void setLogin_hash(String login_hash) {
		
		this.login_hash = login_hash;
	}
	
	public String getLogin_hash() {
		
		return login_hash;
	}
	

	public void setFacebook_id(String facebook_id) {
		
		this.facebook_id = facebook_id;
	}
	
	public String getFacebook_id() {
		
		return facebook_id;
	}
	
	
	public void setTwitter_id(String twitter_id) {
		
		this.twitter_id = twitter_id;
	}
	
	public String getTwitter_id() {
		
		return twitter_id;
	}
	

	public void setUsername(String username) {
		
		this.username = username;
	}
	
	public String getUsername() {
		
		return username;
	}
	
	public void setFull_name(String full_name) {
		
		this.full_name = full_name;
	}
	
	public String getFull_name() {
		
		return full_name;
	}
	
	
}
