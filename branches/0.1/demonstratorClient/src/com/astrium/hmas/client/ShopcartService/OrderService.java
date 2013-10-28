package com.astrium.hmas.client.ShopcartService;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               OrderService.java
 *   File Type                                          :               Source Code
 *   Description                                        :               Order Service interface
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
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("order")
public interface OrderService extends RemoteService {
	public String goDownload(DownloadProduct downloadProduct) throws IllegalArgumentException;

}
