package com.astrium.hmas.server.OrderService;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               OrderServiceImpl.java
 *   File Type                                          :               Source Code
 *   Description                                        :               Order Service interface implementation
 *   																	Sends an ID product to the server and gets
 *   																	back its URI, useful for the client
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
import javax.ws.rs.core.MultivaluedMap;

import com.astrium.hmas.bean.DownloadBean.DownloadProduct;
import com.astrium.hmas.client.ShopcartService.OrderService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

@SuppressWarnings("serial")
public class OrderServiceImpl extends RemoteServiceServlet implements OrderService {

	@Override
	public String goDownload(DownloadProduct downloadProduct) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		/*
		 * Jersey Client creation
		 */
		Client client = new Client();
		/*
		 * Call to the server
		 */
		WebResource webResource = client.resource("http://localhost:8080/hmas_server-1.0-SNAPSHOT/hmas/order");
		/*
		 * parameters map
		 */
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		/*
		 * get the ID attribute from the object sent by the client
		 */
		String identifier = downloadProduct.getId();
		/*
		 * Add the ID to the request parameter
		 */
		queryParams.add("ProductID", identifier);

		/*
		 * Get the response from the server : the URI of the former object
		 */
		String uri = webResource.queryParams(queryParams).accept("text/html").get(String.class);

		/*
		 * return the URI to the client
		 */
		return uri;
	}

}
