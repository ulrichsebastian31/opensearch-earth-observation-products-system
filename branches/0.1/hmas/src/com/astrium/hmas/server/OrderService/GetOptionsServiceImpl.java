package com.astrium.hmas.server.OrderService;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import com.astrium.hmas.bean.CatalogueBean.CatalogueResult;
import com.astrium.hmas.bean.DownloadBean.DownloadProduct;
import com.astrium.hmas.bean.OrderBean.Option;
import com.astrium.hmas.client.OrderService.GetOptionsService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               GetOptionsServiceImpl.java
 *   File Type                                          :               Source Code
 *   Description                                        :               Get Options Service interface implementation
 *   																	Sends the list of the chosen options 
 *   																	for a product to order
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
public class GetOptionsServiceImpl extends RemoteServiceServlet implements GetOptionsService{

	@Override
	public Map<String, Option> getOptions(DownloadProduct downloadProduct) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		/*
		 * Jersey Client configuration
		 */
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getProperties().put(ClientConfig.PROPERTY_READ_TIMEOUT, 30000);
		clientConfig.getProperties().put(ClientConfig.PROPERTY_CONNECT_TIMEOUT, 30000);

		/*
		 * Jersey Client creation
		 */
		Client client = new Client();
		/*
		 * Call to the server
		 */
		WebResource webResource = client.resource("http://localhost:8080/hmas_server-1.0-SNAPSHOT/hmas/order/getOptions");
		/*
		 * parameters map
		 */
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		
		/*
		 * Add the product identifier to the request as a KVP
		 */
		queryParams.add("ProductID", downloadProduct.getId());
		
		/*
		 * Get the response : the XML file
		 */
		String s = webResource.queryParams(queryParams).accept("application/atom+xml").get(String.class);
		
		/*
		 * The object where we will put the results from the XML response
		 */
		Map<String, Option> options = new HashMap<String, Option>();
		
		
		return options;
	}

}
