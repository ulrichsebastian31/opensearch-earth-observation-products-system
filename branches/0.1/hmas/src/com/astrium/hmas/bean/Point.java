package com.astrium.hmas.bean;

import java.io.Serializable;

public class Point implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4312803563977148910L;
	public double latitude;
	public double longitude;
	
	public Point() {
	}

	public Point(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	

}
