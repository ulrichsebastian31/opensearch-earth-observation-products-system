package com.astrium.hmas.bean.OrderBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Order implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -645839685511168488L;
	public String id;
	public String status;
	public List<OrderItem> orderItemList;
	
	public Order() {
		this.orderItemList = new ArrayList<OrderItem>();
	}

	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}



	public List<OrderItem> getOrderItemList() {
		return orderItemList;
	}

	public void setOrderItemList(List<OrderItem> orderItemList) {
		this.orderItemList = orderItemList;
	}
	
	
	
	
}
