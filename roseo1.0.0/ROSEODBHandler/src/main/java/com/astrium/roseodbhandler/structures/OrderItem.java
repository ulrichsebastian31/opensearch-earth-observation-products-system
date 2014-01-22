/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.astrium.roseodbhandler.structures;

/**
 *
 * @author re-cetienne
 */
public class OrderItem {
    
    private String itemID;
    private String eoProductID;
    private String setOptions;
    private String status;

    public OrderItem(String itemID, String eoProductID, String setOptions, String status) {
        this.itemID = itemID;
        this.eoProductID = eoProductID;
        this.setOptions = setOptions;
        this.status = status;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getEoProductID() {
        return eoProductID;
    }

    public void setEoProductID(String eoProductID) {
        this.eoProductID = eoProductID;
    }

    public String getSetOptions() {
        return setOptions;
    }

    public void setSetOptions(String setOptions) {
        this.setOptions = setOptions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    
    
}
