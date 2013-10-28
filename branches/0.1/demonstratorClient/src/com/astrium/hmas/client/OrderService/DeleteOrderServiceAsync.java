package com.astrium.hmas.client.OrderService;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               DeleteOrderServiceAsync.java
 *   File Type                                          :               Source Code
 *   Description                                        :               Cancel an Order service asynchronous 
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

import com.astrium.hmas.bean.OrderBean.Order;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DeleteOrderServiceAsync {

	void deleteOrder(Order order, AsyncCallback<Void> callback);

}
