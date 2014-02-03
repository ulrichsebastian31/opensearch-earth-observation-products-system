package com.astrium.hmas.bean.OrderBean;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author re-cetienne
 */
public class EOProduct implements Serializable{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 6778163367494930490L;
	private String productID;
    private String collectionID;
    private String options;
    private String sensor;
    private String platform;
    private Date lastUpdate;

    public EOProduct() {
    	super();
	}

	public EOProduct(String productID, String collectionID, String options, Date lastUpdate) {
        this.productID = productID;
        this.collectionID = collectionID;
        this.options = options;
        this.lastUpdate = lastUpdate;
    }
    
     public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getCollectionID() {
        return collectionID;
    }

    public void setCollectionID(String collectionID) {
        this.collectionID = collectionID;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

	public String getSensor() {
		return sensor;
	}

	public void setSensor(String sensor) {
		this.sensor = sensor;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}
    
    
    
}
