package com.astrium.hmas.client.FeasibilityService;

/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               HMA-S
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               FeasibilityService.java
 *   File Type                                          :               Source Code
 *   Description                                        :               Feasibility Service interface
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

import com.astrium.hmas.bean.FeasibilityBean.FeasibilityResult;
import com.astrium.hmas.bean.FeasibilityBean.FeasibilitySearch;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("feasibilityResult")
public interface FeasibilityService extends RemoteService {
	public Map<String, FeasibilityResult> getResults(FeasibilitySearch feasibilitySearch) throws IllegalArgumentException;

}
