package com.astrium.hmas.client.FeasibilityService;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               OSFeasibilityService.java
 *   File Type                                          :               Source Code
 *   Description                                        :               OpenSearch Service for feasibility
 *   																	tasking asynchronous interface
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

import java.util.Map;

import com.astrium.hmas.bean.FeasibilityBean.Parameter;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface OSFeasibilityServiceAsync {

	void getParameters(String url, AsyncCallback<Map<String, Parameter>> callback);

}
