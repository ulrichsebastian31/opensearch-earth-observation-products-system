package com.astrium.hmas.server.OrderService;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               ShopcartServiceImpl.java
 *   File Type                                          :               Source Code
 *   Description                                        :               Get the list of the selected CatalogueResult
 *   																	objects and returns them as a list of 
 *   																	DownloadProduct
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

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import net.eads.astrium.dream.util.DateHandler;

import com.astrium.hmas.bean.CatalogueBean.CatalogueResult;
import com.astrium.hmas.bean.OrderBean.EOProduct;
import com.astrium.hmas.client.ShopcartService.ShopcartService;
import com.astrium.roseodbhandler.RoseoManagementHandler;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class ShopcartServiceImpl extends RemoteServiceServlet implements ShopcartService {
	
	public static String url = "jdbc:postgresql://localhost:5432/ROSEODatabase";
    public static String user = "postgres";
    public static String passwd = "password";

	@Override
	public Map<String, EOProduct> getShopcart(List<CatalogueResult> list_shopcart) throws Exception {
		// TODO Auto-generated method stub
		/*
		 * Size of the selected CatalogueResult objects list
		 */
		int nb = list_shopcart.size();
		/*
		 * New object to place these objects
		 */
		Map<String, EOProduct> map = new HashMap<String, EOProduct>();
		
		RoseoManagementHandler roseomh = new RoseoManagementHandler(url,user,passwd);

		if (nb > 0) {

			for (int i = 0; i < nb; i++) {

				CatalogueResult catalogueResult = new CatalogueResult();
				/*
				 * get an element of the list
				 */
				catalogueResult = list_shopcart.get(i);

				/*
				 * Then transform it to a EOProduct object
				 */
				EOProduct eoProduct = new EOProduct();
				String productID = catalogueResult.getIdentifier();
				eoProduct.setProductID(productID);
				String lastUpdateString = catalogueResult.getEnd();
				Date lastUpdate = DateHandler.parseDate(lastUpdateString);
				
				eoProduct.setLastUpdate(lastUpdate);
				eoProduct.setPlatform(catalogueResult.getPlatform());
				eoProduct.setSensor(catalogueResult.getInstrument());
				eoProduct.setCollectionID("null");
				
				InputStream optFile = new FileInputStream(System.getProperty("user.home") + File.separator + "hmas" + File.separator + "options4.json");
		        String options = IOUtils.toString(optFile,"UTF-8");
				
				eoProduct.setOptions(options);
				
				//Add the new eoProduct to the ROSEODatabase for Order
				roseomh.addEOProduct(productID, "null", options, lastUpdateString);

				/*
				 * Put this new object in the map
				 */
				map.put(catalogueResult.getIdentifier(), eoProduct);
			}
		}

		/*
		 * Return it to the client
		 */
		return map;
	}

}
