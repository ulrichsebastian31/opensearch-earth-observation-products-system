package com.astrium.hmas.bean.OrderBean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class OrderItem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1331186595892772026L;
	public String itemID;
	public String productID;
	public Map<String,String> options;
	
	public OrderItem() {
		this.options = new HashMap<String, String>();
		}

	public String getItemID() {
		return itemID;
	}

	public void setItemID(String itemID) {
		this.itemID = itemID;
	}

	public String getProductID() {
		return productID;
	}

	public void setProductID(String productID) {
		this.productID = productID;
	}

	public Map<String, String> getOptions() {
		return options;
	}

	public void setOptions(Map<String, String> options) {
		this.options = options;
	}
	
	
	
	
}
