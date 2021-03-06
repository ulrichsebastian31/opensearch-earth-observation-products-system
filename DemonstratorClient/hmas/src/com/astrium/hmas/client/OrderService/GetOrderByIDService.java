package com.astrium.hmas.client.OrderService;

import com.astrium.hmas.bean.OrderBean.Order;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               GetOrderByIDService.java
 *   File Type                                          :               Source Code
 *   Description                                        :               Get Order by ID Service interface
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
@RemoteServiceRelativePath("getOrderByID")
public interface GetOrderByIDService extends RemoteService{
	public String getOrderByID(Order order) throws IllegalArgumentException;

}
