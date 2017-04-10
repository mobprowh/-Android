package com.models;

import java.io.Serializable;

public class Weather implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3053478382970635522L;
	int id;
	String main;
	String description;
	String icon;

	
	
	
	
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public void setMain(String main) {
		this.main = main;
	}
	
	public String getMain() {
		return main;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
	
	
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	public String getIcon() {
		return icon;
	}
	
}
