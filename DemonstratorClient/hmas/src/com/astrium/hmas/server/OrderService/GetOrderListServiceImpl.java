package com.astrium.hmas.server.OrderService;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.astrium.hmas.bean.OrderBean.Order;
import com.astrium.hmas.client.OrderService.GetOrderListService;
import com.astrium.roseodbhandler.RoseoManagementHandler;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               GetOrderListServiceImpl.java
 *   File Type                                          :               Source Code
 *   Description                                        :               Get Order List Service interface implementation
 *   																	Sends the list of all the registered
 *   																	Order submitted by the user
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

@SuppressWarnings("serial")
public class GetOrderListServiceImpl extends RemoteServiceServlet implements GetOrderListService{

	public static String url = "jdbc:postgresql://localhost:5432/ROSEODatabase";
	public static String user = "postgres";
	public static String passwd = "password";

	@Override
	public Map<String, Order> getOrders() throws Exception{
		// TODO Auto-generated method stub

		RoseoManagementHandler roseomh = new RoseoManagementHandler(url, user, passwd);

		/*
		 * The map containing all the submitted orders to send to the client
		 */
		Map<String, Order> orders = new HashMap<String, Order>();

		Map<String, com.astrium.roseodbhandler.structures.Order> orders_map = roseomh.getOrders();
		Iterator<String> keys = orders_map.keySet().iterator();

		while(keys.hasNext()){

			String key = keys.next();
			/*
			 * Creation of one Order object for each SQL result found 
			 */
			Order order = new Order();
			String orderID = orders_map.get(key).getOrderID();
			order.setOrderID(orderID);
			order.setStatus(orders_map.get(key).getStatus());

			/*
			 * List of the items ID stored in the db in String
			 */
			String items_id = orders_map.get(key).getOrderItems();
			
			order.setOrderItems(items_id);
			
			order.setLastUpdate(orders_map.get(key).getLastUpdate());
			order.setOrderRefence(orders_map.get(key).getOrderRefence());

			if(!order.getStatus().equals("Cancelled")){
				orders.put(orderID, order);
			}
			

		}

	
	System.out.println(orders.size());

	return orders;
}

}
