package com.astrium.hmas.client.OrderService;

import java.util.Map;

import com.astrium.hmas.bean.DownloadBean.DownloadProduct;
import com.astrium.hmas.bean.OrderBean.Option;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               GetOptionsService.java
 *   File Type                                          :               Source Code
 *   Description                                        :               GetOptions for ordering Service interface
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
@RemoteServiceRelativePath("getOptions")
public interface GetOptionsService extends RemoteService{
	public Map<String,Option> getOptions(DownloadProduct downloadProduct) throws IllegalArgumentException;

}
