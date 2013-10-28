package com.astrium.hmas.client.OrderService;

import com.astrium.hmas.bean.OrderBean.Order;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               DeleteOrderService.java
 *   File Type                                          :               Source Code
 *   Description                                        :               Cancel an Order service interface
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
@RemoteServiceRelativePath("deleteOrder")
public interface DeleteOrderService extends RemoteService{

	public void deleteOrder(Order order) throws IllegalArgumentException;
}
