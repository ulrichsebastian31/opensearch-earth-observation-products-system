package com.astrium.hmas.bean.OrderBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class Order implements Serializable, Comparator<Order>, Comparable<Order>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -645839685511168488L;
	public String id;
	public String status;
	private String orderRefence;
    private Date lastUpdate;
    private String orderItems;
    
    public Order(){
    }
	
    public Order(String orderID, String status, Date lastUpdate,String orderRefence,String orderItems) {
        this.id = orderID;
        this.status = status;
        this.orderItems = orderItems;
        this.orderRefence = orderRefence;
        this.lastUpdate = lastUpdate;
    }

    public String getOrderID() {
        return id;
    }

    public void setOrderID(String orderID) {
        this.id = orderID;
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
