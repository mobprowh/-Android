package com.models;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DataWeather {
	
	@JsonProperty("rain")
	Object rain;
	
	@JsonProperty("name")
	Object name;
	
	@JsonProperty("coord")
	Object coord;
	
	@JsonProperty("sys")
	Object sys;
	
	
	@JsonProperty("base")
	Object base;
	
	
	@JsonProperty("wind")
	Object wind;
	
	@JsonProperty("clouds")
	Object clouds;
	
	@JsonProperty("dt")
	Object dt;
	
	
	@JsonProperty("cod")
	Object cod;
	
	@JsonProperty("message")
	Object message;
	
	@JsonProperty("id")
	int id;
	
	@JsonProperty("main")
	private Main main;
	
	@JsonProperty("weather")
	private ArrayList<Weather> weather;

	public void setWeather(ArrayList<Weather> weather) {
	    this.weather = weather;
	}
	
	public ArrayList<Weather> getWeather() {
	    return weather;
	}
	
	public void setMain(Main main) {
		this.main = main;
	}
	
	public Main getMain() {
	    return main;
	}
	
	
}
