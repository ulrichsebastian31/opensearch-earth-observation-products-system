package com.astrium.hmas.client.CatalogueService;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               CatalogueServiceAsync.java
 *   File Type                                          :               Source Code
 *   Description                                        :               Catalogue Service asynchronous interface 
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

import java.util.Map;

import com.astrium.hmas.bean.CatalogueBean.CatalogueResult;
import com.astrium.hmas.bean.CatalogueBean.CatalogueSearch;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CatalogueServiceAsync {

	void getResults(CatalogueSearch catalogueSearch, AsyncCallback<Map<String, CatalogueResult>> callback);

}
