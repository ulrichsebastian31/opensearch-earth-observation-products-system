package com.astrium.hmas.bean;

public class DownloadProduct {
	public boolean availibity;
	public int status;
	public String platform;
	public String sensor;
	public String date;
	public int id;
	
	public DownloadProduct(boolean availibity, int status, String platform,
			String sensor, String date, int id) {
		super();
		this.availibity = availibity;
		this.status = status;
		this.platform = platform;
		this.sensor = sensor;
		this.date = date;
		this.id = id;
	}

	public boolean isAvailibity() {
		return availibity;
	}

	public void setAvailibity(boolean availibity) {
		this.availibity = availibity;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
	
	

}
