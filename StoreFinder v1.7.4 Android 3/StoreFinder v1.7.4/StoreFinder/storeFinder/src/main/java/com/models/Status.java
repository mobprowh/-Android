package com.models;

import java.io.Serializable;

//@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = As.WRAPPER_OBJECT)
//@JsonSubTypes.Type(name = "status", value = Status.class)
public class Status implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9131165989663827671L;
	
	int status_code;
	String status_text;
	

	public void setStatus_text(String status_text) {
		this.status_text = status_text;
	}
	
	public String getStatus_text() {
		return status_text;
	}
	
	public void setStatus_code(int status_code) {
		this.status_code = status_code;
	}
	
	public int getStatus_code() {
		return status_code;
	}
	
}
