package com.astrium.hmas.client.ShopcartService;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               OrderServiceAsync.java
 *   File Type                                          :               Source Code
 *   Description                                        :               Order Service asynchronous
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

import com.astrium.hmas.bean.DownloadBean.DownloadProduct;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface OrderServiceAsync {

	void goDownload(DownloadProduct downloadProduct, AsyncCallback<String> callback);

}
