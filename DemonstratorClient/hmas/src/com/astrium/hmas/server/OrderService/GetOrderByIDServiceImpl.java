package com.astrium.hmas.server.OrderService;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.ws.rs.core.MultivaluedMap;

import com.astrium.hmas.bean.OrderBean.Order;
import com.astrium.hmas.client.Hmas;
import com.astrium.hmas.client.OrderService.GetOrderByIDService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               GetOrderByIDServiceImpl.java
 *   File Type                                          :               Source Code
 *   Description                                        :               Gets all this information about one order
 *   																	thanks to its ID. Sends back a XML file
 *   																	with the status of this order
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
public class GetOrderByIDServiceImpl extends RemoteServiceServlet implements GetOrderByIDService{

	@Override
	public String getOrderByID(Order order) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
		/*
		 * Jersey Client creation
		 */
		Client client = new Client();
		/*
		 * Call to the server
		 */
		WebResource webResource = client.resource("http://localhost:8080/ROSEO-0.0.1-SNAPSHOT/ROSEO/1.0.0/order/" + order.getOrderID());
		/*
		 * Get the response : the XML file
		 */
		String s = webResource.toString();
		
		return s;
	}

}
