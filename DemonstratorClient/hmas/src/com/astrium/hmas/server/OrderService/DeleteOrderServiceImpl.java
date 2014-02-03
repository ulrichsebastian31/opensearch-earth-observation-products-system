package com.astrium.hmas.server.OrderService;

import com.astrium.hmas.bean.OrderBean.Order;
import com.astrium.hmas.client.OrderService.DeleteOrderService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;



/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               DeleteOrderServiceImpl.java
 *   File Type                                          :               Source Code
 *   Description                                        :               Cancel an Order by changing its status
 *   																	in the database
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
public class DeleteOrderServiceImpl extends RemoteServiceServlet implements DeleteOrderService {

	@Override
	public void deleteOrder(Order order) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		Client client = new Client();

		WebResource webResource = client.resource("http://localhost:8080/ROSEO-0.0.1-SNAPSHOT/ROSEO/1.0.0/");
		WebResource webResourceTest = webResource.path("order/" + order.getOrderID());
		final String responseString = webResourceTest.delete(String.class);

		System.out.println(responseString);


	}

}
