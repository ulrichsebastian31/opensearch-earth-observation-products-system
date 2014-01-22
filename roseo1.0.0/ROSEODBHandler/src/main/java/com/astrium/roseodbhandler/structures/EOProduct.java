/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.astrium.roseodbhandler.structures;

import java.util.Date;

/**
 *
 * @author re-cetienne
 */
public class EOProduct {
    
    private String productID;
    private String collectionID;
    private String options;
    private Date lastUpdate;

   

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
    
    
    
}
