package com.astrium.hmas.client.ShopcartService;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               ShopcartServiceAsync.java
 *   File Type                                          :               Source Code
 *   Description                                        :               Shopcart Service asynchronous
 *   																	interface
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

import java.util.List;
import java.util.Map;

import com.astrium.hmas.bean.CatalogueBean.CatalogueResult;
import com.astrium.hmas.bean.DownloadBean.DownloadProduct;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ShopcartServiceAsync {

	void getShopcart(List<CatalogueResult> list_shopcart, AsyncCallback<Map<String, DownloadProduct>> callback);

}
