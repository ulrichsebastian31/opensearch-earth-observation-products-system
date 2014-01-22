/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.astrium.roseodbhandler.structures;

import java.util.Comparator;
import java.util.Date;

/**
 *
 * @author re-cetienne
 */
public class Order implements Comparator<Order>, Comparable<Order>{
    
    private String orderID;
    private String status;
    private String orderRefence;
    private Date lastUpdate;
    private String orderItems;

    public Order(String orderID, String status, Date lastUpdate,String orderRefence,String orderItems) {
        this.orderID = orderID;
        this.status = status;
        this.orderItems = orderItems;
        this.orderRefence = orderRefence;
        this.lastUpdate = lastUpdate;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }
    
     public String getOrderRefence() {
        return orderRefence;
    }

    public void setOrderRefence(String orderRefence) {
        this.orderRefence = orderRefence;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(String orderItems) {
        this.orderItems = orderItems;
    }

    @Override
    public int compare(Order o1, Order o2) {
        return o1.getLastUpdate().compareTo(o2.getLastUpdate());
    }

    @Override
    public int compareTo(Order o) {
        return this.getLastUpdate().compareTo(o.getLastUpdate());
    }
    
    
}
