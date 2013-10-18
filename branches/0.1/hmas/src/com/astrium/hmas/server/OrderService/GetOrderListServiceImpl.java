package com.astrium.hmas.server.OrderService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.astrium.hmas.bean.OrderBean.Order;
import com.astrium.hmas.bean.OrderBean.OrderItem;
import com.astrium.hmas.client.OrderService.GetOrderListService;
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

	@Override
	public Map<String, Order> getOrders() throws IllegalArgumentException{
		// TODO Auto-generated method stub
		try {
			Class.forName("org.postgresql.Driver");

		} catch (ClassNotFoundException e) {
			System.out.println("Where is your PostgreSQL JDBC Driver? Include in your library path!");
			e.printStackTrace();
		}

		/*
		 * Connection to the database
		 */
		System.out.println("-------- PostgreSQL JDBC Connection Testing ------------");

		System.out.println("PostgreSQL JDBC Driver Registered!");

		Connection connection = null;
		Statement st = null;
		Statement stbis = null;
		ResultSet rs = null;
		ResultSet rsbis = null;
		/*
		 * The map containing all the submitted orders to send to the client
		 */
		Map<String, Order> orders = new HashMap<String, Order>();

		try {
			
			connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/osresult", "postgres", "fightclub09");
			st = connection.createStatement();
			stbis = connection.createStatement();
			
			String query = "SELECT * FROM \"order\";";
			
			rs = st.executeQuery(query);
			
			while(rs.next()){
				/*
				 * Creation of one Order object for each SQL result found 
				 */
				Order order = new Order();
				String orderID = null;
				orderID = rs.getString("id");
				order.setId(orderID);
				order.setStatus(rs.getString("status"));
				
				/*
				 * List of the items ID stored in the db in String
				 */
				String items_id = rs.getString("items_id");
				/*
				 * List of OrderItem object to fill with the found SQL result
				 */
				List<OrderItem> items = new ArrayList<OrderItem>();
				
				String[] items_string = items_id.split(",");
				
				for(int i=0;i<items_string.length;i++){
					
					String item_id = items_string[i];
					/*
					 * search in the item table to find the item related to the order
					 */
					String item_query = "SELECT * FROM item WHERE item_id = '" + item_id + "';";
					rsbis = stbis.executeQuery(item_query);
					
					while(rsbis.next()){
						/*
						 * Creation of one OrderItem object
						 */
						OrderItem orderItem = new OrderItem();
						orderItem.setItemID(rsbis.getString("item_id"));
						orderItem.setProductID(rsbis.getString("product_id"));
						/*
						 * All the options set for this item
						 */
						Map<String, String> options = new HashMap<String,String>();
						
						options.put("processingLevel", rsbis.getString("processing_level"));
						options.put("qualityOfService", rsbis.getString("quality_of_service"));
						options.put("productType", rsbis.getString("product_type"));
						
						orderItem.setOptions(options);
						
						items.add(orderItem);
					}
				}
				
				order.setOrderItemList(items);
				
				orders.put(orderID, order);
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(orders.size());
		
		return orders;
	}

}
