package com.models;

import java.io.Serializable;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseReview implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3993047164260406504L;
	private int request_count;
	private int return_count;
	private int total_row_count;
	private ArrayList<Review> reviews;
	
	@JsonProperty("status")
	private Status status;
	
	public void setStatus(Status status) {
	    this.status = status;
	}
	
	public Status getStatus() {
	    return status;
	}

	public ResponseReview() {
		
	}
	
	public void setTotal_row_count(int total_row_count) {
	    this.total_row_count = total_row_count;
	}
	
	public int getTotal_row_count() {
	    return total_row_count;
	}
	
	public void setReturn_count(int return_count) {
	    this.return_count = return_count;
	}
	
	public int getReturn_count() {
	    return return_count;
	}
	
	public void setRequest_count(int request_count) {
	    this.request_count = request_count;
	}
	
	public int getRequest_count() {
	    return request_count;
	}
	
	public void setReviews(ArrayList<Review> reviews) {
		this.reviews = reviews;
	}
	
	public ArrayList<Review> getReviews() {
	    return reviews;
	}
	
	
}
