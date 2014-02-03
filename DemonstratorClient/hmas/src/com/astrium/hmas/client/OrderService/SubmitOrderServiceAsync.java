package com.astrium.hmas.client.OrderService;

import java.util.Map;

import com.astrium.hmas.bean.DownloadBean.DownloadProduct;
import com.astrium.hmas.bean.OrderBean.Order;
import com.astrium.hmas.bean.OrderBean.OrderItem;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               SubmitOrderService.java
 *   File Type                                          :               Source Code
 *   Description                                        :               Submit Order Service for ordering Service
 *   																	asynchronous interface
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

public interface SubmitOrderServiceAsync {

	void submitOrder(Map<String, OrderItem> orderItems, AsyncCallback<String> callback);

}
