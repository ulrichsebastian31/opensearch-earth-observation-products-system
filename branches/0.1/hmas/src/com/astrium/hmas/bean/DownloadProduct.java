package com.astrium.hmas.bean;

import java.io.Serializable;

public class DownloadProduct implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3617745513036044423L;
	public String platform;
	public String sensor;
	public String date;
	public String id;
	
	public DownloadProduct() {

	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getSensor() {
		return sensor;
	}

	public void setSensor(String sensor) {
		this.sensor = sensor;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
	
	

}
