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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.astrium.hmas.bean.CatalogueBean.CatalogueResult;
import com.astrium.hmas.bean.DownloadBean.DownloadProduct;
import com.astrium.hmas.client.ShopcartService.ShopcartService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class ShopcartServiceImpl extends RemoteServiceServlet implements ShopcartService {

	@Override
	public Map<String, DownloadProduct> getShopcart(List<CatalogueResult> list_shopcart) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		/*
		 * Size of the selected CatalogueResult objects list
		 */
		int nb = list_shopcart.size();
		/*
		 * New object to place these objects
		 */
		Map<String, DownloadProduct> map = new HashMap<String, DownloadProduct>();

		if (nb > 0) {

			for (int i = 0; i < nb; i++) {

				CatalogueResult catalogueResult = new CatalogueResult();
				/*
				 * get an element of the list
				 */
				catalogueResult = list_shopcart.get(i);

				/*
				 * Then transform it to a DownloadProduct object
				 */
				DownloadProduct downloadProduct = new DownloadProduct();

				downloadProduct.setId(catalogueResult.getIdentifier());
				downloadProduct.setDate(catalogueResult.getStart() + "-" + catalogueResult.getEnd());
				downloadProduct.setPlatform(catalogueResult.getPlatform());
				downloadProduct.setSensor(catalogueResult.getInstrument());

				/*
				 * Put this new object in the map
				 */
				map.put(catalogueResult.getIdentifier(), downloadProduct);

			}
		}

		/*
		 * Return it to the client
		 */
		return map;
	}

}
