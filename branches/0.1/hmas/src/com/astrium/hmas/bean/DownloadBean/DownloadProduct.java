package com.astrium.hmas.bean.DownloadBean;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               DownloadProduct.java
 *   File Type                                          :               Source Code
 *   Description                                        :               DownloadProduct bean : this class 
 *   																	describes the object to download
 *   																	for the user
 *
 * --------------------------------------------------------------------------------------------------------
 *
 * =================================================================
 *             (C) COPYRIGHT EADS ASTRIUM LIMITED 2013. All Rights Reserved
 *             This software is supplied by EADS Astrium Limited on the express terms
 *             that it is to be treated as confidential and that it may not be copied,
 *             used or disclosed to others for any purpose except as authorised in
 *             writing by this Company.
 * --------------------------------------------------------------------------------------------------------
 */


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
