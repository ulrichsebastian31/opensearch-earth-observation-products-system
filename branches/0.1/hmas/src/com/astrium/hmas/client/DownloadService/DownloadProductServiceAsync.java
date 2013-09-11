package com.astrium.hmas.client.DownloadService;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               DownloadProductServiceAsync.java
 *   File Type                                          :               Source Code
 *   Description                                        :               Download Product service asynchronous
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

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DownloadProductServiceAsync {

	void getProducts(String url, AsyncCallback<String> callback);

}
