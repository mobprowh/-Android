package com.models;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Main implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3053478382970635522L;
	
	@JsonProperty("temp_min")
	double temp_min;
	
	@JsonProperty("grnd_level")
	String grnd_level;
	
	@JsonProperty("sea_level")
	String sea_level;
	
	@JsonProperty("temp_max")
	double temp_max;
	
	double temp;
	double pressure;
	double humidity;

	public void setTemp(double temp) {
		this.temp = temp;
	}
	
	public double getTemp() {
		return temp;
	}
	
	public void setPressure(double pressure) {
		this.pressure = pressure;
	}
	
	public double getPressure() {
		return pressure;
	}
	
	public void setHumidity(double humidity) {
		this.humidity = humidity;
	}
	
	public double getHumidity() {
		return humidity;
	}

	
}
