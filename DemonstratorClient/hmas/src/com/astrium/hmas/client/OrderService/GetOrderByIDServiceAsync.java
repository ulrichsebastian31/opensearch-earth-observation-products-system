package com.astrium.hmas.client.OrderService;

import com.astrium.hmas.bean.OrderBean.Order;
import com.google.gwt.user.client.rpc.AsyncCallback;

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

public interface GetOrderByIDServiceAsync {

	void getOrderByID(Order order, AsyncCallback<String> callback);

}
